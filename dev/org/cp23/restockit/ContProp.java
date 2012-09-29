//Copyright (C) 2011-2012 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.xcp23x.restockit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class ContProp extends RestockIt {
    
    private Block cont;
    private Boolean isScheduled;
    private Material itemNotScheduled;
    private Short damageNotScheduled;
    private HashMap<Material, String> scheduledItems = new HashMap<Material, String>(); //String = fillrate1/maxitems1
    
    
    public ContProp (Block block){
        cont = block;
        loadProps();
    }
    
    public void loadProps(){
        
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
                    
                    scheduledItems.put(mat, fillrate + "/" + maxitems + "/" + currentitems + "/" + damage);
                }
            }
        }
    }
    
    public void saveProps(){
        String props;
        props = cont.getX() + ";" + cont.getY() + ";" + cont.getZ() + ";";
        
        if(isScheduled){
            props = props + "true;0;0;-";
            int n = scheduledItems.size();
            List<Material> keys = new ArrayList<Material>(scheduledItems.keySet()); //Make a list so we can get the keys numerically
            
            for(int i=0; i<n; i++){
                Material mat = keys.get(0);
                String str = scheduledItems.get(mat);
                String fillrate = str.split("/")[0];
                String maxitems = str.split("/")[1];
                String currentitems = str.split("/")[2];
                String damage = str.split("/")[3];
                
                props = props + mat.getId() + "," + fillrate + "," + maxitems + "," + currentitems + "," + damage + ",-";
            }
        } else {
            props = props + "false;" + itemNotScheduled + ";" + damageNotScheduled + ";";
        }
    }
    
    
}
