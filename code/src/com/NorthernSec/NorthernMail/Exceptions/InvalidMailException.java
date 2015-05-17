package com.NorthernSec.NorthernMail.Exceptions;

public class InvalidMailException extends Exception{
	public InvalidMailException(String message){
		super(message);
	}
	public InvalidMailException(Exception e){
		super(e);
	}
}
