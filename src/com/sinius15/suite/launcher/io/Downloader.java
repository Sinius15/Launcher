package com.sinius15.suite.launcher.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.sinius15.suite.launcher.Data;
import com.sinius15.suite.launcher.games.Game;

public class Downloader {

	private static boolean cancleTask = false;
	
	public static synchronized void downloadFile(URL url, File out, PrintStream status) throws Exception{
		if(url.toURI() ==null)
			System.out.println("null");
        URLConnection urlConn = url.openConnection();
        int size = urlConn.getContentLength();
        BufferedInputStream is = new BufferedInputStream(urlConn.getInputStream());
        
        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(out));
        byte[] b = new byte[8*1024];
        int read = 0;
        int done = 0;
        status.println("starting download of file http://" + url.getHost() + url.getPath());
        
        status.println("downloading...   file size: " + size);
        Data.downloadFrame.progressBar.setValue(0);
        while((read = is.read(b)) > -1){
        	if(cancleTask){
        		cancleTask = false;
        		break;
        	}
        		
            bout.write(b,0, read);
            done += read;
            status.println("downloading...   (" + done + "/" + size + ")");
            Data.downloadFrame.progressBar.setValue((done/size)*100);
        }
        status.println("downloading...   (done)");
        bout.flush();
        bout.close();
        is.close();
	}
	
	public static void cancleTasks(){
		cancleTask = true;
	}
	
    public static void unZip(String zipFile, String outputFolder, PrintStream status) throws IOException{
    	byte[] buffer = new byte[1024];
    	File folder = new File(outputFolder);
    	status.println("unzipping...   (to: " + folder.getAbsolutePath() + ")");
    	if(!folder.exists())
    		folder.mkdir();
    	
    	ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
    	ZipEntry ze;
    	while((ze = zis.getNextEntry())!=null){
    	   String fileName = ze.getName();
           File newFile = new File(outputFolder + File.separator + fileName);
           
           
           if(ze.isDirectory()){
        	   newFile.mkdirs();
        	   continue;
           }
           System.out.println("unzipping...   (" + ze.getName() + ")");   
           newFile.createNewFile();
 
           FileOutputStream fos = new FileOutputStream(newFile);             
 
           int len;
           while ((len = zis.read(buffer)) > 0) 
        	   fos.write(buffer, 0, len);
           fos.close();   
    	}
 
        zis.closeEntry();
    	zis.close();
    	new File(zipFile).delete();
   }
    
    
    public static String[] getVersionList(PrintStream status, Game game) throws IOException{
    	URL url = new URL("http://sinius15.com/Launcher/portal.php?game="+game.getName()+"&&req=versionList");
    	URLConnection urlConn = url.openConnection();
        BufferedInputStream is = new BufferedInputStream(urlConn.getInputStream());
        byte[] b = new byte[1024];
        Data.downloadFrame.progressBar.setValue(0);
        is.read(b);
        is.close();
        String input = new String(b, Charset.forName("US-ASCII"));
        String[] output = input.split(",");
        return output;
    }
    
    
    public static String getLatestVersion(PrintStream status, Game game) throws IOException{
    	URL url = new URL("http://sinius15.com/Launcher/portal.php?game="+game.getName()+"&&req=latestVersion");
    	URLConnection urlConn = url.openConnection();
        BufferedInputStream is = new BufferedInputStream(urlConn.getInputStream());
        byte[] b = new byte[1024];
        Data.downloadFrame.progressBar.setValue(0);
        is.read(b);
        is.close();
        return new String(b, Charset.forName("US-ASCII"));
    }
	
}
