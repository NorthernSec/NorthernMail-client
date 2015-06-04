package com.NorthernSec.NorthernMail.Exceptions;

import com.NorthernSec.NorthernMail.Objects.Mail.DecryptedMail;

public class InvalidSignatureException extends Exception{
	private static final long serialVersionUID = 1L;
	private DecryptedMail mail;
	public InvalidSignatureException(DecryptedMail mail, String message){
		super(message);
		this.mail=mail;
	}
	public InvalidSignatureException(DecryptedMail mail, Exception e){
		super(e);
		this.mail=mail;
	}
	public DecryptedMail getMail(){return mail;}
}
