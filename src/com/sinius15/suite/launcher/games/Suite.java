package com.sinius15.suite.launcher.games;

import com.sinius15.suite.launcher.Data;
import com.sinius15.suite.launcher.OptionManager;

public class Suite extends Game {

	@Override
	public String getName() {
		return "suite";
	}

	@Override
	public String getJarName(){
		return "game.jar";
	}
	
	@Override
	public String getArguments() {
		String args = 
				"screenW=>" + OptionManager.getValue("screenWidth") + 
				" screenH=>" + OptionManager.getValue("screenHeight");
		
		if((Boolean)OptionManager.getValue("defaultDataFolder"))
			args = args + " \"dataFolder=>" + Data.DEFAULT_DATA_FOLDER.getAbsolutePath() + "\\suite\"";
		else
			args = args + " \"dataFolder=>" + (String) OptionManager.getValue("dataFolder") + "\\suite\"";

		return args;
	}
	
	@Override
	public int getVersionLength() {
		return 6;
	}

}
