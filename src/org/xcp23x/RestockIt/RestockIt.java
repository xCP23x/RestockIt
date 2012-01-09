//@author Chris Price (xCP23x)


package org.xcp23x.RestockIt;

import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RestockIt extends JavaPlugin {
    
    //Set up listeners
    private final RIplayer playerListener = new RIplayer(this);
    private final RIblock blockListener = new RIblock(this);
    //Set up logger
    static final Logger log = Logger.getLogger("Minecraft");
    public static RestockIt plugin;
    
    @Override
    public void onEnable() {
        plugin = this;
        //Get Restockit version number from config.yml
        PluginDescriptionFile pdfFile = this.getDescription();
        RestockIt.log.info("[RestockIt] v" + pdfFile.getVersion() + " Started"); //Tell the server RestockIt has started
        
        //Register listeners to events
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this); //For opening chests
        pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Event.Priority.Normal, this); //For placing signs
        pm.registerEvent(Event.Type.BLOCK_DISPENSE, blockListener, Event.Priority.Normal, this); //For dispensers dispensing dispensables
    }
        @Override
    public void onDisable() {
        //Tell the server RestockIt has stopped
        PluginDescriptionFile pdfFile = this.getDescription();
        RestockIt.log.info("[RestockIt] v" + pdfFile.getVersion() + " Stopped");
    }
     
    public static void dropSign(Location loc, World world) {
        //Removes a sign and drops it as an item.
        world.getBlockAt(loc).setType(Material.AIR);
        world.dropItem(loc, new ItemStack(Material.SIGN, 1));
    }
}