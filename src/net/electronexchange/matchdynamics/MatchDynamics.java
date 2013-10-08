package net.electronexchange.matchdynamics;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="MatchDynamics", name="MatchDynamics", version="1.0.0")
@NetworkMod(clientSideRequired=true)

/*
 * Main class.  Standard Forge mod template.
 */
public class MatchDynamics {

      @Instance(value = "MatchDynamics")
      public static MatchDynamics instance;
     
      @SidedProxy(clientSide="net.electronexchange.matchdynamics.client.ClientProxy", serverSide="net.electronexchange.matchdynamics.CommonProxy")
      public static CommonProxy proxy;
      
      // The DataCollector class is what records and writes coordinates of all the players
      public DataCollector dataCollector = new DataCollector();
      
      // The thread that runs the DataCollector
      // The only reason it is being intialized here is to use the Thread.isAlive() method later.
      public Thread dataCollectorThread = (new Thread(dataCollector));
     
      @EventHandler
      public void preInit(FMLPreInitializationEvent event) {
      }
     
      @EventHandler
      public void load(FMLInitializationEvent event) {
              proxy.registerRenderers();
              // Register event handler to fire off DataCollector thread.
              MinecraftForge.EVENT_BUS.register(new MatchDynamicsEventHandler(instance));
              
              // Directory to store match data
              FileUtil.createDataDirectory();
      }
     
      @EventHandler 
      public void postInit(FMLPostInitializationEvent event) {
      }     
}