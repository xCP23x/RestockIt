//Copyright (C) 2011-2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventListener implements Listener{
    private final RestockIt plugin = RestockIt.plugin;
    
    public EventListener(){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        //Check if a container has been opened
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block block = event.getClickedBlock();
            RCont cont = new RCont(block);
            if(cont.isRCont()){
                
            }
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        //Check if a container has been broken (or a sign, for legacy usage)
        
    }
    
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event){
        //Check is a dispenser has dispensed a dispensible dispensee
        
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent event){
        //For legacy sign usage
        
    }
}
