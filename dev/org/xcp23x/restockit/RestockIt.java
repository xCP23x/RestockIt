//@author Chris Price (xCP23x)

package org.xcp23x.restockit;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class RestockIt extends JavaPlugin {
    
    static final Logger log = Logger.getLogger("Minecraft");
    public static RestockIt plugin;
    
    @Override
    public void onEnable(){
        plugin = this;
        getServer().getPluginManager().registerEvents(new listeners(), this);
        
        //Prepare the config
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        
        //Check config for errors
        List<String> blacklist = plugin.getConfig().getStringList("blacklist");
        int size = blacklist.size();
        for(int x = 0; x<size; x++) {
            if(signUtils.getType(blacklist.get(x)) < 0) {
                RestockIt.log.warning("[RestockIt] Error in blacklist: " + blacklist.get(x) + "not recognised - Ignoring");
            }
        }
    }
    
    @Override
    public void onDisable(){
    }
}
