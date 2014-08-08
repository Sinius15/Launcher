package com.sinius15.suite.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.sinius15.suite.launcher.games.Game;
import com.sinius15.suite.launcher.io.Downloader;

public class GameLauncher {

	private static Thread runThread, observer;
	
	public static void LaunchGame(final Game game, final boolean online){
		
		System.out.println("Starting the Suite game...");
		Data.launcherFrame.btnPlay.setEnabled(false);
		Data.launcherFrame.btnPlayOffline.setEnabled(false);
		runThread = new Thread(new Runnable() { @Override public void run() {
			
			String versionToStart = (String) Data.launcherFrame.versionCombo.getSelectedItem();
			String dataFolder;
			if((Boolean)OptionManager.getValue("defaultDataFolder"))
				dataFolder = Data.DEFAULT_DATA_FOLDER.getAbsolutePath() + "\\" + game.getName();
			else
				dataFolder = (String) OptionManager.getValue("dataFolder") + "\\" + game.getName();
			String localVersion = getLocalVersion(dataFolder);
			new File(dataFolder).mkdirs();
			
			try{
				if(Data.launcherFrame.autoUpdate.isSelected()){
					versionToStart = game.latestVersion;
				}
			}catch (Exception e) {
				e.printStackTrace();
				System.err.println("[Error] - Something went wrong while checking for latest version. Maybe there is no internet connection?");
				return;
			}
			if(localVersion == null && !online){
				JOptionPane.showMessageDialog(Data.launcherFrame, "The game has never downloaded any versions before, so it can't start the game offline.");
				return;
			}
			if(online && (localVersion == null || !localVersion.equals(versionToStart))){	//if need to download
				System.out.println("going to download verision " + versionToStart);
				if(!downloadReqFiles(dataFolder, "http://sinius15.com/Launcher/" + game.getName() + "/" + versionToStart + ".zip")){
					System.err.println("[Error]  -  Something went wrong while downloading the files. Maybe there is no internet connection?");
					return;
				}
				try {
					Downloader.unZip(dataFolder + "\\temp.zip", dataFolder, System.out);
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("[Error] - Something went wrong while unzipping a file. maybe you have no writing permission in your selected data folder?");
					return;
				}
			}
			
			Launcher.startJar(dataFolder + "\\" + game.getJarName(), game.getArguments());	//will return when game closes
				
		}}, "Game");
		runThread.start();
		observer = new Thread(new Runnable() {@Override public void run() {
			while(true)
				if(!runThread.isAlive())
					break;
			if((Integer)OptionManager.getValue("launcherVisability") == Data.LAUNCHVIS_REOPEN)
				Data.launcherFrame.setVisible(true);	
			System.out.println("Game stopped");	
			Data.launcherFrame.btnPlayOffline.setEnabled(true);
			Data.launcherFrame.btnPlay.setEnabled(true);
			
		}}, "Observer");
		observer.start();
		
	}
	
	public static String getLocalVersion(String dataFolder){
		File f = new File(dataFolder + "\\version.txt");
		if(!f.exists())
			return null;
		try {
			Scanner scan = new Scanner(f);
			String out = scan.next();
			scan.close();
			return out;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean downloadReqFiles(String folder, String address){
		try {
			URL url = new URL(address);
			File out = new File(folder + "\\temp.zip");
			Downloader.downloadFile(url, out, System.out);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
