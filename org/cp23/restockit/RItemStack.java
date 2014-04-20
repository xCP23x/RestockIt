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
@XmlAccessorType(XmlAccessType.FIELD)
public class RItemStack {
    //Gives an ItemStack that can be serialized into XML
    
    //XML serializable variables
    //Use classes rather than primitive types so we can easily convert to string
    private String typeName, displayName;
    private Integer amount, ticksPerItem, maxAmount;
    private Short damage;
    private Byte data;      //This is deprecated, but there's no actual way to get/set data in bukkit
    @XmlTransient
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
        if(amount==null) return new ItemStack(Material.AIR); //It's an empty stack
        
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
    
    
    
    //Getters and setters for XML variables
    //Convert numerical types to String - allows null fields to stay empty
    public String getTypeName(){
        return typeName;
    }
    public void setTypeName(String s){
        typeName = s;
    }
    
    public String getAmount(){
        return (amount==null)? null: amount.toString();
    }
    public void setAmount(Integer n){
        amount = n;
    }
    
    public String getDamage(){
        return (damage==null)? null: damage.toString();
    }
    public void setDamage(Short s){
        damage = s;
    }
    
    public String getData(){
        return (data==null)? null: data.toString();
    }
    public void setData(Byte b){
        data = b;
    }
    
    public String getMaxAmount(){
        return (maxAmount==null)? null: maxAmount.toString();
    }
    public void setmaxAmount(Integer i){
        maxAmount = i;
    }
    
    public String getTicksPerItem(){
        return (ticksPerItem==null)? null: ticksPerItem.toString();
    }
    public void setTicksPerItem(Integer n){
        ticksPerItem = n;
    }
    
    public String getDisplayName(){
        return displayName;
    }
    public void setDisplayName(String s){
        displayName = s;
    }
    
    public List<String> getLore(){
        return lore;
    }
    public void setLore(List<String> sl){
        lore = sl;
    }
    
    @XmlElementWrapper(name="enchantments") //This allows it to disappear when empty (so an empty itemStack can collapse)
    public Map<String, Integer> getEnchantMap(){
        return (enchantMap.isEmpty())? null: enchantMap;
    }
    public void setEnchantMap(Map<String, Integer> m){
        enchantMap = m;
    }
}
