package com.xcp23x.RestockIt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RIblock extends BlockListener {
    public static RestockIt plugin;
    public RIblock(RestockIt instance) {
        plugin = instance;
    }
    
    @Override
public void onSignChange(SignChangeEvent event) {

    Location loc = event.getBlock().getLocation();
    Player player = event.getPlayer();
    String line2 = event.getLine(2);
    Material material = player.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).getType();
    
    //Check Permissions
    if ((event.getLine(1).equalsIgnoreCase("Full Chest")) || (event.getLine(1).equalsIgnoreCase("Full Chest")) || (event.getLine(1).equalsIgnoreCase("RestockIt")) || (event.getLine(1).equalsIgnoreCase("Restock It"))) {
        if ((material != Material.DISPENSER) && (material != Material.CHEST)) {
            player.getWorld().getBlockAt(loc).setType(Material.AIR);
            player.getWorld().dropItem(loc, new ItemStack(Material.SIGN,1));
            player.sendMessage("No chest or dispenser was found."); 
            player.sendMessage("Ensure that you place the sign ABOVE the chest.");
            
        } else if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
            PermissionManager permissions = PermissionsEx.getPermissionManager();
            
            if ((!permissions.has(player, "restockit.chest")) && (material == Material.CHEST)) {
                player.getWorld().getBlockAt(loc).setType(Material.AIR);
                player.getWorld().dropItem(loc, new ItemStack(Material.SIGN,1));
                player.sendMessage("You do not have permission to make a RestockIt chest.");
            } else if ((!permissions.has(player, "restockit.dispenser")) && (material == Material.DISPENSER)) {
                        player.getWorld().getBlockAt(loc).setType(Material.AIR);
                        player.getWorld().dropItem(loc, new ItemStack(Material.SIGN,1));
                        player.sendMessage("You do not have permission to make a RestockIt dispenser.");
            }
        } else if (!event.getPlayer().isOp()) {
            player.getWorld().getBlockAt(loc).setType(Material.AIR);
            player.getWorld().dropItem(loc, new ItemStack(Material.SIGN,1));
            player.sendMessage("You must be an op to make a RestockIt chest.");
            
            //Check the block requested actually exists
        } else if (plugin.isInteger(line2)) {
                    int parseInt = Integer.parseInt(line2);
                    if (!plugin.isValidId(parseInt)) {
                        player.getWorld().getBlockAt(loc).setType(Material.AIR);
                        player.getWorld().dropItem(loc, new ItemStack(Material.SIGN,1));
                        player.sendMessage("Incorrect Syntax - Check item " + parseInt + " exists!");
                    }
            } else if (!plugin.isValidName(line2)) {
                player.getWorld().getBlockAt(loc).setType(Material.AIR);
                player.getWorld().dropItem(loc, new ItemStack(Material.SIGN,1));
                if ("".equals(line2)) { 
                    player.sendMessage("Incorrect Syntax - No item specified");
                } else { 
                    player.sendMessage("Incorrect Syntax - Check that " + line2 + " exists!");
                    player.sendMessage("Names MUST be capitalised, and must be as bukkit names them.");
                    player.sendMessage("If you continue to have problems, use the item ID instead.");
                }
            }
    }
    }
}
