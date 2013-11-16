package com.sinius15.suite.launcher.games;

import com.sinius15.suite.launcher.Data;
import com.sinius15.suite.launcher.OptionManager;

public class Maze extends Game{

	@Override
	public String getName() {
		return "maze";
	}

	@Override
	public String getJarName(){
		return "game.jar";
	}
	
	@Override
	public String getArguments() {
		if((Boolean)OptionManager.getValue("defaultDataFolder"))
			return "\"" + Data.DEFAULT_DATA_FOLDER.getAbsolutePath() + "\\maze\"";
		else
			return "\"" + (String) OptionManager.getValue("dataFolder") + "\\maze\"";
	}
	
	@Override
	public int getVersionLength() {
		return 7;
	}
	
}
