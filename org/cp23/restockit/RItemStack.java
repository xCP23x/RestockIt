//Copyright (C) 2011-2014 Chris Price (xCP23x)
//This software uses the GNU GPL v2 license
//See http://github.com/xCP23x/RestockIt/blob/master/README and http://github.com/xCP23x/RestockIt/blob/master/LICENSE for details

package org.cp23.restockit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

@XmlRootElement(name="itemStack")
@XmlType(propOrder={"typeName", "damage", "data", "amount", "displayName", "lore", "enchantMap"})
public class RItemStack {
    //Gives an ItemStack that can be serialized into XML
    //XML serializable variables
    private String typeName, displayName;
    private int amount, ticksPerItem, maxItems;
    private short damage;
    private byte data;      //This is deprecated, but will have to stay until there's actually a way to get the data
    private Map<String, Integer> enchantMap = new HashMap<>();
    private List<String> lore;
    
    //Transient variables
    private final transient RestockIt plugin = RestockIt.plugin;
    
    public RItemStack(){
        //ONLY called when restoring from XML
    }
    
    public RItemStack(ItemStack is){
        if(is==null) return;
        Material mat = is.getType();
        amount = is.getAmount();
        damage = is.getDurability();
        typeName = mat.name();
        data = is.getData().getData(); 
        lore = is.getItemMeta().getLore();
        displayName = is.getItemMeta().getDisplayName();
        ticksPerItem = 0;
        maxItems = is.getMaxStackSize();
        
        for(Enchantment enc : is.getEnchantments().keySet()){
            enchantMap.put(enc.getName(), is.getEnchantmentLevel(enc));
        }
    }
    
    public ItemStack getItemStack(){
        Material mat = Material.getMaterial(typeName);
        ItemStack is = new ItemStack(mat, amount, damage);
        is.setData(new MaterialData(mat, data));
        
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        im.setDisplayName(displayName);
        
        for(Map.Entry<String, Integer> entry : enchantMap.entrySet()){
            im.addEnchant(Enchantment.getByName(entry.getKey()), entry.getValue(), true);
        }
        
        return is;
    }
    
    public void setMaxItems(int n){
        maxItems = n;
    }
    
    public void setTicksPerItem(int n){
        ticksPerItem = n;
    }
    
    
    //XML Getters and Setters
    @XmlElement(name="type")
    public String getTypeName(){
        return typeName;
    }
    public void setTypeName(String s){
        typeName = s;
    }
    
    @XmlElement
    public String getDisplayName(){
        return displayName;
    }
    public void setDisplayName(String s){
        displayName = s;
    }
    
    @XmlElement
    public int getAmount(){
        return amount;
    }
    public void setAmount(int n){
        amount = n;
    }
    
    @XmlElement
    public short getDamage(){
        return damage;
    }
    public void setDamage(Short s){
        damage = s;
    }
    
    @XmlElement
    public byte getData(){
        return data;
    }
    public void setData(Byte b){
        data = b;
    }
    
    @XmlElement(name="enchantments")
    public Map<String, Integer> getEnchantMap(){
        return enchantMap;
    }
    public void setEnchantMap(Map<String, Integer> m){
        enchantMap = m;
    }
    
    @XmlElement
    public List<String> getLore(){
        return lore;
    }
    public void setLore(List<String> sl){
        lore = sl;
    }
}
