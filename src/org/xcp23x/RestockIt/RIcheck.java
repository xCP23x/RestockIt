//@author Chris Price (xCP23x)
//Thanks to Scott Marshall (scootz) for optimisations to checkId and checkPermissions

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

class RIcheck{
    
    public static RestockIt plugin;
    public RIcheck(RestockIt instance) {plugin = instance;}
    
    public static boolean checkCommand(String line1) {
        if (line1.equalsIgnoreCase("Full Chest") || line1.equalsIgnoreCase("Full Dispenser") || line1.equalsIgnoreCase("RestockIt") || line1.equalsIgnoreCase("Restock It")) {
            return true;
        }
        return false;
    }

    public static int checkID(String line) {
        if (line.equalsIgnoreCase("Incinerator")) {
            return 0;
        }
        Material t;
	String sa[];
	sa = (line.indexOf(":") >= 0 ? line : line+":0").split(":");
        
	if ((t = Material.getMaterial(sa[0])) == null) {
            try {
            	if ((t = Material.getMaterial(Integer.parseInt(sa[0]))) == null) {
                    return -1; // it's an int, but not an valid Material ID.
            	}
            } catch (NumberFormatException ex) {
            	return -2; // it's not an int or ID.
            }
	}

        try {
            Short.parseShort(sa[1]);
	} catch (NumberFormatException ex) {
            return -3; // problem with damage value
	}
		
	return t.getId(); // its a string ID
    }

    public static boolean checkAllocated(Block sign) {
        //Check if the chest is already allocated as RestockIt (return true if it is)
        if (sign.getType() == Material.WALL_SIGN || sign.getType() == Material.SIGN_POST) {
            String string = ((Sign) sign.getState()).getLine(1);
            if (checkCommand(string)) {          
                return true;
            }
        }
        return false;
    }

    public static int checkBlock(Block sign) {
        //Check below the sign - return 1 if it's unallocated, 2 if allocated, else continue if no container
        
        Block block = sign.getWorld().getBlockAt(sign.getX(), sign.getY()-1, sign.getZ());
        int x = block.getX(), y = block.getY(), z = block.getZ();
        if (block.getType() == Material.CHEST) {
            if (!checkAllocated(block.getWorld().getBlockAt(x, y-2, z))) {
                return 1;
            } else {
                return 2;
            }
        }
        
        if (block.getType() == Material.DISPENSER) {
            if (!checkAllocated(block.getWorld().getBlockAt(x, y-2, z))) {
                return 1;
            } else {
                return 2;
            }
        }
        
        //Check above the sign - return -1 if it's unallocated, 2 if allocated, else continue if no container
        block = sign.getWorld().getBlockAt(sign.getX(), sign.getY()+1, sign.getZ());
        if (block.getType() == Material.CHEST) {
            if (!checkAllocated(block.getWorld().getBlockAt(x, y+2, z))) {
                return 1;
            } else {
                return 2;
            }
        }
        
        if (block.getType() == Material.DISPENSER) {
            if (!checkAllocated(block.getWorld().getBlockAt(x, y+2, z))) {
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }

    public static void checkItem(String line2, Location loc, Player player) {
      //  String line2 = ((Sign)(loc.getBlock()).getState()).getLine(2);
        World world = player.getWorld();
        int id = checkID(line2);
        
        //Item int ID returned if valid ---unused
        //ID  0 = Incinerator
        //ID -1 = It's an int, but not an ID
        //ID -2 = It's not an int, not an ID
        //ID -3 = Bad damage value

        switch(id) {
            case -2:
                player.sendMessage("[RestockIt] \"" + line2 + "\"" + " could not be found");
                player.sendMessage("[RestockIt] If you have trouble with name IDs, use the number instead");
                RestockIt.dropSign(loc, world);
                break;
            case -1:
                player.sendMessage("[RestockIt] Item ID " + line2 + " could not be found");
                player.sendMessage("[RestockIt] Try again, or enter its name in UPPER CASE");
                RestockIt.dropSign(loc, world);
                break;
            case -3:
                player.sendMessage("[RestockIt] There was a problem with the damage value");
                player.sendMessage("[RestockIt] Please make sure you typed it correctly");
                RestockIt.dropSign(loc, world);
                break;
            case 0:
                player.sendMessage("[RestockIt] Incinerator created");
                player.sendMessage("[RestockIt] Use shift to move entire stacks");
                player.sendMessage("[RestockIt] Re-open the chest to incinerate items");
                break;
            default:
                break;
        }
    }

    public static boolean checkPermissions(Player player, Block container, Block sign) {
        PermissionManager pm = Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx") ? PermissionsEx.getPermissionManager() : null;
        String line = ((Sign)sign.getState()).getLine(2); 

        String containerName = line.equalsIgnoreCase("incinerator") ? "incinerator" : container.getType().toString().toLowerCase();
        
        if (pm != null) {
            if(!pm.has(player, "restockit."+containerName, player.getWorld().getName())) {
                player.sendMessage("[RestockIt] You do not have permission to make a RestockIt "+containerName);
                return false;
            } else return true;
        } else if(player.isOp()) {
            return true;
        } else if(containerName.equals("incinerator")){
            return true;
        } else player.sendMessage("[RestockIt] You must be an op to make a RestockIt "+containerName);
        return false;
    }
}