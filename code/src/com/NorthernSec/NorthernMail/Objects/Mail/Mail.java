package com.NorthernSec.NorthernMail.Objects.Mail;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.NorthernSec.NorthernMail.Exceptions.InvalidMailException;
import com.NorthernSec.NorthernMail.Exceptions.PrivKeyEncryptedException;
import com.NorthernSec.NorthernMail.Objects.PrivKey;
import com.NorthernSec.NorthernMail.Objects.PubKey;
import com.NorthernSec.NorthernMail.client.KeyManager;
import com.NorthernSec.NorthernMail.Objects.Util;
import com.NorthernSec.NorthernMail.Objects.Mail.DecryptedMail;

public class Mail {
	private static String unlocked = "unlocked";
	private String subject;
	private String message;
	private String signature;
	private String token;
	
	public Mail(String subj, String mess, String sign, String token) throws InvalidMailException{
		if(subj==null || mess==null || token==null){throw new InvalidMailException("Bad mailing format");}
		if(!token.contains("::")){throw new InvalidMailException("Corrupted token");}
		subject=subj;message=mess;signature=sign;this.token=token;
	}
	
	private PrivKey isDecryptableWith(KeyManager k){
		try{
			final Cipher cipher = Cipher.getInstance("RSA");
			if(token.equals(unlocked)){return null;}
			String salt = token.split("::")[0];
			String tempToken = token.split("::")[1];
			PrivKey[] keys = k.getPrivKeys();
		
			for(PrivKey key:keys){
				//TODO Either don't encrypt private keys (decrypt config instead), or implement this here
				try{
					cipher.init(Cipher.DECRYPT_MODE, key.getKey());
					String t = new String(cipher.doFinal(tempToken.getBytes()));
					if (t.equals(salt+unlocked)){
						return key;}
				}catch(InvalidKeyException | PrivKeyEncryptedException | IllegalBlockSizeException | BadPaddingException e){}}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {}
			return null;}
	
	public Boolean isDecryptable(KeyManager k){
		if(token.equals(unlocked)){return true;}
		if(isDecryptableWith(k)==null){return false;}else{return true;}
	}
	public Boolean isSigned(){if(signature==null){return false;}else{return true;}};
	public DecryptedMail decrypt(KeyManager k){
		try{
			final Cipher cipher = Cipher.getInstance("RSA");
			PubKey sign = null;
			String mess = message;
			if(!token.toLowerCase().equals(unlocked)){
				//Decrypt the mail
				String salt = token.split("::")[0];
				String tempToken = token.split("::")[1];
				PrivKey[] keys = k.getPrivKeys();
				PubKey[] signKeys = k.getPubKeys();
				Boolean decrypted = false;

				PrivKey masterKey = isDecryptableWith(k);
				if(masterKey==null){return null;}
				try{
					cipher.init(Cipher.DECRYPT_MODE, masterKey.getKey());
					mess = new String(cipher.doFinal(message.getBytes()));
				}catch(InvalidKeyException | PrivKeyEncryptedException | IllegalBlockSizeException | BadPaddingException e){}
				for (PubKey key:signKeys){
					try {
						cipher.init(Cipher.DECRYPT_MODE, key.getKey());
						String s = new String(cipher.doFinal(signature.getBytes()));
						if(s.equals(Util.shaCreate(mess))){sign=key;
							break;}
					} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {}}}
			return new DecryptedMail(subject,message,sign,null,false);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			return null;
		}
	}
	
}
