//@author Chris Price (xCP23x)

package org.xcp23x.RestockIt;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

class RIschedule {
    
    //X,Y,Z coords of block, as well as the bukkit task ID are stored in hashmaps
    private static HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> sched = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>();
    
    public static void startSchedule(final Block block, int period, final int countTo, Server server) {
        if (getSched(block) != 0) {return;} //It's already running, don't start it again
        setSched(block, server.getScheduler().scheduleSyncRepeatingTask(RestockIt.plugin, new Runnable() {
            @Override
            public void run() {
                //If no sign is there, stop (the player may have removed it mid-count)
                if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
                    RIschedule.stopSchedule(block);
                    return;
                }
                //Get the sign state
                BlockState state = block.getState();
                
                //Stop the schedule when it's reached the countTo
                if (!"".equals(((Sign)state).getLine(3))) {
                    if (Integer.parseInt(((Sign)state).getLine(3)) >= countTo) {
                       RIschedule.stopSchedule(block);
                       return;
                    }
                }
                
                if(RIcheck.checkID(((Sign)state).getLine(3)) <= 0) {
                    ((Sign)state).setLine(3, "1"); //Set it to 1, used to initialise the sign, will start counting on next tick
                } else {
                    ((Sign)state).setLine(3, String.valueOf(Integer.parseInt(((Sign)state).getLine(3)) + 1)); //Adds 1 to the current value
                }
                state.update(); //Save changes to the sign
            }
            
        },0,period));
        if (getSched(block) == -1) {sched.remove(block.getX()).get(block.getY()).get(block.getZ());} //If it fails to start, remove entry from HashMap
    }
    
    public static void stopSchedule(Block block) {
        int i = getSched(block); //Get bukkit task ID
        if (i != 0) {  //If it's 0, it's not running
            RestockIt.plugin.getServer().getScheduler().cancelTask(i); //Cancel task
            sched.remove(block.getX()).get(block.getY()).get(block.getZ()); //Remove entry from HashMap
        }
    }
    
    private static int getSched(Block block) {
        try {
            return sched.get(block.getX()).get(block.getY()).get(block.getZ());
            //If it gives an exception, we need to initialise it with setSched
        }catch (Exception ex) {
            setSched(block, 0);
            return 0;
        }
    }
    
    private static void setSched(Block block, int value) {
        HashMap<Integer, Integer> a = new HashMap<Integer, Integer>(); //Define a HashMap
        HashMap<Integer, HashMap<Integer, Integer>> b = new HashMap<Integer, HashMap<Integer, Integer>>(); //Define a HashMap of a HashMap
        a.put(block.getZ(), value); //Put the value in a
        b.put(block.getY(), a); //Put a in b
        sched.put(block.getX(), b); //Put b in sched
    }
}