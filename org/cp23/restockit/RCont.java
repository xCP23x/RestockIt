//Copyright (C) 2011-2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.*;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

@XmlRootElement(name="container")
@XmlType(propOrder={"x", "y", "z", "worldUID", "rISList"})
@XmlSeeAlso(RItemStack.class)
public class RCont {
    //Variables to be saved in XML (MUST have public set methods)
    private int x, y, z;
    private UUID worldUID;
    private final List<RItemStack> rISList = new ArrayList<>();
    
    //Transient variables
    private transient Block block;
    
    //Other transient variables unrelated to XML
    private final transient RXml rx = RestockIt.rxml;
    private final transient RestockIt plugin = RestockIt.plugin;
    
    
    public RCont(){
        //ONLY called when restoring from XML
        xmlToTransient();
    }
    
    public RCont(Block bl){
        block = bl;
        transientToXml();
    }
    
    public boolean isRCont(){
        return rx.isRCont(this);
    }
    
    
    private void xmlToTransient(){
        //Translates all XML-saved variables to transient variables
        World world = plugin.getServer().getWorld(worldUID);
        block = world.getBlockAt(x, y, z);
        
        //Replace all items in inventory
        if(block instanceof InventoryHolder){
            Inventory inv = ((InventoryHolder) block.getState()).getInventory();
            for(int i=0; i<rISList.size(); i++){
                inv.clear();
                inv.setItem(i, rISList.get(i).getItemStack());
            }
        }
    }
    
    private void transientToXml(){
        //Translates all relevant transient variables to XML-saved variables
        x=block.getX(); y=block.getY(); z=block.getZ();
        worldUID = block.getWorld().getUID();
        if(block.getState() instanceof InventoryHolder){
            InventoryHolder ivh = (InventoryHolder) block.getState();
            ItemStack[] itemStackArr = ivh.getInventory().getContents();
            for(ItemStack is:itemStackArr){
                rISList.add(new RItemStack(is));
            }
        }
    }
    
    
    public void addItemStack(ItemStack is){
        //TEST CODE (See RestockIt.java:39)
        ((InventoryHolder)block.getState()).getInventory().addItem(is);
        transientToXml();
    }
    
    
    //getter methods for XML variables
    @XmlElement()
    public int getX(){
        return x;
    }
    @XmlElement
    public int getY(){
        return y;
    }
    @XmlElement
    public int getz(){
        return z;
    }
    @XmlElement
    public UUID getWorldUID(){
        return worldUID;
    }
    @XmlElement(name="itemStack")
    public List<RItemStack> getrISList(){
        return rISList;
    }
}
