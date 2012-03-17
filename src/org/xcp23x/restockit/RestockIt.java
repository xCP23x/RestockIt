//@author Chris Price (xCP23x)
//Nothing interesting here, just setting up listeners and announcing that it's started

package org.xcp23x.restockit;

import java.util.logging.Logger;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class RestockIt extends JavaPlugin {
    
    static final Logger log = Logger.getLogger("Minecraft");
    public static RestockIt plugin;
    
    @Override
    public void onEnable(){
        plugin = this;
        getServer().getPluginManager().registerEvents(new listeners(), this);
        PluginDescriptionFile pdfFile = this.getDescription();
        RestockIt.log.info("[RestockIt] v" + pdfFile.getVersion() + " Started");
    }
    
    @Override
    public void onDisable(){
        PluginDescriptionFile pdfFile = this.getDescription();
        RestockIt.log.info("[RestockIt] v" + pdfFile.getVersion() + " Stopped");
    }
}
