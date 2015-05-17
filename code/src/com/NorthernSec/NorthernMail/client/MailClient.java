package com.NorthernSec.NorthernMail.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;
import com.NorthernSec.NorthernMail.Exceptions.InvalidMailException;
import com.NorthernSec.NorthernMail.Objects.Configuration;
import com.NorthernSec.NorthernMail.Objects.Mail.Mail;
import com.NorthernSec.NorthernMail.Objects.Mail.MailTemplate;
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
  
  public Mail[] fetchMail() throws IOException, ParseException{
	  JSONObject j = new JSONObject();
	  j.put("command","FETCH");
	  j.put("version", VERSION);
	  con.send((j.toJSONString()).getBytes());
	  
	  JSONObject jResult = (JSONObject)new JSONParser().parse(new String(con.receive()));
	  JSONArray mails = (JSONArray)jResult.get("mails");
	  ArrayList<Mail> myMails = new ArrayList<Mail>();
	  for(int i=0;i<mails.size();i++){
		  try{
			  JSONObject mail = (JSONObject)mails.get(i);
			  Mail m = new Mail((String)mail.get("subject"),(String)mail.get("message"),(String)mail.get("signature"),(String)mail.get("token"));
			  if(m.isDecryptable(keyMan)){
				  myMails.add(m);}
		  }catch (InvalidMailException e){}}
	  return (Mail[])myMails.toArray();
	  //return new String(con.receive());
  }
    
  public void sendMail(MailTemplate mail) throws IOException, InvalidKeyException, EncryptionException, InvalidMailException{
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
		  Configuration c = new Configuration();
		  c.getKeyManager().generateKeypair(2048, "DummyKeyPair1", null);
		  c.getKeyManager().generateKeypair(1337, "DummyKeyPair2", null);
		  c.getKeyManager().generateKeypair(2048, "alice", null);
		  c.getKeyManager().removePrivKey("alice");
		  c.getKeyManager().generateKeypair(2048, "bob", null);
		  c.getKeyManager().removePrivKey("bob");
		  c.save();
		  // MailClient mc = new MailClient();
		  //mc.connect("127.0.0.1", 5002);
		  //Thread.sleep(1000);
		  //System.out.println("Fetching mails:");
		  //System.out.println(mc.fetchMail());
		  //keyMan.generateKeypair(4096, "testkey");
		  //MailTemplate m = new MailTemplate();
		  //m.setSubject("Hello world!");
		  //m.setMessage("dummy test message");
		  //System.out.println("Sending mail:");
		  //System.out.println(m.getReadyMail());
		  //mc.sendMail(m);
		  //mc.close();
	  } catch (Exception e){
		  e.printStackTrace();
	  }
  }
}
