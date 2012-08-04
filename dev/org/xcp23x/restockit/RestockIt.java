//Copyright (C) 2011-2012 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.xcp23x.restockit;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RestockIt extends JavaPlugin {
    
    static final Logger log = Logger.getLogger("Minecraft");
    public static RestockIt plugin;
    private static boolean debugEnabled = false;
    private static boolean schedDebugEnabled = false;
    
    //Prepare config for delayed chests and command chests
    private FileConfiguration chests = null;
    private File chestsFile = null;
    
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
        //Save any delayed or command chests
        this.saveChests();
    }
    
    public void loadChests(){
        //If it's not been loaded, load it
        if(chestsFile == null){
            chestsFile = new File(getDataFolder(), "chests.yml");
        }
        chests = YamlConfiguration.loadConfiguration(chestsFile);
    }
    
    public FileConfiguration getChests(){
        if(chests == null){
            this.loadChests();
        }
        return chests;
    }
    
    public void saveChests(){
        if(chests == null || chestsFile == null){
            return;
        }
        try{
            getChests().save(chestsFile);
        } catch (IOException ex){
            RestockIt.log.severe("[RestockIt] Could not save chest data");
        }
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
