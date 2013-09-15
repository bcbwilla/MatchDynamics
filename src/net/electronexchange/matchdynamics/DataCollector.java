package net.electronexchange.matchdynamics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;

/*
 * Gets all player coordinates and writes the data to file
 */
public class DataCollector implements Runnable{
	private World world;       // Minecraft world the data is coming from.
	private FileWriter writer; // File where data is going.
	private final int SLEEP_MS = 1000; // Milliseconds between datapoints.
	//TODO Add way to change SLEEP_MS from in game.

	public DataCollector() {
	}
	
	public DataCollector(World world, FileWriter writer) {
		this.world = world;
		this.writer = writer;
	}
	
	/*
	 * From Runnable interface
	 */
	@Override
	public void run() {
		while(true) {
			writePlayerCoords();  // write current coords of all players to file
			
			try {
				writer.append("TICKLINE,NULL,NULL,NULL,NULL\n"); // this line separates groupings of time steps
				TimeUnit.MILLISECONDS.sleep(SLEEP_MS); // pause between writings
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	
	/*
	 * Get players' data from world, write to file
	 */
	private void writePlayerCoords() {
		for(int i = 0; i < world.playerEntities.size(); i++) {
			// get all the data from the world object
			EntityPlayer p = (EntityPlayer) world.playerEntities.get(i);
			String userName = p.username;
			String teamName = getTeamName(p);		
			double x = p.posX;
			double y = p.posY;
			double z = p.posZ;
			
			// write to file in CSV format
			try {
				writer.append(teamName).append(",");
				writer.append(userName).append(",");
				writer.append(Double.toString(x)).append(",");
				writer.append(Double.toString(y)).append(",");
				writer.append(Double.toString(z)).append("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// for debugging
			// System.out.println(username + " " + teamName + ". x = " + Double.toString(x));
		}
	}
	
	/*
	 * Simplifies the player's "team name" because there is not obvious Team.getName() method.
	 * Maybe?
	 */
	private String getTeamName(EntityPlayer p) {
		String teamName = "None";
		Team t = p.getTeam();
		if(t != null) {
			teamName = t.toString();
			String[] parts = teamName.split("@");
			
			if(parts.length > 0) {
			teamName = parts[1];
			}
		}
		return teamName;
	}
	
	/* 
	 * File writer needs to be shared to close the file in MatchDynamicsEventHandler
	 */
	public FileWriter getWriter() {
		return writer;
	}
	
}
