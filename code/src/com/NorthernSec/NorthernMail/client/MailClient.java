package com.NorthernSec.NorthernMail.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.JSONObject;

import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;
import com.NorthernSec.NorthernMail.Objects.Mail;
import com.NorthernSec.NorthernMail.connection.ConnectionHandler;

public class MailClient {
  private ConnectionHandler con;
  private KeyManager keyMan;

  final private String VERSION="NorthernMail Client v0.0 beta";

  public MailClient(){
	con=new ConnectionHandler();
	keyMan=new KeyManager();
  }
  
  public void connect(String host, int port) throws UnknownHostException, IOException{
	  con.connect(host, port);
  }
  
  public void close() throws IOException{
	  con.close();
  }
  
  public String fetchMail() throws InvalidKeyException, IOException, EncryptionException{
	  con.send(("FETCH::"+VERSION).getBytes());
	  return new String(con.receive());
  }
    
  public void sendMail(Mail mail) throws IOException, InvalidKeyException, EncryptionException{
	  JSONObject j = new JSONObject();
	  j.put("command","POST");
	  j.put("version", VERSION);
	  j.put("mail", mail.getReadyMail());
	  con.send((j.toJSONString()).getBytes());
	  //TODO: replace send with con.aesSend(cipher.doFinal(data.getBytes()))
	  //TODO: complete encryption
  }
  
  public static void main(String[] args){
	  try{
		  MailClient mc = new MailClient();
		  mc.connect("127.0.0.1", 5002);
		  System.out.println(mc.fetchMail());
		  //keyMan.generateKeypair(4096, "testkey");
		  mc.sendMail("some test mail", "testkey", true, "privKey");
		  mc.close();
	  } catch (Exception e){
		  e.printStackTrace();
	  }
  }
}
