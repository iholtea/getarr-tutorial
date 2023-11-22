package io.ionuth.invoice.service.imp;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import io.ionuth.invoice.model.AppUser;
import io.ionuth.invoice.model.VerifyData;
import io.ionuth.invoice.repository.UserRepository;
import io.ionuth.invoice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepo;
	private final EmailService emailService;
	
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
	public void sendVerificationCode(AppUser user) {
		Date expDate = DateUtils.addMinutes(new Date(),15); 
		String verifyCode = RandomStringUtils.randomAlphabetic(6).toUpperCase();
		VerifyData verifyData = new VerifyData(user.getUserId(), verifyCode, 
				 new Timestamp(expDate.getTime()));
		userRepo.addMfaVerifyCode(verifyData);
		// we could use a payed SMS service like Twilio or maybe AWS
		// for now use send the verification code via email
		String mailSubject = "GetArrays App login code";
		String mailContent = "Verification code: " + verifyCode;
		emailService.sendEmail(mailSubject, mailContent, user.getEmail());
	}
}
