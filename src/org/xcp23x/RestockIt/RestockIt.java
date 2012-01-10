//@author Chris Price (xCP23x)

package org.xcp23x.RestockIt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
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
    
    public static void startTimer(final Block block, int period, final int countTo){
        //Counts up on a sign until countTo is reached
        final Timer timer = new Timer(period,null);
        timer.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent evt) {
                //If no sign is there, stop (the player may have removed it mid-count)
                if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
                    timer.stop();
                    return;
                }
                
                //Get the sign state
                BlockState state = block.getState();
                
                switch(RIcheck.checkID(((Sign)state).getLine(3))){
                    
                    //If 1 or -1 is returned, it's an integer so can be added to
                    case 1:
                        ((Sign)state).setLine(3, String.valueOf(Integer.parseInt(((Sign)state).getLine(3)) + 1)); //Adds 1 to the current value
                        break;
                        
                    case -1:
                        ((Sign)state).setLine(3, String.valueOf(Integer.parseInt(((Sign)state).getLine(3)) + 1));
                        break;
                        
                    default:
                        ((Sign)state).setLine(3, "0"); //Set it to 0, used to initialise the sign, will start counting on next tick
                        break;
                }
                
                state.update(); //Save changes to the sign
                
                if (Integer.parseInt(((Sign)state).getLine(3)) >= countTo) {
                    timer.stop(); //Stop the timer when it's reached the countTo
                }
            }
        });
        timer.start(); //Start the timer (we only defined it then)
    }
}