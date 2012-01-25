//@author Chris Price (xCP23x)

package org.xcp23x.RestockIt;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

class RIschedule {
    
    public static RestockIt plugin;
    public RIschedule(RestockIt instance) {plugin = instance;}
    
    //Process IDs of running schedules are stored in a HashMap
    private static HashMap<Block, Integer> blocks = new HashMap<Block, Integer>();
    
    public static void startSchedule(final Block block, int period) {
        if (blocks.get(block) == null) blocks.put(block,0); //Prepare the HashMap if it's null
        if (blocks.get(block) != 0) return; //It's already running, don't start it again
        blocks.put(block, RestockIt.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(RestockIt.plugin, new Runnable() {
            @Override
            public void run() {
                //If no sign is there, stop (the player may have removed it mid-count)
                if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
                    RIschedule.stopSchedule(block);
                    return;
                }
                
                String line3 = ((Sign)block.getState()).getLine(3);
                
                if(RIdelay.getCurrentBlocks(line3) >= RIdelay.getMaxBlocks(line3)) {
                    RIschedule.stopSchedule(block);
                    return;
                }
                                
                RIdelay.setCurrentBlocks(block, (RIdelay.getCurrentBlocks(line3) + 1)); //Adds 1 to the current value
            }
            
        },0,period * 20));
        if (blocks.get(block) == -1) {blocks.remove(block);} //If it fails to start, remove entry from HashMap
    }
    
    public static void stopSchedule(Block block) {
        int i = blocks.get(block); //Get bukkit task ID
        if (i != 0) {  //If it's 0, it's not running
            RestockIt.plugin.getServer().getScheduler().cancelTask(i); //Cancel task
            blocks.remove(block); //Remove entry from HashMap
        }
    }
}