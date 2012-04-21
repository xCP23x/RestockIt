//Copyright (C) 2011-2012 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.xcp23x.restockit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

class playerUtils extends RestockIt {
    //Colours for use in player messages:
    private static ChatColor bCol = ChatColor.DARK_AQUA; //Bracket colour
    private static ChatColor rCol = ChatColor.GRAY; // RestockIt colour
    private static ChatColor eCol = ChatColor.DARK_RED; //Error colour
    private static ChatColor dCol = ChatColor.RED;//Description colour
    private static ChatColor sCol = ChatColor.GOLD; //String colour
    
    public static void sendPlayerMessage(Player player, int number) {
        //Give the error code here, so we only have to do it once
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
        //Give the error code here, so we only have to do it once
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
            case 7:
                player.sendMessage(dCol + "You do not have permission to use " + sCol + string);
                break;
            case 8:
                player.sendMessage(dCol + "You do not have permission to open a RestockIt " + sCol + string);
                break;
            case 9:
                player.sendMessage(dCol + "You do not have permission to destroy a RestockIt " + sCol + string);
            default:
                player.sendMessage(dCol + "Unspecified Error");
                break;
        }
    }
    
    /*
    public static boolean hasBlacklistPermissions(Player player){
        return hasPermissions(player, "restockit.blacklist.bypass");
    }
    
    public static boolean hasContainerPermissions(Player player, Block container, String line, Boolean creating) {
        //Check if it's an incinerator
        String containerName = signUtils.isIncinerator(line) ? "incinerator" : container.getType().name().toLowerCase();
        String perm = creating ? "restockit." + containerName + ".create" : "restockit." + containerName + ".open";
        if (hasPermissions(player, perm)) return true;
        return hasDeprecatedPermissions(player, "restockit." + containerName);
    }
     */
    
    public static boolean hasPermissions(RIperm riperm){
        String perm = riperm.getPerm();
        String depperm = riperm.getDeprecatedPerm();
        Player player = riperm.getPlayer();
        RestockIt.log.info("Perm checked: " + perm);
        
        PermissionManager pm = Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx") ? PermissionsEx.getPermissionManager() : null;
        //If using SuperPerms:
        if(pm == null) {
            if(RestockIt.plugin.getConfig().getBoolean("opsOverrideBlacklist") && player.isOp() && "restockit.blacklist.bypass".equals(perm)) return true;
            if(player.hasPermission(perm)) return true;
            if(player.hasPermission(depperm)) {
                warnDepPermissions(riperm);
                return true;
            }
            
        //If using PermissionsEx:
        } else{
            if(pm.has(player, perm, player.getWorld().getName())) return true;
            if(pm.has(player, depperm, player.getWorld().getName())) {
                warnDepPermissions(riperm);
                return true;
            }
        }
        
        return false;
    }
    
    private static void warnDepPermissions(RIperm riperm){
        RestockIt.log.warning("[RestockIt] Using deprecated permission: " + riperm.getDeprecatedPerm());
        RestockIt.log.info("[RestockIt] Use " + riperm.getPerm() + " instead");
        RestockIt.log.info("[RestockIt] See http://dev.bukkit.org/server-mods/restockit/ for more info");
    }
}
