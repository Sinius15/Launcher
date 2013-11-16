package com.sinius15.suite.launcher.games;

import java.io.File;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;

import com.sinius15.suite.launcher.Data;
import com.sinius15.suite.launcher.io.Downloader;
import com.sinius15.suite.launcher.io.YAMLFile;

public abstract class Game {

	public String getName(){ return null; };
	public String getArguments(){return null;};
	public String getJarName(){return null;};
	public String latestVersion = "";
	public String[] versionList = {""};
	public static boolean online = true;
	
	private File configFile = new File(Data.DEFAULT_DATA_FOLDER + "\\" + getName() + "\\" + "launcherConfig.yml");
	public int getVersionLength(){return 0;}
	
	public void update(){
		if(online)
			Data.launcherFrame.versionCombo.setModel(new DefaultComboBoxModel<String>(versionList));
		else
			Data.launcherFrame.versionCombo.setModel(new DefaultComboBoxModel<String>(new String[]{}));
		File fi = new File(Data.DEFAULT_DATA_FOLDER + "\\" + getName());
		fi.mkdirs();
		if(!configFile.exists()){
			try {
				configFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			YAMLFile dat = new YAMLFile(true);
			dat.addboolean("autoUpdate", true);
			dat.addString("version", "-");
			try {
				dat.Save(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		YAMLFile dat = new YAMLFile(true);
		dat.Load(configFile);
		Data.launcherFrame.autoUpdate.setSelected(dat.getboolean("autoUpdate"));
		Data.launcherFrame.versionCombo.setEnabled(!dat.getboolean("autoUpdate"));
		
		if(!online)
			Data.launcherFrame.versionCombo.addItem(dat.getString("version"));
		Data.launcherFrame.versionCombo.setSelectedItem(dat.getString("version"));
	}
	
	public void saveConfig(){
		
		YAMLFile dat = new YAMLFile(true);
		dat.addboolean("autoUpdate", Data.launcherFrame.autoUpdate.isSelected());
		
//		if(Data.launcherFrame.autoUpdate.isSelected())
//			dat.addString("version", "-");
//		else{
			dat.addString("version", (String) Data.launcherFrame.versionCombo.getSelectedItem());
//		}
		try {
			dat.Save(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init(){
		System.out.println("Loading the game " + getName());
		try {
			latestVersion = Downloader.getLatestVersion(System.out, this);
			versionList = Downloader.getVersionList(System.out, this);
			versionList[versionList.length-1] = versionList[versionList.length-1].substring(0, getVersionLength());
			latestVersion = latestVersion.substring(0, getVersionLength());
		} catch (IOException e) {
			latestVersion = null;
			versionList = null;
			online = false;
		}
		
		Data.launcherFrame.gameBox.addItem(getName());
		Data.launcherFrame.gameBox.setSelectedItem(getName());
		
		update();
		
		System.out.println("Done loading the game " + getName());
	};
	
}
