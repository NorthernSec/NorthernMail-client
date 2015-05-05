package com.NorthernSec.NorthernMail.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;

public class KeyManagement {
	
	private ConfigReader conf;
	
	public KeyManagement(){
		conf=new ConfigReader();
	}
	
	public void generateKeypair(int size, String name, String password) throws EncryptionException, IOException{
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
			final Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec sks = new SecretKeySpec(shaCreate(password), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secKey);
			ObjectOutputStream oospub = new ObjectOutputStream(new FileOutputStream(pubKey));
			CipherOutputStream cospriv = new CipherOutputStream (new FileOutputStream(privKey), sks);
			
			oospub.writeObject(keys.getPublic());
			cospriv.writeObject(keys.getPrivate());
			oospub.close();
			cospriv.close();
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e);
		}
	}
	public void getPublicKey(String key){
		File pubKey = new File(conf.getKeyLocation()+key+"-public.key");
		
	}
	
	private static byte[] shaKeyCreate(String pass){
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
