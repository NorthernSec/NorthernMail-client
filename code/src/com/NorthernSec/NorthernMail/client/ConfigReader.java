package com.NorthernSec.NorthernMail.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.NorthernSec.NorthernMail.Objects.Configuration;

public class ConfigReader {
	private Configuration conf;
	private File path;
	
	public String getPath(){return path.getAbsolutePath();}
	public Configuration getConf(){return conf;}
	public void create(String path){this.path=new File(path);conf=new Configuration();}
	public void load(String path) throws FileNotFoundException, IOException, ClassNotFoundException{
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))){
			conf = (Configuration)in.readObject();
			this.path=new File(path);
		}
	}
	public void save() throws IOException{
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))){
			if(!path.exists()){
				path.createNewFile();}
			out.writeObject(conf);
		}
	}
}
