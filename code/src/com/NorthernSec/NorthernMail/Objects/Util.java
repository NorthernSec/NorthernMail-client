package com.NorthernSec.NorthernMail.Objects;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

	public static byte[] shaCreate(String pass){
        try{
		String key = pass;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(key.getBytes("UTF-8"));
		byte[] longdigest = md.digest();
		//byte[] digest = new byte[16];
		//System.arraycopy(longdigest, 0, digest, 0, 16);
		//return digest;
		return longdigest;
        }catch(NoSuchAlgorithmException | UnsupportedEncodingException e){return null;}
    }
}
