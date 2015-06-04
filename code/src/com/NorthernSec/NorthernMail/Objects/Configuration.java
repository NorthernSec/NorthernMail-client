package com.NorthernSec.NorthernMail.Objects;

import java.io.Serializable;

import com.NorthernSec.NorthernMail.client.KeyManager;

public class Configuration implements Serializable{
	private static final long serialVersionUID = 1L;
	private KeyManager keyMan;
	
	public Configuration(){
		keyMan = new KeyManager();
	}
	
	public KeyManager getKeyManager(){return keyMan;}
	
	
}
