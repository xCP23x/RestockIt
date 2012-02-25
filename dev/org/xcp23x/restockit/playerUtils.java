//@author Chris Price (xCP23x)

package org.xcp23x.restockit;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class playerUtils {
    public static void sendPlayerMessage(Player player, int number) {
        player.sendMessage("[RestockIt] Error " + number + ":");
        switch(number){
            case 1:
                player.sendMessage("This is already a RestockIt container");
                break;
            case 6:
                player.sendMessage("No chest was found below or above the sign");
                break;
            default:
                player.sendMessage("Unspecified Error");
                break;
        }
    }
    
    public static void sendPlayerMessage(Player player, int number, String string) {
        player.sendMessage("[RestockIt] Error " + number + ":");
        switch(number){
            case 2:
                player.sendMessage("You do not have permission to create a RestockIt " + string);
                break;
            case 3:
                player.sendMessage(string + " is not a valid item ID");
                break;
            case 4:
                player.sendMessage(string + " is not a valid item name");
                break;
            case 5:
                player.sendMessage(string + " is not a valid damage value");
                break;
            default:
                player.sendMessage("Unspecified Error");
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
