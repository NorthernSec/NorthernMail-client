package com.NorthernSec.NorthernMail.client;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

import com.NorthernSec.NorthernMail.Exceptions.AlreadyExistsException;
import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;
import com.NorthernSec.NorthernMail.Objects.PrivKey;
import com.NorthernSec.NorthernMail.Objects.PubKey;
import com.NorthernSec.NorthernMail.Objects.Util;

public class KeyManager implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<PubKey> publicKeys;
	private ArrayList<PrivKey> privateKeys;
	
	public KeyManager(){
		publicKeys=new ArrayList<PubKey>();privateKeys=new ArrayList<PrivKey>();}
	
	public PubKey getPubKey(String name){
		for(PubKey key:publicKeys){
			if(key.getName().equals(name)){return key;}
		}return null;}
	public PubKey[] getPubKeys(){return publicKeys.toArray(new PubKey[publicKeys.size()]);}
	public void addPubKey(PubKey key) throws AlreadyExistsException{
		if (getPubKey(key.getName())!=null){
			throw new AlreadyExistsException("A key with this name already exists");}
		publicKeys.add(key);}
	public void removePubKey(String name){
		for(PubKey key:publicKeys){
			if(key.getName().equals(name)){publicKeys.remove(key);}}}
	
	public PrivKey getPrivKey(String name){
		for(PrivKey key:privateKeys){
			if(key.getName().equals(name)){return key;}}
		return null;}
	public PrivKey[] getPrivKeys(){return privateKeys.toArray(new PrivKey[privateKeys.size()]);}
	public void addPrivKey(PrivKey key) throws AlreadyExistsException{
		if (getPrivKey(key.getName())!=null){
			throw new AlreadyExistsException("A key with this name already exists");}
		privateKeys.add(key);}
	public void removePrivKey(String name){
		Iterator<PrivKey> it = privateKeys.iterator();
		while(it.hasNext()){
			PrivKey key = it.next();
			if(key.getName().equals(name)){it.remove();}}}

	
	public void generateKeypair(int size, String name, String password) throws EncryptionException, IOException, InvalidKeyException, AlreadyExistsException{
		try {
			//Generate keys
			final KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			keygen.initialize(size);
			final KeyPair keys = keygen.generateKeyPair();
			if (getPrivKey(name)!=null){throw new AlreadyExistsException("A key with this name already exists");}
			if (getPubKey(name)!=null){throw new AlreadyExistsException("A key with this name already exists");}
			PubKey pubKey = new PubKey(name,keys.getPublic());
			PrivKey privKey;
			if(password != null && password.length()>0){
				final Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
				final SecretKeySpec sks = new SecretKeySpec(Util.shaCreate(password), "AES");
				cipher.init(Cipher.ENCRYPT_MODE, sks);
				privKey = new PrivKey(name, new SealedObject(keys.getPrivate(), cipher));
			}else{
				privKey = new PrivKey(name,keys.getPrivate());}
			addPubKey(pubKey);
			addPrivKey(privKey);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException e) {
			throw new EncryptionException(e);}}
	
}
