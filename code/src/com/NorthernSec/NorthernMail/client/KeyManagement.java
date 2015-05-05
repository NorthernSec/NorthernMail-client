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
	
	public void generateKeypair(int size, String name) throws EncryptionException, IOException{
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
			ObjectOutputStream oospub = new ObjectOutputStream(new FileOutputStream(pubKey));
			ObjectOutputStream oospriv = new ObjectOutputStream(new FileOutputStream(privKey));
			oospub.writeObject(keys.getPublic());
			oospriv.writeObject(keys.getPrivate());
			oospub.close();
			oospriv.close();
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException(e);
		}
	}
	public void getPublicKey(String key){
		File pubKey = new File(conf.getKeyLocation()+key+"-public.key");
		
	}
}
