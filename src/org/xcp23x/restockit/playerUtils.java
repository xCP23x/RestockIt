//@author Chris Price (xCP23x)

package org.xcp23x.restockit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class playerUtils {
    static ChatColor bCol = ChatColor.DARK_AQUA; //Bracket colour
    static ChatColor rCol = ChatColor.GRAY; // RestockIt colour
    static ChatColor eCol = ChatColor.DARK_RED; //Error colour
    static ChatColor dCol = ChatColor.RED;//Description colour
    static ChatColor sCol = ChatColor.GOLD; //String colour
    
    public static void sendPlayerMessage(Player player, int number) {
        player.sendMessage(bCol + "[" + rCol + "RestockIt" + bCol + "]" + eCol + " Error " + number + ":");
        switch(number){
            case 1:
                player.sendMessage(dCol + "This is already a RestockIt container");
                break;
            case 6:
                player.sendMessage(dCol + "No chest was found below or above the sign");
                break;
            default:
                player.sendMessage(dCol + "Unspecified Error");
                break;
        }
    }
    
    public static void sendPlayerMessage(Player player, int number, String string) {
        player.sendMessage(bCol + "[" + rCol + "RestockIt" + bCol + "]" + eCol + " Error " + number + ":");
        switch(number){
            case 2:
                player.sendMessage(dCol + "You do not have permission to create a RestockIt " + sCol + string);
                break;
            case 3:
                player.sendMessage(sCol + string + dCol + " is not a valid item ID");
                break;
            case 4:
                player.sendMessage(sCol + string + dCol + " is not a valid item name");
                break;
            case 5:
                player.sendMessage(sCol + string + dCol + " is not a valid damage value");
                break;
            default:
                player.sendMessage(dCol + "Unspecified Error");
                break;
        }
    }
    
    public static boolean hasPermissions(Player player, Block container, String line) {
        
        PermissionManager pm = Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx") ? PermissionsEx.getPermissionManager() : null;
        String containerName = line.equalsIgnoreCase("incinerator") ? "incinerator" : container.getType().name().toLowerCase();
        
        if(pm == null) {
            if(player.hasPermission("restockit." + containerName)) return true;
        } else if(pm.has(player, "restockit." + containerName, player.getWorld().getName())) return true;
        return false;
    }
}
