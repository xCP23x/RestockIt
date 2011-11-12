package org.xcp23x.RestockIt;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RIpl extends PlayerListener {
    public static RestockIt plugin;
    public RIpl(RestockIt instance) {
        plugin = instance;
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block chest = event.getClickedBlock();
            if((chest.getType() == Material.CHEST) || (chest.getType() == Material.DISPENSER)) {
                int signx = chest.getX();
                int signy = chest.getY() + 1;
                int signz = chest.getZ();
                Block sign = chest.getWorld().getBlockAt(signx,signy,signz);
                if((sign.getType() == Material.WALL_SIGN) || sign.getType() == Material.SIGN_POST) {
                    Material item = Material.AIR;
                    Inventory inventory = null;
                    if (chest.getType() == Material.CHEST) {inventory = ((Chest)chest.getState()).getInventory();}
                    else {inventory = ((Dispenser)chest.getState()).getInventory();}
                    String line1 = ((Sign)sign.getState()).getLine(1);
                    String line2 = ((Sign)sign.getState()).getLine(2);
                    String itemID = line2;
                    short damage = -1;
                    if(line2.contains(":")) {
                        itemID = line2.split(":")[0];
                        String split = line2.split(":")[1];
                        damage = Short.parseShort(split);
                    }
                    if(RIbl.checkCommand(line1)){
                        item = getMaterial(itemID); 
                        if(item != Material.AIR) {
                            fillChest(item, inventory, damage);
                        }
                    }
                }
            }
        }
    }
    
    public Material getMaterial(String line2) {
        int ID = RIbl.checkID(line2);
        if(ID == 1) {
            return Material.getMaterial(Integer.parseInt(line2));
        }else if(ID == 2) {
            return Material.getMaterial(line2);
        }else return Material.AIR;
    }
    
    public void fillChest(Material item, Inventory inv, Short damage) {
        int stackSize = item.getMaxStackSize();
        int invSize = inv.getSize();
        ItemStack stack = new ItemStack(item, stackSize);
        int x = 0;
        if(damage <= 255) {
            if(damage >= 0) stack.setDurability(damage);
            while(x < invSize) {
               inv.setItem(x, stack);
                x++;
            }
        }
    }
}