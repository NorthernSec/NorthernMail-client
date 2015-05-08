package com.NorthernSec.NorthernMail.Objects;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.JSONObject;

import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;

import sun.misc.BASE64Encoder;


public class Mail {
	private String subject;
	private String message;
	private PrivateKey signature;
	private PublicKey publicKey;
	
	public void setSubject(String subject){this.subject=subject;}
	public void setMessage(String message){this.message=message;}
	public void addPublicKey(PublicKey key){this.publicKey=key;}
	public void signWith(PrivateKey key){this.signature=key;}
	
	public String getReadyMail() throws EncryptionException, InvalidKeyException{
		JSONObject mail = new JSONObject();
		String strSubj = subject;
		String strMess = message;
		//TODO: addition of date. Just not sure if the date is "sensitive data" and should be encrypted
		//TODO: Adding a random number and the encrypted version (short token to try to decrypt to see if a message was intended for you)
		BASE64Encoder b64 = new BASE64Encoder();
		try{
			final Cipher cipher = Cipher.getInstance("RSA");
			if(publicKey != null){
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				strMess=b64.encode(cipher.doFinal(message.getBytes()));
				strSubj=b64.encode(cipher.doFinal(subject.getBytes()));
			}
			if(signature != null){
				byte[] digest = Util.shaCreate(message);
				cipher.init(Cipher.ENCRYPT_MODE, signature);
				mail.put("signature", b64.encode(cipher.doFinal(digest)));
			}
		}catch(IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e){
			throw new EncryptionException(e);
		}
		mail.put("subject", strSubj);
		mail.put("message", strMess);
		return mail.toJSONString();
	}
}
