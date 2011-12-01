//@author Chris Price (xCP23x)

package org.xcp23x.RestockIt;

import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RestockIt extends JavaPlugin {
    
    private final RIpl playerListener = new RIpl(this);
    private final RIbl blockListener = new RIbl(this);
    static final Logger log = Logger.getLogger("Minecraft");
    public static RestockIt plugin;
    
    @Override
    public void onEnable() {
        plugin = this;
        PluginDescriptionFile pdfFile = this.getDescription();
        RestockIt.log.info("[RestockIt] v" + pdfFile.getVersion() + " Started");
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DISPENSE, blockListener, Event.Priority.Normal, this);
    }
        @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        RestockIt.log.info("[RestockIt] v" + pdfFile.getVersion() + " Stopped");
    }
    
}