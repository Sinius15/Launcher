package com.sinius15.suite.launcher.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class LoggingInner {
	
	public static boolean logIn(String username, String password, JFrame frame){
		try {
			System.out.println("Checking username and password with my boss...");
			if(username.equals("") || username.contains(" ")){
				JOptionPane.showMessageDialog(frame, "The username is invalid, please try an other one.", "Invalid Username", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if(password.equals("") || password.contains(" ")){
				JOptionPane.showMessageDialog(frame, "The password is invalid, please try an other one.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if(!getReturnString("req=existsPlayer&name="+username)){
				JOptionPane.showMessageDialog(frame, "The player does not exits."
						+ "Please try an other username.", "Player does not exist", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if(!getReturnString("req=login&name=" + username + "&pass=" + password)){
				JOptionPane.showMessageDialog(frame, "The combination of username and passowrd is incorrect."+ System.lineSeparator()
						+ "Please try again.'"+ System.lineSeparator()
						+ "If you have forgotten your password, visit sinius15.com.", "Invalid login", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "There seems to be a problem with the connection to the server" + System.lineSeparator()
					+ "You can start the game in offline mode if you still want to play." + System.lineSeparator()
					+ "We are verry sorry for the inconvenience caused.", "Problem", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	private static boolean getReturnString(String req) throws IOException{
		URL url = new URL("http://sinius15.com/Launcher/portal.php?" + req);
    	URLConnection urlConn = url.openConnection();
        BufferedInputStream is = new BufferedInputStream(urlConn.getInputStream());
        byte[] b = new byte[1024];
        is.read(b);
        is.close();
        String input = new String(b, Charset.forName("US-ASCII"));
        if(input.startsWith("true"))
        	return true;
        if(input.startsWith("false"))
        	return false;
        throw new IOException("There was a problem with the internet thingy. please show this message to the game owners. debug: " + input);
	}
	
}
