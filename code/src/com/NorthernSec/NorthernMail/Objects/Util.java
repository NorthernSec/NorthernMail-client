package com.NorthernSec.NorthernMail.Objects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Util {

	public static byte[] shaCreate(String pass){
        try{
		String key = pass;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(key.getBytes("UTF-8"));
		byte[] longdigest = md.digest();
		//byte[] digest = new byte[16];
		//System.arraycopy(longdigest, 0, digest, 0, 16);
		//return digest;
		return longdigest;
        }catch(NoSuchAlgorithmException | UnsupportedEncodingException e){return null;}
    }
	public static byte[] compress(byte[] b) throws IOException{
		Deflater def = new Deflater();
		def.setInput(b);
		ByteArrayOutputStream bao = new ByteArrayOutputStream(b.length);
		def.finish();
		byte[] buffer = new byte[1024];
		while(!def.finished()){
			int count = def.deflate(buffer);
			bao.write(buffer,0,count);
		}
		bao.close();
		return bao.toByteArray();
	}
	public static byte[] decompress(byte[] b) throws DataFormatException, IOException{
		Inflater inf = new Inflater();
		inf.setInput(b);
		ByteArrayOutputStream bao = new ByteArrayOutputStream(b.length);
		byte[] buffer = new byte[1024];
		while(!inf.finished()){
			int count = inf.inflate(buffer);
			bao.write(buffer,0,count);
		}
		bao.close();
		return bao.toByteArray();
	}
}
