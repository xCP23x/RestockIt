//@author Chris Price (xCP23x)

package org.xcp23x.restockit;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class scheduler {
    
    private static HashMap<Block, Integer> schedules = new HashMap<Block, Integer>();
    
    public static void startSchedule(final Block block, int period) {
        if (schedules.get(block) == null) schedules.put(block,0); //Prepare the HashMap if it's null
        if (schedules.get(block) != 0) return; //It's already running, don't start it again
        
        schedules.put(block, RestockIt.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(RestockIt.plugin, new Runnable() {
            @Override
            public void run() {
                //If no sign is there, stop (the player may have removed it mid-count)
                if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
                    scheduler.stopSchedule(block);
                    return;
                }
                
                Block chest = chestUtils.getChestFromSign(block);
                
                if(chestUtils.getCurrentItems(signUtils.getMaterial(block), block) >= signUtils.getMaxItems(block)) {
                    scheduler.stopSchedule(block);
                    return;
                }
                
                //Add item to chest
                ItemStack stack = new ItemStack(signUtils.getMaterial(block), 1);
                chestUtils.getInventory(chest).addItem(stack);
                RestockIt.log.info("Add one item until full with " + signUtils.getMaxItems(block) + " items");
            }
            
        },0,period));
        if (schedules.get(block) == -1) {schedules.remove(block);} //If it fails to start, remove entry from HashMap
    }
    
    public static void stopSchedule(Block block) {
        int i = schedules.get(block); //Get bukkit task ID
        if (i != 0) {  //If it's 0, it's not running
            RestockIt.plugin.getServer().getScheduler().cancelTask(i); //Cancel task
            schedules.remove(block); //Remove entry from HashMap
        }
    }
}
