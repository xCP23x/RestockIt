//@author Chris Price (xCP23x)

package org.xcp23x.RestockIt;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class RIblock extends BlockListener {
    public static RestockIt plugin;
    public RIblock(RestockIt instance) {plugin = instance;}
    
    @Override
    public void onSignChange(SignChangeEvent event) {
        //Check and fix a common error (forgotten blank line)
        if (RIcheck.checkCommand(event.getLine(0))) {
            event.setLine(3, event.getLine(2));
            event.setLine(2, event.getLine(1));
            event.setLine(1, event.getLine(0));
            event.setLine(0, "");
        }
        
        Location loc = event.getBlock().getLocation();
        Player player = event.getPlayer();
        String[] lines = event.getLines();
        String line1 = lines[1];
        String line2 = lines[2];
        Block block = event.getBlock();
        int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
        
        if (RIcheck.checkCommand(line1)) {
            //Check if the chest/dispenser is above or below the sign, or already claimed
            int cb = RIcheck.checkBlock(block);
            
            //1  = Sign above chest
            //-1 = Sign below chest
            //2  = Chest already allocated
            //0  = No container found
            
            switch(cb) {
                case 1:
                    //Check perms if container is below the sign, then check item requested exists
                    if(RIcheck.checkPermissions(player, player.getWorld().getBlockAt(x,y-1,z), loc.getBlock())){
                        RIcheck.checkItem(line2, loc, player);
                        String nline3 = RIdelay.prepareLines(lines);
                        String line3 = nline3 == null ? "" : nline3;
                        event.setLine(3, line3);
                        RIschedule.startSchedule(block, RIdelay.getPeriod(line3)); //Start the schedule for this sign
                    } else RestockIt.dropSign(block.getLocation(), player.getWorld());
                    break;
                    
                case -1:
                    //Check perms if container is above the sign, then check item requested exists
                    if(RIcheck.checkPermissions(player, player.getWorld().getBlockAt(x,y+1,z), loc.getBlock())) {
                        RIcheck.checkItem(line2, loc, player);
                        String nline3 = RIdelay.prepareLines(lines);
                        String line3 = nline3 == null ? "" : nline3;
                        event.setLine(3, line3);
                        RIschedule.startSchedule(block, RIdelay.getPeriod(line3)); //Start the schedule for this sign
                        
                    } else RestockIt.dropSign(block.getLocation(), player.getWorld());
                    break;
                    
                case 0:
                    //Remove sign if chest doesn't exist
                    player.sendMessage("[RestockIt] No chest or dispenser was found");
                    player.sendMessage("[RestockIt] Ensure you placed the sign directly above or below the chest or dispenser");
                    RestockIt.dropSign(loc, player.getWorld());
                    break;
                    
                case 2:
                    //Remove sign if chest is allocated
                    RestockIt.dropSign(loc, player.getWorld());
                    player.sendMessage("[RestockIt] This is already a RestockIt container");
                    player.sendMessage("[RestockIt] Remember, a RestockIt sign can be below or above a container");
                    break;
            }
        }
    }
    
    @Override
    public void onBlockDispense(BlockDispenseEvent event) {
        Block block = event.getBlock();
        if(block.getType() == Material.DISPENSER) {   //Make sure the dispensable dispensee was dispensed by a dispenser
            RIplayer.checkSign(block);
        }
    }
}