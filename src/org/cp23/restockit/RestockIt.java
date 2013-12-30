//Copyright (C) 2011-2013 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class RestockIt extends JavaPlugin {
    
    static final Logger log = Logger.getLogger("Minecraft");
    public static RestockIt plugin;
    private static boolean debugEnabled = false;
    private static boolean schedDebugEnabled = false;
    private static List<String> blacklist, singleContainers, doubleContainers, dispensers;
    
    @Override
    public void onEnable(){
        plugin = this;
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        
        //Prepare the config
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        debugEnabled = this.getConfig().getBoolean("debugMessages");
        schedDebugEnabled = this.getConfig().getBoolean("MOARdebug");
        if(debugEnabled) log.info("[RestockIt] Basic debug messages enabled");
        if(schedDebugEnabled) log.info("[RestockIt] Scheduler debug messages enabled");
        
        //Load config
        blacklist = RestockIt.plugin.getConfig().getStringList("blacklist");
        singleContainers = RestockIt.plugin.getConfig().getStringList("singleContainers");
        doubleContainers = RestockIt.plugin.getConfig().getStringList("doubleContainers");
        dispensers = RestockIt.plugin.getConfig().getStringList("dispensers");
        
        //Check config for errors (i.e. force any errors to be logged to console)
        isInList(Material.AIR, "blacklist");
        isInList(Material.AIR, "singleContainers");
        isInList(Material.AIR, "doubleContainers");
        isInList(Material.AIR, "dispensers");
    }
    
    @Override
    public void onDisable(){
    }
    
    public static boolean isContainer(Material mat){
        return isInList(mat, "dispensers") || isInList(mat, "singleContainers") || isInList(mat, "doubleContainers");
    }
    
    public static boolean isInList(Material mat, String listname){
        List<String> list;
        switch(listname){
            case "blacklist":
                list = blacklist;
                break;
            case "singleContainers":
                list = singleContainers;
                break;
            case "doubleContainers":
                list = doubleContainers;
                break;
            case "dispensers":
                list = dispensers;
                break;
            default:
                return false;
        }
        
        int size = list.size();
        for(int x = 0; x<size; x++) {
            String blItem = list.get(x);
            if(SignUtils.getType(blItem) <= 0) {
                RestockIt.log.warning("[RestockIt] Error in " + listname + " list: " + blItem + "not recognised - Ignoring");
            } else if (mat.getId() == SignUtils.getType(blItem)){
                return true;
            }
        }
        return false;
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
