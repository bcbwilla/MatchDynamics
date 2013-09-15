package net.electronexchange.matchdynamics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;

/*
 *  This class fires the DataCollector.
 * 
 *  To start collecting data, right click an air block holding a feather.
 *  To stop, right click an air block holding a blaze rod.
 *  
 */

public class MatchDynamicsEventHandler {
	private MatchDynamics md;
	
	public MatchDynamicsEventHandler(MatchDynamics md) {
		this.md = md;
	}
	
	/*
	 * Event handler to start DataCollector
	 */
  	@ForgeSubscribe
  	public void playerInteractEvent(PlayerInteractEvent event) {
  		EntityPlayer p = event.entityPlayer;
  		World world = p.worldObj;
  		ItemStack heldItem = p.getHeldItem();
  		FileWriter writer;
  		
  		if(heldItem != null) {
  			int heldItemId = heldItem.itemID;
  			PlayerInteractEvent.Action targetAction = PlayerInteractEvent.Action.RIGHT_CLICK_AIR;
  		
  			try {
	  	  		if(event.action == targetAction && heldItemId == Item.feather.itemID && !md.dataCollectorThread.isAlive()){
	  	  			writer = new FileWriter(fileNameBuilder());	
	  		  		md.dataCollector = new DataCollector(world, writer);
	  		  		md.dataCollectorThread = (new Thread(md.dataCollector));
	  		  		md.dataCollectorThread.start();
	  		  		p.sendChatToPlayer(new ChatMessageComponent().addText("Start."));
	  		  	
	  	  		} else if(event.action == targetAction && heldItemId == Item.blazeRod.itemID && md.dataCollectorThread.isAlive()) {
	  	  			writer = md.dataCollector.getWriter();
	  	  			md.dataCollectorThread.stop();  // need to find a better way to stop threads.
					writer.flush();
					writer.close();
					p.sendChatToPlayer(new ChatMessageComponent().addText("Done."));
	  	  		}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
  		}
  	}
  	
  	/*
  	 * Creates a file name for the data based on current date and time.
  	 * The save path is hard coded in, so this needs to be changed accordingly.
  	 */
	private String fileNameBuilder() {
		//TODO Auto generate better save path based on user.
		String base = "/home/ben/projects/plugins/MatchDynamics/analysis/data/md--";
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy--HH-mm-ss");
		Date date = new Date();
		return base + df.format(date) + ".csv";
	}
}
	
