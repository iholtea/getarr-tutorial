package io.ionuth.invoice.model;

import java.sql.Timestamp;

/*
 *  Class used for
 *    MultiFactor Authentication Code
 *    Reset password verification URL
 *    Verify account URL
 */
public class VerifyData {
	
	private Long userId;
	private String verifyStr;
	private Timestamp verifyDate;
	
	public VerifyData() {}
	
	public VerifyData(Long userId, String verifyStr, Timestamp verifyDate) {
		super();
		this.userId = userId;
		this.verifyStr = verifyStr;
		this.verifyDate = verifyDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getVerifyStr() {
		return verifyStr;
	}

	public void setVerifyStr(String verifyStr) {
		this.verifyStr = verifyStr;
	}

	public Timestamp getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Timestamp verifyDate) {
		this.verifyDate = verifyDate;
	}
	
}
