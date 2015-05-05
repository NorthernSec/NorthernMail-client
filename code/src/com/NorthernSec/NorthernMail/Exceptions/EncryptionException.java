package com.NorthernSec.NorthernMail.Exceptions;

public class EncryptionException extends Exception {
  public EncryptionException(String message){
	super(message);
  }
  public EncryptionException(Exception e){
	super(e);
  }
}
