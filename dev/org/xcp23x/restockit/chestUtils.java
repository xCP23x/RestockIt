//@author Chris Price (xCP23x)

package org.xcp23x.restockit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class chestUtils {
    
    public static int getCurrentItems(Material item, Block sign) {
        return 0;
    }
    
    public static Inventory getInventory(Block container){
        Material mat = container.getType();
        if(mat == Material.CHEST){
            return ((Chest)container.getState()).getInventory();
        }
        else if(mat == Material.DISPENSER){
            return ((Dispenser)container.getState()).getInventory();
        }
        else return null;
    }
    
    public static boolean isSignAboveChest(Block chest) {
        if((chest.getType() == Material.CHEST) || (chest.getType() == Material.DISPENSER)) {
            Block sign = signUtils.getSignAboveChest(chest);
            if(sign.getType() == Material.WALL_SIGN || sign.getType() == Material.SIGN_POST) {
                if(signUtils.isRIsign(sign)) return true;
            }
        }
        return false;
    }
    
    public static boolean isSignBelowChest(Block chest) {
        if((chest.getType() == Material.CHEST) || (chest.getType() == Material.DISPENSER)) {
            Block sign = signUtils.getSignBelowChest(chest);
            if (sign.getType() == Material.WALL_SIGN || sign.getType() == Material.SIGN_POST) {
                if(signUtils.isRIsign(sign)) return true;
            }
        }
        return false;
    }
    
    public static boolean isRIchest(Block chest) {
        if(isSignBelowChest(chest) == false && isSignAboveChest(chest) == false) return false;
        else return true;
    }
    
    public static boolean isAlreadyRIchest(Block sign) {
        //Check below the sign
        Block block = sign.getWorld().getBlockAt(sign.getX(), sign.getY()-1, sign.getZ());
        int x = block.getX(), y = block.getY(), z = block.getZ();
        if (block.getType() == Material.CHEST || block.getType() == Material.DISPENSER) {
            if (!signUtils.isRIsign(block.getWorld().getBlockAt(x, y-2, z))) {
                return false;
            } else {
                return true;
            }
        }
        
        //Check above the sign
        block = sign.getWorld().getBlockAt(sign.getX(), sign.getY()+1, sign.getZ());
        if (block.getType() == Material.CHEST || block.getType() == Material.DISPENSER) {
            if (!signUtils.isRIsign(block.getWorld().getBlockAt(x, y+2, z))) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    
    public static void fillChest(Block chest){
        Block sign = signUtils.getSignFromChest(chest);
        Material mat = signUtils.getMaterial(sign);
        Inventory inv = getInventory(chest);
        Short damage = signUtils.getDamage(sign);
        if (mat != Material.AIR) { //Trying to fill a chest with air will crash the client (but not the server)
            int stacks = inv.getSize();  //Get inventory size (chest size != dispenser)
            int stackSize = mat.getMaxStackSize(); //Get stack size (snowball stack != stone)
            ItemStack stack = new ItemStack(mat, stackSize); //Make the ItemStack
            if(damage >= 0) {stack.setDurability(damage);} //Incorporate damage (remember, -1 = no damage value)
            
            for(int x=0; x<stacks; x++) {inv.setItem(x, stack);}//Fill the chest
            
        } else {inv.clear();} //Clear inventory if air is requested (see, there WAS a reason for setting it as air)
    }
}
