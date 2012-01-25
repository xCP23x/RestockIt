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
    public RIplayer(RestockIt instance) {plugin = instance;}
    
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
                String line3 =((Sign)p.getState()).getLine(3);
                if(RIcheck.checkCommand(line1)) {
                    readSign(chest, p, line1);
                    RIschedule.startSchedule(p, RIdelay.getPeriod(line3)); //Start the schedule for this sign
                    return; //We don't want to know about the sign below the chest, so return
                }
            }
            if (m.getType() == Material.WALL_SIGN || m.getType() == Material.SIGN_POST) {
                String line1 =((Sign)m.getState()).getLine(1);
                String line3 =((Sign)m.getState()).getLine(3);
                if(RIcheck.checkCommand(line1)) {
                    readSign(chest, m, line1);
                    RIschedule.startSchedule(m, RIdelay.getPeriod(line3)); //Start the schedule for this sign
                }
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
        String line3 = ((Sign)sign.getState()).getLine(3);
        String itemID = line2; //itemID is the one that is used, line2 is used for split.
        int maxItems = 0;
        int timePeriod = 0;
        short damage = -1; //If it stays at -1, there's no damage value
        
        if(line2.contains(":")) {          //If it's got a :, get damage value
            itemID = line2.split(":")[0];
            String split = line2.split(":")[1];
            damage = Short.parseShort(split);
        }
        
        if(line3.contains(",")) {
            if (line3.toLowerCase().contains("stacks")) {
                String mI = line3.split("stacks")[0];
                int maxStacks = 0;
                mI = mI.replaceAll(" ","");
                try {maxStacks = Integer.parseInt(mI);}
                catch(Exception e) {
                    RestockIt.log.info("Failed");
                }
                maxItems = item == Material.AIR ? 0
                        :(maxStacks * getMaterial(itemID).getMaxStackSize()) + (maxStacks % getMaterial(itemID).getMaxStackSize());
                
            } else if (line3.toLowerCase().contains("items")) {
                String mI = line3.split("items")[0];
                mI = mI.replaceAll(" ","");
                try {
                    maxItems = item == Material.AIR ? 0 : Integer.parseInt(mI);
                }
                catch(Exception e) {RestockIt.log.info("Failed");} 
            }
            
            String period = line3.split(",")[1];
            period = period.toLowerCase().replaceAll(" ", "").replaceAll("s", "");
            try {
                timePeriod = Integer.parseInt(period);
            }
            catch(Exception e) {RestockIt.log.info("Failed");}
        }
        
        if(RIcheck.checkCommand(line1)){ //Check if it's a RestockIt sign
            if (line2.equalsIgnoreCase("Incinerator")) {
                item = Material.AIR; //Setting it to air will empty the container (see fillChest())
            } else {item = getMaterial(itemID);}
            fillChest(item, inventory, damage, maxItems);
        }
    }
    
    public static Material getMaterial(String line2) {
        int id = RIcheck.checkID(line2); //Get what type of ID it is
        //No need to try/catch, it's already been done by checkID() so we shouldn't have any exceptions here
        if(id > 0){
            return Material.getMaterial(id);
        }else {return Material.AIR;} //Return air if it's invalid
    }
    
    public static void fillChest(Material item, Inventory inv, Short damage, int maxItems) {
        if (item != Material.AIR) { //Trying to fill a chest with air will crash the client (but not the server)
            int stacks = inv.getSize();  //Get inventory size (chest size != dispenser)
            int stackSize = item.getMaxStackSize(); //Get stack size (snowball stack != stone)
            ItemStack stack = new ItemStack(item, stackSize); //Make the ItemStack
            if(damage >= 0) {stack.setDurability(damage);} //Incorporate damage (remember, -1 = no damage value)
            
            int spares = 0;
            
            if (maxItems != 0) {
                stacks = maxItems / stackSize;
                spares = maxItems % stackSize;
                stacks = (stacks> inv.getSize()) ? inv.getSize() : stacks;
            }
            
            for(int x=0; x<stacks; x++) {inv.setItem(x, stack);}//Fill the chest
            
            ItemStack extra = new ItemStack(item, spares);
            inv.addItem(extra);
            
        } else {inv.clear();} //Clear inventory if air is requested (see, there WAS a reason for setting it as air)
    }
} 