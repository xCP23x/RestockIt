//Copyright (C) 2011-2012 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.xcp23x.restockit;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

class RIcont {
    //Class for RestockIt containers (any references to cont mean container)
    
    private Block cont;
    private InventoryHolder ivh;
    private Material item;
    private int maxItems;
    private int fillRate;
    
    public RIcont(Block block){
        if(block == null){
            RestockIt.debug("RIcont received null block on init");
            return;
        }
        cont = block;
        //Make it automatically check if it's a restockit chest, if it is, get details  (ivh, item, maxItems, etc)
        //If it's not, fail silently, maybe set a bool value to false
    }
    
    public Boolean isRIcont(){
        if(cont.getType() == Material.CHEST || cont.getType() == Material.DISPENSER){
            RIsign sign = getRIsign();
            //If it's a RestockIt sign, it must be a RI cont
            if(sign != null && sign.isRIsign()) return true;
            
            //TODO: Code for loading data from saved chests
            
        }
        return false;
    }
    
    public RIsign getRIsign(){
        //Check above cont
        Block block = cont.getWorld().getBlockAt(cont.getX(), cont.getY() +1, cont.getZ());
        RIsign sign = new RIsign(block);
        if(sign.isRIsign()) return sign;
        
        //Check below cont
        block = cont.getWorld().getBlockAt(cont.getX(), cont.getY() -1, cont.getZ());
        sign = new RIsign(block);
        if(sign.isRIsign()) return sign;
        
        return null;
    }
    
    public Block getDoubleChest(){
        
        
        return null;
    }
    
    public Boolean isDoubleChest(){
        
        
        return false;
    }
    
    public int getCurrentAmount(Material item){
        int items = 0;
        Inventory inv = ivh.getInventory();
        int stacks = inv.getSize(); //How many stacks?
        for(int x=0; x<stacks; x++) {  //Check each stack
            ItemStack stack = inv.getItem(x);
            if(stack == null) continue; //Continue to next stack
            if(stack.getType() == item) {
                items = items + stack.getAmount();
            }
        }
        return items;
    }

}
