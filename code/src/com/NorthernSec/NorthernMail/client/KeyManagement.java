package com.NorthernSec.NorthernMail.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;

public class KeyManagement {
	
	private ConfigReader conf;
	
	public KeyManagement(){
		conf=new ConfigReader();
	}
	
	public void generateKeypair(int size, String name, String password) throws EncryptionException, IOException, InvalidKeyException{
		try {
			//Generate keys
			final KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			keygen.initialize(size);
			final KeyPair keys = keygen.generateKeyPair();
			final String keyLocation=conf.getKeyLocation();
			File privKey=new File(keyLocation+name+"-private.key");
			File pubKey=new File(keyLocation+name+"-public.key");
			//Create parent dirs
			if (privKey.getParentFile() != null){privKey.getParentFile().mkdir();}
			if (pubKey.getParentFile() != null){pubKey.getParentFile().mkdir();}
			//Create files
			privKey.createNewFile();
			pubKey.createNewFile();
			Object objPriv = null;
			if(password.length()>0 && password != null){
				final Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
				final SecretKeySpec sks = new SecretKeySpec(shaCreate(password), "AES");
				cipher.init(Cipher.ENCRYPT_MODE, sks);
				SealedObject soPriv = new SealedObject(keys.getPrivate(), cipher);
			}else{
				objPriv = keys.getPrivate();
			}
			ObjectOutputStream oospub = new ObjectOutputStream(new FileOutputStream(pubKey));
			ObjectOutputStream oospriv = new ObjectOutputStream(new FileOutputStream(privKey));
			oospub.writeObject(keys.getPublic());
			oospriv.writeObject(objPriv);
			oospub.close();
			oospriv.close();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException e) {
			throw new EncryptionException(e);
		}
	}
	public PublicKey getPublicKey(String key) throws FileNotFoundException, IOException, ClassNotFoundException{
		File pubKey = new File(conf.getKeyLocation()+key+"-public.key");
		ObjectInputStream oisPriv = new ObjectInputStream(new FileInputStream(pubKey));
		return (PublicKey)oisPriv.readObject();
	}
	public PrivateKey getPrivKey(String name, String password) throws InvalidKeyException, FileNotFoundException, IOException, ClassNotFoundException, EncryptionException{
		File privKey = new File(conf.getKeyLocation()+name+"-private.key");
		PrivateKey key = null;
		if(password.length()>0 && password != null){
			try {
				final SecretKeySpec sks = new SecretKeySpec(shaCreate(password), "AES");
				Cipher cipher;
				cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, sks);
				ObjectInputStream oisPriv = new ObjectInputStream(new FileInputStream(privKey));
				SealedObject soKey = (SealedObject)(oisPriv.readObject());
				key = (PrivateKey)soKey.getObject(cipher);
			} catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException e) {
				throw new EncryptionException(e);
			}
		}else{
			ObjectInputStream oisPriv = new ObjectInputStream(new FileInputStream(privKey));
			key=(PrivateKey)oisPriv.readObject();
		}
		return key;
	}
	
	private static byte[] shaCreate(String pass){
        try{
		String key = pass;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(key.getBytes("UTF-8"));
		byte[] longdigest = md.digest();
		byte[] digest = new byte[16];
		System.arraycopy(longdigest, 0, digest, 0, 16);
		return digest;
        }catch(NoSuchAlgorithmException | UnsupportedEncodingException e){return null;}
    }
}
