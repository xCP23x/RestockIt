//@author Chris Price (xCP23x)

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

public class RIplayer extends PlayerListener {
    public static RestockIt plugin;
    public RIplayer(RestockIt instance) {
        plugin = instance;
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block chest = event.getClickedBlock();
            checkSign(chest);
        }
    }
    
    public static void checkSign(Block chest) {
        if((chest.getType() == Material.CHEST) || (chest.getType() == Material.DISPENSER)) {
            //m = sign below chest
            //p = sign above chest
            Block m = chest.getWorld().getBlockAt(chest.getX(),chest.getY()-1,chest.getZ());
            Block p = chest.getWorld().getBlockAt(chest.getX(),chest.getY()+1,chest.getZ());
            
            if(p.getType() == Material.WALL_SIGN || p.getType() == Material.SIGN_POST) {
                String line1 =((Sign)p.getState()).getLine(1);
                RIschedule.startSchedule(p, 20, 20, plugin.getServer());
                if(RIcheck.checkCommand(line1)) {
                    readSign(chest, p, line1);
                    return; //We don't want to know about the sign below the chest, so return
                }
            }
            if (m.getType() == Material.WALL_SIGN || m.getType() == Material.SIGN_POST) {
                String line1 =((Sign)m.getState()).getLine(1);
                if(RIcheck.checkCommand(line1)) {readSign(chest, m, line1);}
                //RestockIt.startTimer(m, 1000, 20, plugin.getServer());
            }
        }
    }    
            
    public static void readSign(Block chest, Block sign, String line1){
        if(sign == null) {return;} //Stops an exception
        Material item = Material.AIR; //If it's still air by the end of it, we know it hasn't been changed
        Inventory inventory = null;
        
        //getInventory() needs to be called for a chest or a dispenser separately
        if (chest.getType() == Material.CHEST) {inventory = ((Chest)chest.getState()).getInventory();}
        else {inventory = ((Dispenser)chest.getState()).getInventory();}
        
        String line2 = ((Sign)sign.getState()).getLine(2);
        String itemID = line2; //itemID is the one that is used, line2 is used for split.
        short damage = -1; //If it stays at -1, there's no damage value
        
        if(line2.contains(":")) {          //If it's got a :, get damage value
            itemID = line2.split(":")[0];
            String split = line2.split(":")[1];
            damage = Short.parseShort(split);
        }
        
        if(RIcheck.checkCommand(line1)){ //Check if it's a RestockIt sign
            if (line2.equalsIgnoreCase("Incinerator")) {
                item = Material.AIR; //Setting it to air will empty the container (see fillChest())
            } else {item = getMaterial(itemID);}
            fillChest(item, inventory, damage);
        }
    }
    
    public static Material getMaterial(String line2) {
        int id = RIcheck.checkID(line2); //Get what type of ID it is
        //No need to try/catch, it's already been done by checkID() so we shouldn't have any exceptions here
        if(id == 1) { //It's an int ID already
            return Material.getMaterial(Integer.parseInt(line2)); //Make it an int (from a string), return it as a Material
            
        }else if(id == 2) {
            return Material.getMaterial(line2); //Return it as a Material
            
        }else {return Material.AIR;} //Return air if it's invalid
    }
    
    public static void fillChest(Material item, Inventory inv, Short damage) {
        if (item != Material.AIR) { //Trying to fill a chest with air will crash the client (but not the server)
            int invSize = inv.getSize();  //Get inventory size (chest size != dispenser)
            int stackSize = item.getMaxStackSize(); //Get stack size (snowball stack != stone)
            ItemStack stack = new ItemStack(item, stackSize); //Make the ItemStack
            if(damage >= 0) {stack.setDurability(damage);} //Incorporate damage (remember, -1 = no damage value)
            
            for(int x=0; x<invSize; x++) {inv.setItem(x, stack);}//Fille the chest
            
        } else {inv.clear();} //Clear inventory if air is requested (see, there WAS a reason for setting it as air)
    }
}