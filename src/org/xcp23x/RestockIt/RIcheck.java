//@author Chris Price (xCP23x)

package org.xcp23x.RestockIt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RIcheck{

    public static boolean checkCommand(String line1) {
        //Check if player is actually making a RestockIt chest. (Accepts fullchest syntax)
        if (line1.equalsIgnoreCase("Full Chest") || line1.equalsIgnoreCase("Full Dispenser") || line1.equalsIgnoreCase("RestockIt") || line1.equalsIgnoreCase("Restock It")) {
            return true;
        }
        return false;
    }

    public static int checkID(String line) {
        if (line.equalsIgnoreCase("Incinerator")) {
            return 3;
        }
        if (line.contains(":")) {
            String damageStr = line.split(":")[1];
            String itemStr = line.split(":")[0];
            try {
                Short.parseShort(damageStr); //Only used to give ArrayIndexOutOfBoundsException, no need to assign to anything
                int item = Integer.parseInt(itemStr);
                if ((item != 0) && (Material.getMaterial(item) != null)) {
                    return 1;
                } //It's an int ID
                return -1; //It's an int, but not an ID
            } catch (ArrayIndexOutOfBoundsException ex) {
                return -2; //Problem with damage value
            } catch (NumberFormatException ex) {
                if ((!"AIR".equals(itemStr)) && (itemStr != null) && (Material.getMaterial(itemStr) != null)) {
                    return 2;
                } //It's a string ID
                return 0; //Not an int or an id.
            }
        } else {
            try {
                int item = Integer.parseInt(line);
                if ((item != 0) && (Material.getMaterial(item) != null)) {
                    return 1;
                } //It's an int ID
                return -1; //It's an int, but not an ID
            } catch (NumberFormatException ex) {
                if ((!"AIR".equals(line)) && (line != null) && (Material.getMaterial(line) != null)) {
                    return 2;
                } //It's a string ID
            }
            return 0; //Not an int, not an ID
        }
    }

    public static boolean checkAllocated(int x, int y, int z, Player player) {
        //Check if the chest is already allocated as RestockIt (return true if it is)
        Block sign = player.getWorld().getBlockAt(x, y, z);
        if (sign.getType() == Material.WALL_SIGN || sign.getType() == Material.SIGN_POST) {
            String string = ((Sign) sign.getState()).getLine(1);
            if (checkCommand(string)) {
                return true;
            }
        }
        return false;
    }

    public static int checkBlock(Player player, Location loc, int x, int y, int z) {
        //Check below the sign - return 1 if it's unallocated, 2 if allocated, else continue if no container
        Block block = player.getWorld().getBlockAt(x, y - 1, z);
        if (block.getType() == Material.CHEST) {
            if (!checkAllocated(x, y - 2, z, player)) {
                return 1;
            }
        } else {
            return 2;
        }
        if (block.getType() == Material.DISPENSER) {
            if (!checkAllocated(x, y - 2, z, player)) {
                return 1;
            }
        } else {
            return 2;
        }
        //Check above the sign - return -1 if it's unallocated, 2 if allocated, else continue if no container
        block = player.getWorld().getBlockAt(x, y + 1, z);
        if (block.getType() == Material.CHEST) {
            if (!checkAllocated(x, y + 2, z, player)) {
                return 1;
            } else {
                return 2;
            }
        }
        if (block.getType() == Material.DISPENSER) {
            if (!checkAllocated(x, y + 2, z, player)) {
                return 1;
            } else {
                return 2;
            }
        }
        //No chest or dispenser found
        player.sendMessage("[RestockIt] No chest or dispenser was found");
        player.sendMessage("[RestockIt] Ensure you placed the sign directly above or below the chest or dispenser");
        RestockIt.dropSign(loc, player.getWorld());
        return 0;
    }

    public static void checkItem(String line2, Location loc, Player player) {
        World world = player.getWorld();
        int id = checkID(line2);
        //ID  0 = It's not an int, not an ID
        //ID  1 = It's an integer ID  //Unused
        //ID  2 = It's a string ID    //Unused
        //ID  3 = Incinerator
        //ID -1 = It's an int, but not an ID
        //ID -2 = Bad damage value

        switch(id) {
            case 0:
                player.sendMessage("[RestockIt] \"" + line2 + "\"" + " could not be found");
                player.sendMessage("[RestockIt] If you have trouble with name IDs, use the number instead");
                RestockIt.dropSign(loc, world);
                break;
            case -1:
                player.sendMessage("[RestockIt] Item ID " + line2 + " could not be found");
                player.sendMessage("[RestockIt] Try again, or enter its name in UPPER CASE");
                RestockIt.dropSign(loc, world);
                break;
            case -2:
                player.sendMessage("[RestockIt] There was a problem with the damage value");
                player.sendMessage("[RestockIt] Please make sure you typed it correctly");
                RestockIt.dropSign(loc, world);
                break;
            case 3:
                player.sendMessage("[RestockIt] Incinerator created");
                player.sendMessage("[RestockIt] Use shift to move entire stacks");
                player.sendMessage("[RestockIt] Re-open the chest to incinerate items");
                break;
            default:
                break;
        }
    }

    public static boolean checkPermissions(Player player, Block block, Location loc, String line) {
        Material material = block.getType();
        //Check if PermissionsEx is in use
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
            //Set PermissionsEx as the perms manager
            PermissionManager perms = PermissionsEx.getPermissionManager();
            if (line.equalsIgnoreCase("Incinerator")) {
                //Check if they're allowed to make an incinerator
                if (!perms.has(player, "restockit.incinerator")) {
                    RestockIt.dropSign(loc, player.getWorld());
                    player.sendMessage("[RestockIt] You do not have permission to make a RestockIt incinerator.");
                    return false;
                } else {return true;}
                
                //Check if they're allowed to make a RestockIt chest
            } else if (!perms.has(player, "restockit.chest") && (material == Material.CHEST)) {
                RestockIt.dropSign(loc, player.getWorld());
                player.sendMessage("[RestockIt] You do not have permission to make a RestockIt chest.");
                return false;
                
                //Check if they're allowed to make a RestockIt dispenser
            } else if (!perms.has(player, "restockit.dispenser") && (material == Material.DISPENSER)) {
                RestockIt.dropSign(loc, player.getWorld());
                player.sendMessage("[RestockIt] You do not have permission to make a RestockIt dispenser.");
                return false;
            }
            
        } else if (!player.isOp()) {
            //If not an op, they can make an incinerator but nothing else
            if (line.equalsIgnoreCase("Incinerator")) {
                return true;
            }
            RestockIt.dropSign(loc, player.getWorld());
            if (material == Material.CHEST) {
                player.sendMessage("[RestockIt] You must be an op to make a RestockIt chest");
            }
            if (material == Material.DISPENSER) {
                player.sendMessage("[RestockIt] You must be an op to make a RestockIt dispenser");
            }
            return false;
        }
        return true;
    }    
}