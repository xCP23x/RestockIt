package com.xcp23x.RestockIt;

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
      if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
          Block block = event.getClickedBlock();
          
      if (block.getType() == Material.CHEST) {
        Chest chest = (Chest)block.getState();
        Inventory inv = chest.getInventory();
        Block targetBlock = block.getWorld().getBlockAt(block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1, block.getLocation().getBlockZ());
   
 // Signs below chests not implemented        
 //       if ((targetBlock.getType() != Material.WALL_SIGN) && (targetBlock.getType() != Material.SIGN_POST)) {
 //       targetBlock = block.getWorld().getBlockAt(block.getLocation().getBlockX(), block.getLocation().getBlockY() - 1, block.getLocation().getBlockZ());
 //       }
        
        if ((targetBlock.getType() == Material.WALL_SIGN) || (targetBlock.getType() == Material.SIGN_POST)) {
          String[] sign_text = ((Sign)targetBlock.getState()).getLines();
          String item_data = sign_text[2];
          String item_ID = item_data;
          String item_damage = "0";
          
          if (item_data.contains(":")) {
            item_ID = item_data.split(":")[0];
            item_damage = item_data.split(":")[1];
          }

          if (((sign_text[1].equalsIgnoreCase("Full Chest")) || (sign_text[1].equalsIgnoreCase("RestockIt")) || (sign_text[1].equalsIgnoreCase("Restock It"))) && (plugin.isInteger(item_ID))) {
            int itemID = Integer.parseInt(item_ID);
            
            if (plugin.isValidId(itemID)) {
               ItemStack item = new ItemStack(itemID, Material.getMaterial(itemID).getMaxStackSize());
            
                if ((item_data.contains(":")) && (plugin.isInteger(item_damage)) &&          
                  (Short.parseShort(item_damage) < 255)) {
                 item.setDurability(Short.parseShort(item_damage));
                }
              for (int x = 0; x < inv.getSize(); x++) inv.setItem(x, item);
            }
          } else if ((sign_text[1].equalsIgnoreCase("Full Chest")) || (sign_text[1].equalsIgnoreCase("RestockIt")) || (sign_text[1].equalsIgnoreCase("Restock It"))) {
                if (plugin.isValidName(item_ID)) {
                    Material mat = Material.getMaterial(item_ID);
                    ItemStack item = new ItemStack(mat, mat.getMaxStackSize());
            
                    if ((item_data.contains(":")) && (plugin.isInteger(item_damage)) &&          
                       (Short.parseShort(item_damage) < 255)) {
                       item.setDurability(Short.parseShort(item_damage));
                    }
                 for (int x = 0; x < inv.getSize(); x++) inv.setItem(x, item);
            }
          }
        }
        
        //Do the same for a dispenser
      } else if (block.getType() == Material.DISPENSER){
            Dispenser dispenser = (Dispenser)block.getState();
            Inventory inv = dispenser.getInventory();
            Block targetBlock = block.getWorld().getBlockAt(block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1, block.getLocation().getBlockZ());
        
//            if ((targetBlock.getType() != Material.WALL_SIGN) && (targetBlock.getType() != Material.SIGN_POST)) {
//            targetBlock = block.getWorld().getBlockAt(block.getLocation().getBlockX(), block.getLocation().getBlockY() - 1, block.getLocation().getBlockZ());
//            }
        
            if ((targetBlock.getType() == Material.WALL_SIGN) || (targetBlock.getType() == Material.SIGN_POST)) {
                String[] sign_text = ((Sign)targetBlock.getState()).getLines();
                String item_data = sign_text[2];
                String item_ID = item_data;
                String item_damage = "0";
          
            if (item_data.contains(":")) {
                item_ID = item_data.split(":")[0];
                item_damage = item_data.split(":")[1];
          }

            if (((sign_text[1].equalsIgnoreCase("Full Dispenser")) || (sign_text[1].equalsIgnoreCase("RestockIt")) || (sign_text[1].equalsIgnoreCase("Restock It"))) && (plugin.isInteger(item_ID))) {
                int itemID = Integer.parseInt(item_ID);
            
                if (plugin.isValidId(itemID)) {
                    ItemStack item = new ItemStack(itemID, Material.getMaterial(itemID).getMaxStackSize());
            
                    if ((item_data.contains(":")) && (plugin.isInteger(item_damage)) &&          
                        (Short.parseShort(item_damage) < 255)) {
                        item.setDurability(Short.parseShort(item_damage));
                        }
                    for (int x = 0; x < inv.getSize(); x++) inv.setItem(x, item);
                    }
          } else if ((sign_text[1].equalsIgnoreCase("Full Dispenser")) || (sign_text[1].equalsIgnoreCase("RestockIt")) || (sign_text[1].equalsIgnoreCase("Restock It"))) {
                if (plugin.isValidName(item_ID)) {
                    Material mat = Material.getMaterial(item_ID);
                    ItemStack item = new ItemStack(mat, mat.getMaxStackSize());
            
                    if ((item_data.contains(":")) && (plugin.isInteger(item_damage)) &&          
                       (Short.parseShort(item_damage) < 255)) {
                       item.setDurability(Short.parseShort(item_damage));
                    }
                 for (int x = 0; x < inv.getSize(); x++) inv.setItem(x, item);
            }
          }
        }
      }
    }          
  }
}