package com.NorthernSec.NorthernMail.Objects.Mail;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.NorthernSec.NorthernMail.Exceptions.InvalidMailException;
import com.NorthernSec.NorthernMail.Exceptions.InvalidSignatureException;
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
		final BASE64Decoder b64 = new BASE64Decoder();
		try{
			final Cipher cipher = Cipher.getInstance("RSA");
			if(token.equals(unlocked)){return null;}
			String salt = token.split("::")[0];
			byte[] tempToken = b64.decodeBuffer((token.split("::")[1]));
			PrivKey[] keys = k.getPrivKeys();
		
			for(PrivKey key:keys){
				//TODO Either don't encrypt private keys (decrypt config instead), or implement this here
				try{
					cipher.init(Cipher.DECRYPT_MODE, key.getKey());
					String t = new String(Util.decompress(cipher.doFinal(tempToken)));
					if (t.equals(salt+unlocked)){
						return key;}
				}catch(InvalidKeyException | PrivKeyEncryptedException | IllegalBlockSizeException | BadPaddingException | DataFormatException e){
					System.out.println(e);
				}}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IOException e) { }
		return null;}
	
	public Boolean isDecryptable(KeyManager k){
		if(token.equals(unlocked)){return true;}
		if(isDecryptableWith(k)==null){return false;}else{return true;}
	}
	public Boolean isSigned(){if(signature==null){return false;}else{return true;}};
	public DecryptedMail decrypt(KeyManager k) throws InvalidMailException, InvalidSignatureException{
		try{
			final BASE64Decoder b64 = new BASE64Decoder();
			final Cipher cipher = Cipher.getInstance("RSA");
			PubKey sign = null;
			PrivKey recv = null;
			String mess = message;
			String subj = subject;
			Boolean encrypted = false;
			if(!token.toLowerCase().equals(unlocked)){
				encrypted=true;
				PrivKey[] keys = k.getPrivKeys();
				recv = isDecryptableWith(k);
				if(recv==null){throw new InvalidMailException("Can not decrypt mail with any private keys");}
				try{
					cipher.init(Cipher.DECRYPT_MODE, recv.getKey());
					mess = new String(Util.decompress(cipher.doFinal(b64.decodeBuffer(message))));
					subj = new String(Util.decompress(cipher.doFinal(b64.decodeBuffer(subject))));
				}catch(InvalidKeyException | PrivKeyEncryptedException | IllegalBlockSizeException | BadPaddingException | IOException | DataFormatException e) {
					throw new InvalidMailException("This mail is corrupted. This could indicate forgery or a corrupt database or client");}}
			if(isSigned()){
				PubKey[] signKeys = k.getPubKeys();
				for (PubKey key:signKeys){
					try {
						cipher.init(Cipher.DECRYPT_MODE, key.getKey());
						String s = new String(cipher.doFinal(b64.decodeBuffer(signature)));
						if(s.equals(Util.shaCreate(mess))){sign=key;
							break;}
					} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
						throw new InvalidSignatureException(new DecryptedMail(subj,mess,sign,recv,encrypted), e);
					}}}
			return new DecryptedMail(subj,mess,sign,recv,encrypted);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			return null;
		}
	}
	
}
