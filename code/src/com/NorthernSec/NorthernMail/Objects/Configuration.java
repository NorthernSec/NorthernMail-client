package com.NorthernSec.NorthernMail.Objects;

import javax.crypto.SealedObject;

import com.NorthernSec.NorthernMail.client.KeyManager;

public class Configuration {
	private KeyManager keyMan;
	
	public Configuration(){
		keyMan = new KeyManager();
	}
	
	public KeyManager getKeyManager(){return keyMan;}
}
