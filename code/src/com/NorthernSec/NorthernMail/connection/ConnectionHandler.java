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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;

public class ConnectionHandler {
  private Socket socClient;
  private DataInputStream disIn;
  private DataOutputStream dosOut;
  private Cipher cipAES;
  private SecretKey keyAES;
  
  
  public void aesSend(byte[] data) throws InvalidKeyException, IOException, EncryptionException{
	byte[] iv = new byte[32];
	new Random().nextBytes(iv);
	try {
      cipAES.init(Cipher.ENCRYPT_MODE, keyAES, new IvParameterSpec(iv));
      byte[] encrypted = cipAES.doFinal(data);
      ByteArrayOutputStream baos=new ByteArrayOutputStream();
      //Prepend iv to data
      baos.write(iv);
      baos.write(encrypted);
      send(baos.toByteArray());
    } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
      throw new EncryptionException(e);
	}
  }
  public void send(byte[] data)throws IOException{
	//split message in case it is too big
	//Prepend the length of the message to the byte array
	byte[] length=ByteBuffer.allocate(4).putInt(data.length).array();
	ByteArrayOutputStream baos=new ByteArrayOutputStream();
	baos.write(length);
	baos.write(data);
	dosOut.write(baos.toByteArray());
  }
  
  public byte[] receive() throws IOException{
	byte[] blen = new byte[4];
	disIn.read(blen);
	int len = ByteBuffer.wrap(blen).order(ByteOrder.LITTLE_ENDIAN).getInt();
	byte[] returndata = new byte[len];
    disIn.readFully(returndata);
    return returndata;
  }
  private void initiationsequence() throws EncryptionException{
	KeyGenerator keyGen;
	try {
		keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256);
	    cipAES = Cipher.getInstance("AES");
	    keyAES = keyGen.generateKey();
	} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new EncryptionException("Could not generate temporary AES key");
	}
  }
  public void connect(String host, int port) throws UnknownHostException, IOException{
    socClient = new Socket(host,port);
    disIn = new DataInputStream(socClient.getInputStream());
    dosOut = new DataOutputStream(socClient.getOutputStream());
    //initiationsequence();
    //System.out.println(keyAES);
    //send("INIT::".getBytes());
  }
  public void close() throws IOException{
	  dosOut.close();
	  disIn.close();
	  socClient.close();
  }

}
