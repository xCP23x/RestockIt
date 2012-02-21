//@author Chris Price (xCP23x)

package org.xcp23x.restockit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

//Blank
//RestockIt
//Item:Damage
//#stacks, ts
//OR
//#s, ts
//OR
//#, ts


public class signUtils {
    
    public static Material getMaterial(Block block){
        return getType(block) > 0 ? Material.getMaterial(getType(block)) : Material.AIR;
    }
    
    public static short getDamage(Block block) {
        Sign sign = (Sign)block.getState();
        String line = sign.getLine(2);
        String dmgStr = (line.indexOf(":") >= 0 ? line : line+":0").split(":")[1];
        return Short.parseShort(dmgStr);
    }
    
    public static int getType(Block block) {
        Sign sign = (Sign)block.getState();
        String line = sign.getLine(2);
        if (line.equalsIgnoreCase("Incinerator")) {
            return 0;
        }
        Material t;
	String sa[] = (line.indexOf(":") ==1 ? line : line+":0").split(":");
        
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
		
	return t.getId();
    }
    
    public static void setMaxItems(int items, Block block) {
        Sign sign = (Sign)block.getState();
        String part = sign.getLine(3).split(",")[1];
        sign.setLine(3, items + "," + part);
        sign.update();
    }
    
    public static int getMaxItems(Block block) {
        Sign sign = (Sign)block.getState();
        String str = sign.getLine(3).contains(",") ? sign.getLine(3).split(",")[0].toLowerCase() : null;
        if(str==null) return 0;
        return (str.contains("s") || str.contains("stacks")) ? Integer.parseInt(str.replaceAll("stacks", "").replaceAll("s", ""))*getMaterial(sign.getBlock()).getMaxStackSize() : 0;
    }
    
    public static void setPeriod(float period, Block block) {
        Sign sign = (Sign)block.getState();
        String part1 = sign.getLine(3).split(",")[0];
        String part2 = period%20 == 0 ? period/20 + "s" : period + "t";
        sign.setLine(3, part1 + ", " + part2);
        sign.update();
    }
    
    public static int getPeriod(Block block) {
        Sign sign = (Sign)block.getState();
        String periodStr = sign.getLine(3).contains(",") ? sign.getLine(3).split(",")[1].replaceAll(" ", "") : "0";
        return periodStr.contains("s") ? Short.parseShort(periodStr.replaceAll("s", ""))*20 : Integer.parseInt(periodStr);
    }
    
    public static boolean isRIsign(Block block){
        
        if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
            Sign sign = (Sign)block.getState();
            String line0 = sign.getLine(0).toLowerCase();
            String line1 = sign.getLine(1).toLowerCase();
            if(line1.contains("restockit") || line1.contains("restock it") || line1.contains("full chest") || line1.contains("full dispenser")) {
                return true;
            } else if(line0.contains("restockit") || line0.contains("restock it") || line0.contains("full chest") || line0.contains("full dispenser")) {
                sign.setLine(3, sign.getLine(2));
                sign.setLine(2, sign.getLine(1));
                sign.setLine(1, sign.getLine(0));
                sign.setLine(0, "");
                sign.update();
                return true;
            }
        }
        return false;
    }
    
    public static void dropSign(Block sign) {
        Location loc = sign.getWorld().getBlockAt(sign.getX(), sign.getY(), sign.getZ()).getLocation();
        sign.setType(Material.AIR);
        sign.getWorld().dropItem(loc, new ItemStack(Material.SIGN, 1));
    }
    
    public static boolean isDelayedSign(Block sign) {
        if(getMaxItems(sign) == 0 || getPeriod(sign) == 0) return false;
        return true;
    }
    
    public static boolean isIncinerator(Block block) {
        Sign sign = (Sign)block.getState();
        if(sign.getLine(2).toLowerCase().contains("incinerator")) return true;
        return false;
    }
    
    public static Block getSignFromChest(Block chest) {
        if(chestUtils.isSignAboveChest(chest)) return getSignAboveChest(chest);
        if(chestUtils.isSignBelowChest(chest)) return getSignBelowChest(chest);
        return null;
    }
    
    public static Block getSignAboveChest(Block chest) {
        return chest.getWorld().getBlockAt(chest.getX(), chest.getY() +1, chest.getZ());
    }
    
    public static Block getSignBelowChest(Block chest) {
        return chest.getWorld().getBlockAt(chest.getX(), chest.getY() -1, chest.getZ());
    }
    
    public static boolean hasErrors(Block sign, Player player) {
        
        String line = ((Sign)sign.getState()).getLine(2);
        
        switch(getType(sign)){
            case -3:
                playerUtils.sendPlayerMessage(player, 5, line.split(":")[1]);
                return true;
            case -2:
                playerUtils.sendPlayerMessage(player, 4, line.contains(":") ? line.split(":")[0] : line);
                return true;
            case -1:
                playerUtils.sendPlayerMessage(player, 3, line.contains(":") ? line.split(":")[0] : line);
                return true;
        }
        return false;
    }
}
