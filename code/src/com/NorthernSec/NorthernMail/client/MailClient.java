package com.NorthernSec.NorthernMail.client;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;
import com.NorthernSec.NorthernMail.connection.ConnectionHandler;

public class MailClient {
  private ConnectionHandler con;

  private String VERSION="NorthernMail Client v0.0 beta";

  public MailClient(){
	con=new ConnectionHandler();
  }
  
  public void connect(String host, int port) throws UnknownHostException, IOException{
	  con.connect(host, port);
  }
  
  public void close() throws IOException{
	  con.close();
  }
  
  public String fetchMail() throws InvalidKeyException, IOException, EncryptionException{
	  con.send(("Fetch::"+VERSION).getBytes());
	  return new String(con.receive());
  }
  
  public void sendMail(String mail,PublicKey key, Boolean sign) throws EncryptionException, InvalidKeyException, IOException{
	  try {
		final Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		String data = "";
		con.aesSend(cipher.doFinal(data.getBytes()));
		//TODO: complete encryption
	} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
		throw new EncryptionException(e);
	}
  }
  
  public void generateKeypair(int size, String name) throws EncryptionException, IOException{
	  try {
		//Generate keys
		final KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		keygen.initialize(size);
		final KeyPair keys = keygen.generateKeyPair();
		//TODO get file location from config
		File privKey=new File(name+"-private.key");
		File pubKey=new File(name+"-public.key");
		//Create parent dirs
		if (privKey.getParentFile() != null){privKey.getParentFile().mkdir();}
		if (pubKey.getParentFile() != null){pubKey.getParentFile().mkdir();}
		//Create files
		privKey.createNewFile();
		pubKey.createNewFile();
	} catch (NoSuchAlgorithmException e) {
		throw new EncryptionException(e);
	}
  }
  
  public static void main(String[] args){
	  try{
		  MailClient mc = new MailClient();
		  mc.connect("127.0.0.1", 5002);
		  System.out.println(mc.fetchMail());
	  } catch (Exception e){
		  e.printStackTrace();
	  }
  }
}
