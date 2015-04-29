/**
 * NorthernMail Java Client
 *
 * -- description of class --
 * 
 * Copyright (c) 2015	NorthernSec
 * Copyright (c) 2015	Pieter-Jan Moreels
 * This software is licensed under the Original BSD License
 */
package com.NorthernSec.NorthernMail.connection;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConnectionHandler {
  private Socket socClient;
  private DataInputStream disIn;
  private DataOutputStream dosOut;
  
  private String VERSION="NorthernMail Client v0.0 beta";
  
  private void sendData(String data)throws IOException{
	//Prepend the length of the message to the byte array
	byte[] length=ByteBuffer.allocate(4).putInt(data.length()).array();
	ByteArrayOutputStream baos=new ByteArrayOutputStream();
	baos.write(length);
	baos.write(data.getBytes());
	dosOut.write(baos.toByteArray());
  }
  public ConnectionHandler(){
	  
  }
  private String fetchMail(){
	String mails = "";
	try{
	  sendData("Fetch::"+VERSION);
	  byte[] blen = new byte[4];
	  disIn.read(blen);
	  System.out.println(blen[0]&0xff);
	  int len = ByteBuffer.wrap(blen).order(ByteOrder.LITTLE_ENDIAN).getInt();
	  System.out.println(len);
	  byte[] returndata = new byte[len];
	  disIn.readFully(returndata);
	  return new String(returndata);
	}catch(Exception e){
      System.out.println(e);
		//TODO raise not connected except
	}
	return mails;
  }
  private void initiationsequence(){
	
  }
  public void connect(String host, int port){
    String strData;
	try{
	  socClient = new Socket(host,port);
      disIn = new DataInputStream(socClient.getInputStream());
      dosOut = new DataOutputStream(socClient.getOutputStream());
      String mail = fetchMail();
      System.out.println(mail);
      //TODO connection sequence
      //while(true){
      //	if ((strData=disIn.read)!=null){
      //    System.out.println(strData);
      //	}
      	
      //}
	}catch(Exception e){
	  System.out.println(e);
	}
  }
  /**
   * @param args
   */
  public static void main(String[] args) {
	// TODO Auto-generated method stub
	ConnectionHandler c = new ConnectionHandler();
	c.connect("localhost", 5002);
  }

}
