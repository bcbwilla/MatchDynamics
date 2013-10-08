package net.electronexchange.matchdynamics;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.client.Minecraft;

public class FileUtil {
	private static final String filenameBase = "mods" + File.separator + "matchdynamics";
	
	/*
	 * Creates a directory of data storage if it doesn't already exist.
	 */
	public static void createDataDirectory() {
        File dataDirectory = new File(filenameBase);
        if (!dataDirectory.exists()) {
      	  dataDirectory.mkdir();
        }
	}
	
  	/*
  	 * Creates a file name for the data based on current date and time.
  	 * The save path is hard coded in, so this needs to be changed accordingly.
  	 */
	public static String fileNameBuilder() {
		String base = filenameBase + File.separator + "md--";
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy--HH-mm-ss");
		Date date = new Date();
		return base + df.format(date) + ".csv";
	}
	
	private String getCurrentMapName() {
		Minecraft.getMinecraft().thePlayer.sendChatMessage("/map");
		
		return "";
		
	}
}
	

