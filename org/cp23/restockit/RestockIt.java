//Copyright (C) 2011-2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RestockIt extends JavaPlugin {
    
    public static final Logger log = Logger.getLogger("Minecraft");
    public static RestockIt plugin;
    public static RXml rxml;
    
    //Config variables
    private static boolean debugEnabled;
    public static boolean opsBypass;
    public static List<String> blacklist, singleConts, doubleConts, dispensers;
        
    @Override
    public void onEnable(){
        plugin = this;
        rxml = new RXml();
        loadConfig();
        getCommand("restockit").setExecutor(new CommandListener());
        plugin.getServer().getPluginManager().registerEvents(new EventListener(), plugin);
    }
    
    @Override
    public void onDisable(){
        //TODO: Save changes
    }
    
    private void loadConfig(){   
        
        //To add new entries to old config files, we need to use copyDefaults(true)
        //This breaks any formatting due to YAML parsing, so comments must be defined in a header:
        
        String header = (
            "RestockIt configuration file\n"
            +"See http://dev.bukkit.org/bukkit-plugins/RestockIt for more info \n\n"
            +"blacklist: list of items that cannot be put into a RestockIt container\n"
            +"singleContainers: list of containers that cannot be doubled up\n"
            +"doubleContainers: list of containers that CAN be doubled up\n"
            +"dispensers: list of containers that can dispense items\n\n"
            +"opsBypassBlacklist: if no permissions plugin is present, can ops bypass the blacklist? (true/false)\n"
            +"debug : Enable debug messages - may spam console (true/false)\n"
        );
        
        FileConfiguration config = this.getConfig();
        
        config.options().copyDefaults(true);
        config.options().copyHeader(true);
        config.options().header(header);
        this.saveConfig();
        
        //Load debug setting
        debugEnabled = config.getBoolean("debug");
        if(debugEnabled) log.info("[MuchCraft] Such debug - Much enabled!");
        
        //Load config
        blacklist = config.getStringList("blacklist");
        singleConts = config.getStringList("singleContainers");
        doubleConts = config.getStringList("doubleContainers");
        dispensers = config.getStringList("dispensers");
        
        opsBypass = config.getBoolean("opsBypassBlacklist");
    }
    
    public void debug(String msg){
        if(debugEnabled){
            RestockIt.log.info("[RestockIt][DEBUG]: "+msg);
        }
    }
}
