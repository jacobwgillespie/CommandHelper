
package com.laytonsmith.abstraction;

import com.laytonsmith.abstraction.blocks.MCMaterial;
import com.laytonsmith.core.nbt.NbtFactory.NbtCompound;

import java.util.Map;

/**
 *
 * 
 */
public interface MCItemStack extends AbstractionObject{
    public MCMaterialData getData();
    public short getDurability();
    public int getTypeId();
    public void setDurability(short data);
    public void addEnchantment(MCEnchantment e, int level);
    public void addUnsafeEnchantment(MCEnchantment e, int level);
    public Map<MCEnchantment, Integer> getEnchantments();
    public void removeEnchantment(MCEnchantment e);
    public MCMaterial getType();
    public void setTypeId(int type);
    
    public int maxStackSize();
    
    public int getAmount();

    public void setData(int data);
	
	public boolean hasItemMeta();
	public MCItemMeta getItemMeta();
	public void setItemMeta(MCItemMeta im);

    public NbtCompound getNbt();
}
