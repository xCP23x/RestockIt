//@author Chris Price (xCP23x)

package org.xcp23x.restockit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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
    
    public static Material getMaterial(Block sign){
        String line = ((Sign)sign).getLine(1);
        String matStr = (line.indexOf(":") >= 0 ? line : line+":0").split(":")[0];
        return Material.getMaterial(matStr);
    }
    
    public static short getDamage(Block sign) {
        String line = ((Sign)sign).getLine(1);
        String dmgStr = (line.indexOf(":") >= 0 ? line : line+":0").split(":")[1];
        return Short.parseShort(dmgStr);
    }
    
    public static int getType(Block sign) {
        String line = ((Sign)sign).getLine(1);
        if (line.equalsIgnoreCase("Incinerator")) {
            return 0;
        }
        Material t;
	String sa[] = (line.indexOf(":") >= 0 ? line : line+":0").split(":");
        
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
    
    public static void setMaxItems(int items, Block sign) {
        String part = ((Sign)sign).getLine(3).split(",")[1];
        ((Sign)sign).setLine(3, items + "," + part);
    }
    
    public static int getMaxItems(Block sign) {
        String str = ((Sign)sign).getLine(3).contains(",") ? ((Sign)sign).getLine(3).split(",")[0].toLowerCase() : null;
        return (str.contains("s") || str.contains("stacks")) ? Integer.parseInt(str.replaceAll("stacks", "").replaceAll("s", ""))*getMaterial(sign).getMaxStackSize() : 0;
    }
    
    public static void setPeriod(float period, Block sign) {
        String part1 = ((Sign)sign).getLine(3).split(",")[0];
        String part2 = period%20 == 0 ? period/20 + "s" : period + "t";
        ((Sign)sign).setLine(3, part1 + ", " + part2);
    }
    
    public static int getPeriod(Block sign) {
        String periodStr = ((Sign)sign).getLine(3).contains(",") ? ((Sign)sign).getLine(3).split(",")[1].replaceAll(" ", "").replaceAll("t", "") : "0";
        return periodStr.contains("s") ? Integer.parseInt(periodStr.replaceAll("s", ""))*20 : Integer.parseInt(periodStr);
    }
    
    public static boolean isRIsign(Block sign){
        if (sign.getType() == Material.WALL_SIGN || sign.getType() == Material.SIGN_POST) {
            String line0 = ((Sign)sign).getLine(0).toLowerCase();
            String line1 = ((Sign)sign).getLine(1).toLowerCase();
            if(line1.contains("restockit") || line1.contains("restock it") || line1.contains("full chest") || line1.contains("full dispenser")) {
                return true;
            } else if(line0.contains("restockit") || line0.contains("restock it") || line0.contains("full chest") || line0.contains("full dispenser")) {
                Sign sgn = ((Sign)sign);
                sgn.setLine(3, sgn.getLine(2));
                sgn.setLine(2, sgn.getLine(1));
                sgn.setLine(1, sgn.getLine(0));
                sgn.setLine(0, "");
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
}
