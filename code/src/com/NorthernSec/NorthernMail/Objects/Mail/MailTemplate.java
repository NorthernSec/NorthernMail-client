package com.NorthernSec.NorthernMail.Objects.Mail;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.JSONObject;

import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;
import com.NorthernSec.NorthernMail.Exceptions.InvalidMailException;
import com.NorthernSec.NorthernMail.Objects.Util;

import sun.misc.BASE64Encoder;


public class MailTemplate {
	private String subject;
	private String message;
	private PrivateKey signature;
	private PublicKey publicKey;
	
	public void setSubject(String subject){this.subject=subject;}
	public void setMessage(String message){this.message=message;}
	public void encryptWith(PublicKey key){this.publicKey=key;}
	public void signWith(PrivateKey key){this.signature=key;}
	
	public String getReadyMail() throws EncryptionException, InvalidKeyException, InvalidMailException{
		if(subject == null || message == null){throw new InvalidMailException("Uninitialized mail");}
		if(subject.length()==0 || message.length()==0){throw new InvalidMailException("Empty subject or message");}
		JSONObject mail = new JSONObject();
		String strSubj = subject;
		String strMess = message;
		String strToken = "unlocked";
		//TODO: addition of date. Just not sure if the date is "sensitive data" and should be encrypted
		//TODO: Adding a random number and the encrypted version (short token to try to decrypt to see if a message was intended for you)
		BASE64Encoder b64 = new BASE64Encoder();
		try{
			final Cipher cipher = Cipher.getInstance("RSA");
			if(publicKey != null){
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				strMess=b64.encode(cipher.doFinal(Util.compress(message.getBytes())));
				strSubj=b64.encode(cipher.doFinal(Util.compress(subject.getBytes())));
				String strSalt = new BigInteger(130, new SecureRandom()).toString();
				byte[] compressed = Util.compress((strSalt+strToken).getBytes());
				strToken = strSalt+"::"+b64.encode(cipher.doFinal(compressed));
			}
			if(signature != null){
				byte[] digest = Util.shaCreate(message);
				cipher.init(Cipher.ENCRYPT_MODE, signature);
				mail.put("signature", b64.encode(cipher.doFinal(Util.compress(digest))));
			}
		}catch(IOException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e){
			throw new EncryptionException(e);
		}
		mail.put("subject", strSubj);
		mail.put("message", strMess);
		mail.put("token",strToken);
		return mail.toJSONString();
	}
}
