package io.ionuth.invoice.model;

/*
 *  Class to contain data of JOIN between AppUser and VerifyData
 */
public class UserVerify {
	
	private AppUser appUser;
	private VerifyData verifyData;
	
	public UserVerify() {
		appUser = new AppUser();
		verifyData = new VerifyData();
	}
	
	public UserVerify(AppUser appUser, VerifyData verifyData) {
		super();
		this.appUser = appUser;
		this.verifyData = verifyData;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public VerifyData getVerifyData() {
		return verifyData;
	}

	public void setVerifyData(VerifyData verifyData) {
		this.verifyData = verifyData;
	}
	
	
	
	
}
