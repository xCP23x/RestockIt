//Copyright (C) 2011-2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.BlockIterator;

public class CommandListener implements CommandExecutor {
    private final RestockIt plugin = RestockIt.plugin;
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("restockit")){
            switch(args[0].toLowerCase()){
                case "addcont":
                    //TEST CODE
                    if(sender instanceof Player){
                        Player player = (Player) sender;
                        BlockIterator bi = new BlockIterator(player);
                        while(bi.hasNext()){
                            Block block = bi.next();
                            
                            if(block.getState() instanceof InventoryHolder){
                                RestockIt.rxml.addToList(new RCont(block));
                                RestockIt.rxml.save("containers.xml");
                                plugin.debug("Added container");
                                break;
                            } else if(block.getType().isSolid()){
                                //Player could be inside a non-solid block (e.g. torch), or it could be air
                                break;
                            }
                        }
                    }
                    //END OF TEST CODE
                    break;
                case "save":
                    //TEST CODE
                    RestockIt.rxml.save("containers.xml");
                    break;
                case "load":
                    //TEST CODE
                    try{
                        RestockIt.rxml.load("containers.xml");
                    } catch(RXml.RXmlException e){}
                    break;
                
                    
                case "help":
                    
                    break;
                default:
                    
                    break;
            }
            
            
        }
        
        
        return true;
    }
}
