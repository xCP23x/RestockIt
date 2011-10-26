package com.xcp23x.RestockIt;

import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RestockIt extends JavaPlugin {
    static final Logger log = Logger.getLogger("Minecraft");
    public static RestockIt plugin;
    public boolean dispenserEnabled = false;
    //Listeners:
    private final RIplayer playerListener = new RIplayer(this);
    private final RIblock blockListener = new RIblock(this);

    @Override
    public void onDisable() {
        RestockIt.log.info("[RestockIt] Stopped");
    }

    @Override
  public void onEnable()
  {
    plugin = this;
    RestockIt.log.info("[RestockIt] Started");
    PluginManager pm = this.getServer().getPluginManager();
    pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
    pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Event.Priority.Normal, this);
  }
        
    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true; 
        } catch (Exception e) {}
        return false;
  }
    
    public boolean isValidId(int id) {
        return (id != 0) && (Material.getMaterial(id) != null);
  }
    
    public boolean isValidName(String name) {
        return (name != null) && (Material.getMaterial(name) != null) && (!"AIR".equals(name));
    }
}
