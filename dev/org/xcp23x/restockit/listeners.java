//@author Chris Price (xCP23x)

package org.xcp23x.restockit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class listeners implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block chest = event.getClickedBlock();
            eventTriggered(chest);
        }
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Block sign = event.getBlock();
        if(signUtils.isRIsign(sign)){
            
        }
    }
    
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        Block block = event.getBlock();
        if(block.getType() == Material.DISPENSER) {   //Make sure the dispensable dispensee was dispensed by a dispenser
            eventTriggered(block);
        }
    }
    
    public void eventTriggered(Block chest){
        if(chestUtils.isRIchest(chest)) {
            Block sign = signUtils.getSignFromChest(chest);
            if(signUtils.isDelayedSign(sign)){
                scheduler.startSchedule(sign, signUtils.getPeriod(sign));
            } else chestUtils.fillChest(chest);
        }
    }
}
