//Copyright (C) 2011-2012 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.xcp23x.restockit;

import org.bukkit.block.Block;

class RIsign {
    //Class for RestockIt signs
    private Block sign;
    
    public RIsign(Block block) {
        sign = block;
        //Do stuff
    }
    
    public Boolean isRIsign(){
        
        return false;
    }
    
    
}
