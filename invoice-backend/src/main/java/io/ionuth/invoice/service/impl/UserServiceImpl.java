package io.ionuth.invoice.service.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.ionuth.invoice.model.AppUser;
import io.ionuth.invoice.model.UserVerify;
import io.ionuth.invoice.model.VerifyData;
import io.ionuth.invoice.repository.UserRepository;
import io.ionuth.invoice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepo;
	private final EmailService emailService;
	
	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	// constructor injection
	public UserServiceImpl(UserRepository userRepo, EmailService emailService) {
		this.userRepo = userRepo;
		this.emailService = emailService;
	}
	
	@Override
	public AppUser createUser(AppUser appUser) {
		return userRepo.create(appUser);
	}
	
	@Override
	public AppUser getUserByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}
	
	@Override
	public String sendVerificationCode(AppUser user) {
		Date expDate = DateUtils.addMinutes(new Date(),15); 
		String verificationCode = RandomStringUtils.randomAlphabetic(6).toUpperCase();
		logger.info("Generated login verification code: {}", verificationCode);
		VerifyData verifyData = new VerifyData(user.getUserId(), verificationCode, 
				 new Timestamp(expDate.getTime()));
		userRepo.addVerifyMfaCode(verifyData);
		// we could use a payed SMS service like Twilio or maybe AWS
		// for now use send the verification code via email
		String mailSubject = "GetArrays App login code";
		String mailContent = "Verification code: " + verificationCode;
		emailService.sendEmail(mailSubject, mailContent, user.getEmail());
		return verificationCode;
	}
	
	@Override
	public AppUser verifyCode(String email, String code) {
		UserVerify userVerify = userRepo.verifyMfaCode(email,code);
		if( userVerify.getVerifyData().getVerifyDate().before(new Date()) ) {
			// verification code expired -> throw exception
			return null;
		}
		userRepo.deteleMfaCodeByUserId(userVerify.getAppUser().getUserId());
		return userVerify.getAppUser(); 
	}
}
