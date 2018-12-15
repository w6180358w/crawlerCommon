package com.black.web.base.utils.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SendMailAuthenticator extends Authenticator {
	String uid, psd;

	public SendMailAuthenticator(String uid, String psd) {
		this.uid = uid;
		this.psd = psd;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(uid, psd);
	}

}
