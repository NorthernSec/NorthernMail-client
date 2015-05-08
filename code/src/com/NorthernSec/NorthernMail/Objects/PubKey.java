package com.NorthernSec.NorthernMail.Objects;

import java.security.PublicKey;

public class PubKey {
	private String name;
	private PublicKey key;
	private String comments;
	
	public PubKey(String name, PublicKey key){
		this.name=name;
		this.key=key;
	}
	public PubKey(String name, PublicKey key, String comments){
		this.name=name;
		this.key=key;
		this.comments=comments;
	}
	
	public String getName(){return name;}
	public PublicKey getKey(){return key;}
	public String getComments(){return comments;}
	
	public void setComments(String comments){this.comments=comments;}
}
