package com.sinius15.suite.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.sinius15.suite.launcher.games.Chess;
import com.sinius15.suite.launcher.games.Game;
import com.sinius15.suite.launcher.games.Maze;
import com.sinius15.suite.launcher.games.Suite;

public class Launcher {

	public static Suite suite = new Suite();
	public static Maze maze = new Maze();
	public static Chess chess = new Chess();
	
	public static void main(String[] args) {
		Data.launcherFrame.setVisible(true);
		initOnlineStuff();
		Data.initOptions();
		try {
			Data.DEFAULT_DATA_FOLDER.mkdirs();
			OptionManager.loadOptions(Data.CONFIG_FILE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if((Boolean)OptionManager.getValue("userCredentials")){
			Data.launcherFrame.txtUsername.setText((String) OptionManager.getValue("username"));
			Data.launcherFrame.textFieldHadFocus = true;
			Data.launcherFrame.passwordField.setEchoChar((char)9679);
			Data.launcherFrame.passwordField.setText((String) OptionManager.getValue("password"));
			Data.launcherFrame.passwordFieldHadFocus = true;
		}
	}
	
	public static void initOnlineStuff(){			//init all the games;
		Thread internetThread = new Thread(new Runnable() {@Override public void run() {
			Data.launcherFrame.btnPlay.setEnabled(false);
			suite.init();
			maze.init();
			chess.init();
			if(suite.versionList != null)
				Data.launcherFrame.btnPlay.setEnabled(true);
			
		}}, "getInternetData");
		internetThread.start();
	}
	
	public static Game getSelectedGame(){
		if(Data.launcherFrame.gameBox.getSelectedItem().equals(suite.getName()))
			return suite;
		if(Data.launcherFrame.gameBox.getSelectedItem().equals(maze.getName()))
			return maze;
		if(Data.launcherFrame.gameBox.getSelectedItem().equals(chess.getName()))
			return chess;
		return null;
	}
	
	public static void saveGame(String name){
		getSelectedGame().saveConfig();
	}
	
	public static void updateSelectedGame() {
		getSelectedGame().update();
	}
	
	public static void startJar(String file, String arguments){
		if(arguments != null)
			System.out.println("The game arguments are: " + arguments);
		
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("java -jar \"" + file + "\" " + arguments);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		if((Integer)OptionManager.getValue("launcherVisability") == Data.LAUNCHVIS_CLOSE)
			System.exit(0);
		if((Integer)OptionManager.getValue("launcherVisability") == Data.LAUNCHVIS_REOPEN)
			Data.launcherFrame.setVisible(false);
		
		//this is for the reading of errors
		final InputStream err = p.getErrorStream();
		Thread errThread = new Thread(new Runnable() {@Override public void run() {
			try {
				BufferedReader errReader = new BufferedReader(new InputStreamReader(err));
				String line = null;
				while ((line = errReader.readLine()) != null) 
					System.err.println(line);
				errReader.close();
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}}, "errThread");
		errThread.start();
		
		//this is for the reading of normal output
		InputStream in = p.getInputStream();
		try {
			BufferedReader txtReader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = txtReader.readLine()) != null) 
				System.out.println("[game] "+line);
			txtReader.close();
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		while(true)
			if(!errThread.isAlive())
				break;
		try {
			in.close();
			err.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

}
