//@author Chris Price (xCP23x)

package org.xcp23x.restockit;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class playerUtils {
    public static void sendPlayerMessage(Player player, int number) {
        switch(number){
            
        }
    }
    
    public static void sendPlayerMessage(Player player, int number, String string) {
        switch(number){
            
        }
    }
    
    public static boolean hasPermissions(Player player, Block container, Block sign) {
        PermissionManager pm = Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx") ? PermissionsEx.getPermissionManager() : null;
        String line = ((Sign)sign.getState()).getLine(2);
        String containerName = line.equalsIgnoreCase("incinerator") ? "incinerator" : container.getType().toString().toLowerCase();
        
        if(pm == null) {
            if(player.hasPermission("restockit." + containerName)) return true;
        } else if(pm.has(player, "restockit." + containerName, player.getWorld().getName())) return true;
        return false;
    }
}
