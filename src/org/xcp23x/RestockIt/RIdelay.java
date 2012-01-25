//@author Chris Price (xCP23x)

package org.xcp23x.RestockIt;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

public class RIdelay {
    
    public static RestockIt plugin;
    public RIdelay(RestockIt instance) {plugin = instance;}
    
    public static int getPeriod(String line3) {
        if(!line3.contains("/") || !line3.contains(",")) {return -1;}
        return Integer.parseInt(line3.split(",")[1]);
    }
    
    public static int getMaxBlocks(String line3) {
        if(!line3.contains("/") || !line3.contains(",")) {return -1;}
        return Integer.parseInt(line3.split("/")[1].split(",")[0]);
    }
    
    public static void setMaxBlocks(Block sign, int maxBlocks) {
        BlockState state = sign.getState();
        String line3 = ((Sign)state).getLine(3);
        if(!line3.contains("/") || !line3.contains(",")) {return;}
        String currentBlocks = line3.split("/")[0];
        String period = line3.split(",")[1];
        ((Sign)state).setLine(3, (currentBlocks + "/" + maxBlocks + "," + period));
        state.update();
    }
    
    public static void setCurrentBlocks(Block sign, int currentBlocks) {
        BlockState state = sign.getState();
        String line3 = ((Sign)state).getLine(3);
        if(!line3.contains("/") || !line3.contains(",")) {return;}
        String maxBlocks = line3.split("/")[1].split(",")[0];
        String period = line3.split(",")[1];
        ((Sign)state).setLine(3, (currentBlocks + "/" + maxBlocks + "," + period));
        state.update();
    }
    
    public static int getCurrentBlocks(String line3) {
        if(!line3.contains("/") || !line3.contains(",")) {return -1;}
        return Integer.parseInt(line3.split("/")[0]);
    }
    
    public static String prepareLines(String[] lines) {
        String line2 = lines[2];
        int itemID = RIcheck.checkID(line2);
        Material item = Material.getMaterial(itemID);
        int maxBlocks = 0;
        String period = "";
        String line3 = lines[3].toLowerCase().replaceAll(" ", "");
        
        if (!line3.contains("items") && !line3.contains("stacks")) return null;
        
        if(line3.contains("stacks")) {
            String str = line3.split("stacks")[0];
            int maxStacks = 0;
            try {
                maxStacks = Integer.parseInt(str);
            } catch (Exception e) {
                RestockIt.log.info("Couldn't parse int");
            }
            maxBlocks = (item == Material.AIR) ? 0
                    :(maxStacks * item.getMaxStackSize()) + (maxStacks % Material.getMaterial(itemID).getMaxStackSize());
        }
        
        if(line3.contains("items")) {
            String str = line3.split("items")[0];
            try {
                maxBlocks = (item == Material.AIR) ? 0 : Integer.parseInt(str);
            } catch (Exception e) {
                RestockIt.log.info("Couldn't parse int");
            }
        }
        
        period = line3.split(",")[1].replaceAll("s", "");
        return (0 + "/" + maxBlocks + "," + period);
    }
}