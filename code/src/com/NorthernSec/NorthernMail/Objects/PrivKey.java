package com.NorthernSec.NorthernMail.Objects;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;
import com.NorthernSec.NorthernMail.Exceptions.PrivKeyEncryptedException;

public class PrivKey {
	private String name;
	private Object key;
	private String comments;
	private Boolean encrypted;
	
	public PrivKey(String name, PrivateKey key){
		this.name=name;
		this.key=key;
		this.encrypted=false;
	}
	public PrivKey(String name, SealedObject key){
		this.name=name;
		this.key=key;
		this.encrypted=true;
	}
	public PrivKey(String name, PrivateKey key, String comments){
		this.name=name;
		this.key=key;
		this.comments=comments;
		this.encrypted=false;
	}
	public PrivKey(String name, SealedObject key, String comments){
		this.name=name;
		this.key=key;
		this.comments=comments;
		this.encrypted=true;
	}
	
	public String getName(){return name;}
	public PrivateKey getKey() throws PrivKeyEncryptedException{
		if(encrypted){
			throw new PrivKeyEncryptedException("This key is encrypted with a key");
		}
		return (PrivateKey) key;
	}
	public PrivateKey getKey(String password) throws EncryptionException, InvalidKeyException, ClassNotFoundException, IOException{
		try {
			final SecretKeySpec sks = new SecretKeySpec(Util.shaCreate(password), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, sks);
			return (PrivateKey)((SealedObject)key).getObject(cipher);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException e) {
			throw new EncryptionException(e);
		}
	}
	public String getComments(){return comments;}
	public Boolean isEncrypted(){return encrypted;}
	
	public void setComments(String comments){this.comments=comments;}

}
