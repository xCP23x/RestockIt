//@author Chris Price (xCP23x)

package org.xcp23x.restockit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        
        if(signUtils.isRIsign(sign)){
            
            if(chestUtils.getChestFromSign(sign) == null){
                signUtils.dropSign(sign);
                playerUtils.sendPlayerMessage(player, 6);
                return;
            }
            
            if(chestUtils.isAlreadyRIchest(sign)) {
                playerUtils.sendPlayerMessage(player, 1);
                signUtils.dropSign(sign);
                return;
            }
            
            if(signUtils.isIncinerator(sign)) {
                eventTriggered(sign);
                return;
            }
            
            if(signUtils.hasErrors(sign, player)) {
                signUtils.dropSign(sign);
                return;
            }
            
            //For debug
            //player.sendMessage(chestUtils.getChestFromSign(sign).getType().name().toLowerCase());
            
            if(!playerUtils.hasPermissions(player, chestUtils.getChestFromSign(sign), sign)){
                signUtils.dropSign(sign);
                playerUtils.sendPlayerMessage(player, 2, chestUtils.getChestFromSign(sign).getType().name().toLowerCase());
                return;
            }
            
            eventTriggered(sign);
            
        }
    }
    
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        Block block = event.getBlock();
        if(block.getType() == Material.DISPENSER) {   //Make sure the dispensable dispensee was dispensed by a dispenser
            eventTriggered(block);
        }
    }
    
    public void eventTriggered(Block block){
        Block sign = null;
        Block chest = null;
        
        if(chestUtils.isRIchest(block)) {
            sign = signUtils.getSignFromChest(block);
            chest = block;
        } else if(signUtils.isRIsign(block)) {
            sign = block;
            chest = chestUtils.getChestFromSign(block);
        }
        
        if(sign != null){
            if(signUtils.isDelayedSign(sign)){
                scheduler.startSchedule(sign, signUtils.getPeriod(sign));
            } else if(chest != null) chestUtils.fillChest(chest);
        }
    }
}
