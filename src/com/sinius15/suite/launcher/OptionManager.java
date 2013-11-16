package com.sinius15.suite.launcher;

import java.io.File;
import java.util.ArrayList;

import com.sinius15.suite.launcher.io.YAMLFile;

@SuppressWarnings("rawtypes")
public class OptionManager {

	private static ArrayList<Option> options = new ArrayList<Option>();
	
	public static void addOption(Option e){
		options.add(e);
	}
	
	public static void saveOptions(File f) throws Exception{
		YAMLFile data = new YAMLFile(true);
		for(Option p : options){
			if(p.getType().equals("int"))
				data.addInt(p.key, (Integer)p.value);
			if(p.getType().equals("boolean"))
				data.addboolean(p.key, (Boolean)p.value);
			if(p.getType().equals("String"))
				data.addString(p.key, (String)p.value);
		}
		data.Save(f);
	}
	
	public static void saveDefaultOptions(File f) throws Exception{
		YAMLFile data = new YAMLFile(true);
		for(Option p : options){
			if(p.getType().equals("int"))
				data.addInt(p.key, (Integer)p.defaultValue);
			if(p.getType().equals("boolean"))
				data.addboolean(p.key, (Boolean)p.defaultValue);
			if(p.getType().equals("String"))
				data.addString(p.key, (String)p.defaultValue);
		}
		data.Save(f);
	}
	
	@SuppressWarnings("unchecked")
	public static void loadOptions(File f) throws Exception{
		if(!f.exists()){
			f.createNewFile();
			saveDefaultOptions(f);
		}
			
		YAMLFile data = new YAMLFile(true);
		data.Load(f);
		for(Option p : options){
			if(p.getType().equals("int"))
				p.value = data.getInt(p.key);
			if(p.getType().equals("boolean"))
				p.value = data.getboolean(p.key);
			if(p.getType().equals("String"))
				p.value = data.getString(p.key);
		}
	}
	
	public static void updateValuesToManager(){
		for(Option p : options){
			p.setValue.run();
		}
	}
	
	public static void updateValuesToFrame(){
		for(Option p : options)
			p.setValueInFrame.run();
	}
	
	public static Object getValue(String key){
		for(Option p : options)
			if(p.key.equals(key))
				return p.value;
				
		return null;
	}
	
	
	
}
