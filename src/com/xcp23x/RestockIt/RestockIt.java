package com.xcp23x.RestockIt;

/*import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;*/
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
    //pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Event.Priority.Normal, this);
    
/*    if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
        RestockIt.log.info("[RestockIt] PermissionsEX found");
    } else {
        RestockIt.log.info("[RestockIt] PermissionsEX not found - Using op status");
    } */
    
    
    
/*    if (!new File("plugins/FullChest2").exists()) {
      FullChest2.log.info("[FullChest 2] Config directory does not exist. Creating new folder...");
      new File("plugins/FullChest2").mkdir();
      FullChest2.log.info("[FullChest 2] Config directory created.");
    }

    if (!new File("plugins/FullChest2/config.txt").exists()) {
      try {
        new File("plugins/FullChest2/config.txt").createNewFile();
        try {
          FileWriter fw = new FileWriter("plugins/FullChest2/config.txt");
          BufferedWriter bw = new BufferedWriter(fw);
          bw.write("Dispenser=0");
          bw.close();
          fw.close();
        }
        catch (Exception ex) {
          FullChest2.log.info("[FullChest 2] Error making config!");
        }
      } catch (IOException e) {
      }

    }

    try
    {
      FileReader fr = new FileReader("plugins/FullChest2/config.txt");
      BufferedReader br = new BufferedReader(fr);

      if (br.readLine().equalsIgnoreCase("dispenser=1"))
      {
        pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Event.Priority.Normal, this);

        FullChest2.log.info("[FullChest 2] Dispensers are enabled (MAY BE LAGGY)");
      }
      else {
        FullChest2.log.info("[FullChest 2] Dispensers are disabled");
      }

      br.close();
      fr.close();
    }
    catch (Exception ex) {
      FullChest2.log.info("[FullChest 2] Unable to read config - Dispensers Disabled");
    }
 */
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
