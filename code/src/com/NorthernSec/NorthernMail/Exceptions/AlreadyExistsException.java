package com.NorthernSec.NorthernMail.Exceptions;

public class AlreadyExistsException extends Exception{
	public AlreadyExistsException(String message){
		super(message);
	}
	public AlreadyExistsException(Exception e){
		super(e);
	}
}
