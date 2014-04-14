//Copyright (C) 2011-2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import javax.xml.bind.annotation.*;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class RCont {
    private final Block block;
    private InventoryHolder holder;
    private boolean isRCont;
    
    public RCont(Block bl){
        isRCont = false;
        block = bl;
        if(block instanceof InventoryHolder){
            holder = (InventoryHolder) bl;
            Inventory inv;
            RXml rx = RestockIt.rxml;
            isRCont = rx.isRCont(this);
        }
    }
    
    public boolean isRCont(){
        return isRCont;
    }
    
    
    @XmlElement
    public int getX(){
        return block.getX();
    }
    @XmlElement
    public int getY(){
        return block.getY();
    }
    @XmlElement
    public int getz(){
        return block.getZ();
    }
}
