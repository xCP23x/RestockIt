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
    public RIblock(RestockIt instance) {
        plugin = instance;
    }
    
    @Override
    public void onSignChange(SignChangeEvent event) {
        Location loc = event.getBlock().getLocation();
        Player player = event.getPlayer();
        int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
        
        //Check and fix a common error (forgotten blank line)
        if (RIcheck.checkCommand(event.getLine(0))) {
            event.setLine(2, event.getLine(1));
            event.setLine(1, event.getLine(0));
            event.setLine(0, "");
        }
        
        String line1 = event.getLine(1);
        String line2 = event.getLine(2);
        
        if (RIcheck.checkCommand(line1)) {
            //Check if the chest/dispenser is above or below the sign, or already claimed
            int cb = RIcheck.checkBlock(player, loc, x, y, z);
            
            //1  = Sign above chest
            //-1 = Sign below chest
            //2  = Chest already allocated
            //0  = No container found (ignored)
            
            //Check perms if container is below the sign, then check item requested exists
            if (cb == 1 && RIcheck.checkPermissions(player, player.getWorld().getBlockAt(x,y-1,z), loc, line2)) {RIcheck.checkItem(line2, loc, player);}
            //Check perms if container is above the sign, then check item requested exists
            if (cb == -1 && RIcheck.checkPermissions(player, player.getWorld().getBlockAt(x,y+1,z), loc, line2)) {RIcheck.checkItem(line2, loc, player);}
            //Remove sign if chest allocated
            if (cb == 2) {
                RestockIt.dropSign(loc, player.getWorld());
                player.sendMessage("[RestockIt] This is already a RestockIt container");
                player.sendMessage("[RestockIt] Remember, a RestockIt sign can be below or above a container");
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