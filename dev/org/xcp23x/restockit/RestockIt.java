//Copyright (C) 2011-2012 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.xcp23x.restockit;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class RestockIt extends JavaPlugin {
    
    static final Logger log = Logger.getLogger("Minecraft");
    public static RestockIt plugin;
    static boolean debugEnabled = false;
    static boolean schedDebugEnabled = false;
    
    @Override
    public void onEnable(){
        plugin = this;
        getServer().getPluginManager().registerEvents(new listeners(), this);
        
        //Prepare the config
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        debugEnabled = this.getConfig().getBoolean("debugMessages");
        schedDebugEnabled = this.getConfig().getBoolean("MOARdebug");
        if(debugEnabled) log.info("[RestockIt] Basic debug messages enabled");
        if(schedDebugEnabled) log.info("[RestockIt] Scheduler debug messages enabled");
        
        //Check config for errors
        List<String> blacklist = plugin.getConfig().getStringList("blacklist");
        int size = blacklist.size();
        for(int x = 0; x<size; x++) {
            if(signUtils.getType(blacklist.get(x)) < 0) {
                RestockIt.log.warning("[RestockIt] Error in blacklist: " + blacklist.get(x) + " not recognised - Ignoring");
            }
        }
    }
    
    @Override
    public void onDisable(){
    }
    
    public static void debug(String msg){
        if(debugEnabled == true){
            RestockIt.log.info("[RestockIt][DEBUG]: "+msg);
        }
    }
    
    public static void debugSched(String msg){
        if(schedDebugEnabled == true){
            RestockIt.log.info("[RestockIt][SCHEDULER-DEBUG]: " +msg);
        }
    }
}
