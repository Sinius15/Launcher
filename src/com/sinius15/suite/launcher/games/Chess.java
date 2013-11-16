package com.sinius15.suite.launcher.games;

import com.sinius15.suite.launcher.Data;
import com.sinius15.suite.launcher.OptionManager;

public class Chess extends Game{

	@Override
	public String getName() {
		return "chess";
	}

	@Override
	public String getJarName(){
		return "chess.jar";
	}
	
	@Override
	public String getArguments() {
		String args = "";
		
		if((Boolean)OptionManager.getValue("defaultDataFolder"))
			args = "\"dataFolder=>" + Data.DEFAULT_DATA_FOLDER.getAbsolutePath() + "\\chess\"";
		else
			args = "\"dataFolder=>" + (String) OptionManager.getValue("dataFolder") + "\\chess\"";

		return args;
	}
	
	@Override
	public int getVersionLength() {
		return 3;
	}
	
}
