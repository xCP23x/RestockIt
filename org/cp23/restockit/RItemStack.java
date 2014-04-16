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
@XmlType(propOrder={"typeName", "amount", "damage", "data", "maxAmount", "ticksPerItem", "displayName", "lore", "enchantMap"})
public class RItemStack {
    //Gives an ItemStack that can be serialized into XML
    //XML serializable variables
    private String typeName, displayName;
    private int amount, ticksPerItem, maxAmount;
    private short damage;
    private byte data;      //This is deprecated, there's no actual way to get/set data in bukkit
    private Map<String, Integer> enchantMap = new HashMap<>();
    private List<String> lore;
    
    //Transient variables
    private final transient RestockIt plugin = RestockIt.plugin;
    
    public RItemStack(){
        //ONLY called when restoring from XML
    }
    
    public RItemStack(ItemStack is){
        if(is==null) return;
        typeName = is.getType().name();
        amount = is.getAmount();
        damage = is.getDurability();
        data = is.getData().getData();
        lore = is.getItemMeta().getLore();
        displayName = is.getItemMeta().getDisplayName();
        ticksPerItem = 0;
        maxAmount = is.getMaxStackSize();
        
        for(Enchantment enc : is.getEnchantments().keySet()){
            enchantMap.put(enc.getName(), is.getEnchantmentLevel(enc));
        }
    }
    
    public ItemStack getItemStack(){
        if(typeName==null) return null;
        Material mat = Material.getMaterial(typeName);
            
        ItemStack is = new ItemStack(mat, amount, damage);
        is.setData(new MaterialData(mat, data));
        
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        im.setDisplayName(displayName);
        
        for(Map.Entry<String, Integer> entry : enchantMap.entrySet()){
            im.addEnchant(Enchantment.getByName(entry.getKey()), entry.getValue(), true);
        }
        is.setItemMeta(im);
        return is;
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
    public void setDamage(short s){
        damage = s;
    }
    
    @XmlElement
    public byte getData(){
        return data;
    }
    public void setData(byte b){
        data = b;
    }
    
    @XmlElement
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
    
    @XmlElement
    public int getTicksPerItem(){
        return ticksPerItem;
    }
    public void setTicksPerItem(int n){
        ticksPerItem = n;
    }
    
    @XmlElement
    public int getMaxAmount(){
        return maxAmount;
    }
    public void setmaxAmount(int i){
        maxAmount = i;
    }
}
