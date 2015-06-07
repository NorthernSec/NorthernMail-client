package com.NorthernSec.NorthernMail.Objects;

import java.io.Serializable;

import com.NorthernSec.NorthernMail.client.KeyManager;

public class Configuration implements Serializable{
	private static final long serialVersionUID = 1L;
	private KeyManager keyMan;
	private String host;
	private int port;
	
	public Configuration(){
		keyMan = new KeyManager();
	}
	
	public void setHost(String host){this.host=host;}
	public void setPort(int port){this.port=port;}
	
	public String getHost(){return host;}
	public int getPort(){return port;}
	public KeyManager getKeyManager(){return keyMan;}
	
	
}
