package com.NorthernSec.NorthernMail.Exceptions;

public class PrivKeyEncryptedException extends Exception {
	public PrivKeyEncryptedException(String message){
		super(message);
	}
	public PrivKeyEncryptedException(Exception e){
		super(e);
	}
}
