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
    //Class for RestockIt containers
    
    private Block block;
    private InventoryHolder ivh;
    private Material item;
    private int maxItems;
    private int fillRate;
    
    public RIcont(Block cont){
        block = cont;
        //do stuff
        
    }
    
    public Block getSign(){
        
        
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
