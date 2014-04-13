//Copyright (C) 2011-2012 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ContProp extends RestockIt {
    
    private Block cont;
    private Boolean isScheduled;
    private Material itemNotScheduled;
    private Short damageNotScheduled;
    private HashMap<String, String> scheduledItems = new HashMap<String, String>(); //First string is material/damage, second is other details.
    
    
    public ContProp (Block block){
        cont = block;
        loadProps();
    }
    
    public Boolean isScheduled(){
        return isScheduled;
    }
    
    public Material getItemNotScheduled(){
        return itemNotScheduled;
    }
    
    public Short getDamageNotScheduled(){
        return damageNotScheduled;
    }
    
    public Block getBlock(){
        return cont;
    }
    
    //Add some way to return schedules
    
    
    public void setNotScheduled(Material item, Short damage){
        isScheduled = false;
        itemNotScheduled = item;
        damageNotScheduled = damage;
        saveProps();
    }
    
    public void addScheduled(Material item, Short damage, int fillrate, int maxitems, int currentitems){
        isScheduled = true;
        itemNotScheduled = null;
        damageNotScheduled = null;
        
        if(getSchedule(item, damage) != null){
            //A schedule already exists for this item/damage, so remove it
            scheduledItems.remove(item + "/" + damage);
        }
        //Make a new schedule
        scheduledItems.put(item + "/" + damage, fillrate + "/" + maxitems + "/" + currentitems);
        saveProps();
    }
    
    public void removeScheduled(Material item, Short damage){
        if(getSchedule(item, damage) != null){
            //A schedule already exists for this item/damage, so remove it
            scheduledItems.remove(item + "/" + damage);
        }
        saveProps();
    }
    
    public void removeAll(Material item, Short damage){
        if(getChestProps(cont) != null){
            deleteChestProps(cont);
        }
    }
    
    private void loadProps(){
        
        //Syntax for chests: x;y;z;isScheduled(bool);itemNotScheduled;damageNotScheduled;-item1,fillrate1,maxitems1,currentitems1,damage1,-item2......
        
        String props = getChestProps(cont);
        if(props != null){
            isScheduled = Boolean.parseBoolean(props.split(";")[3]);
            
            if(!isScheduled){
                itemNotScheduled = Material.getMaterial(Integer.parseInt(props.split(";")[4]));
                damageNotScheduled = Short.parseShort(props.split(";")[4]);
            } else {
                itemNotScheduled = null;
                damageNotScheduled = null;
                int n = props.indexOf("-");
                for(int i = 1; i < n; i++){
                    String str = props.split("-")[i];
                    Material mat = Material.getMaterial(Integer.parseInt(str.split(",")[0]));
                    
                    String fillrate = str.split(",")[1];
                    String maxitems = str.split(",")[2];
                    String currentitems = str.split(",")[3];
                    String damage = str.split(",")[4];
                    
                    scheduledItems.put(mat + "/" + damage, fillrate + "/" + maxitems + "/" + currentitems);
                }
            }
        }
    }
    
    private void saveProps(){
        String props;
        props = cont.getX() + ";" + cont.getY() + ";" + cont.getZ() + ";";
        
        if(isScheduled){
            props = props + "true;0;0;-";
            int n = scheduledItems.size();
            List<String> keys = new ArrayList<String>(scheduledItems.keySet()); //Make a list so we can get the keys numerically
            
            for(int i=0; i<n; i++){
                String key = keys.get(i);
                String mat = key.split("/")[0];
                String damage = key.split("/")[1];
                
                String details = scheduledItems.get(key);
                int fillrate = getFillRateFromSchedule(details);
                int maxitems = getMaxItemsFromSchedule(details);
                int currentitems = getCurrentItemsFromSchedule(details);
                
                props = props + mat + "," + fillrate + "," + maxitems + "," + currentitems + "," + damage + ",-";
            }
        } else {
            props = props + "false;" + itemNotScheduled + ";" + damageNotScheduled + ";";
        }
        setChestProps(cont, props);
    }
    
    private String getSchedule(Material item, Short damage){
        String str = item.getId() + "/" + damage;
        if(scheduledItems.containsKey(str)){
            return scheduledItems.get(str);
        }
        return null;
    }
    
    private int getFillRateFromSchedule(String str){
        return Integer.parseInt(str.split("/")[0]);
    }
    
    private int getMaxItemsFromSchedule(String str){
        return Integer.parseInt(str.split("/")[1]);
    }
    
    private int getCurrentItemsFromSchedule(String str){
        return Integer.parseInt(str.split("/")[2]);
    }
    
}
