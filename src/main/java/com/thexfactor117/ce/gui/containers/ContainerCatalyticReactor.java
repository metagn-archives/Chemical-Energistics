package com.thexfactor117.ce.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.thexfactor117.ce.tiles.machines.TileCatalyticReactor;

public class ContainerCatalyticReactor extends Container
{
	private TileCatalyticReactor catalyticReactor;
	private int energyStored;
    
    public ContainerCatalyticReactor(EntityPlayer player, TileCatalyticReactor te)
    {
    	this.catalyticReactor = te;
 
        this.addSlotToContainer(new Slot(catalyticReactor, 1, 81, 34));
        this.addSlotToContainer(new Slot(catalyticReactor, 0, 41, 34));
        
        //Inventory
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18 + 1, 84 + i * 18));
            }
        }
        
        // Hotbar
        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18 + 1, 142));
        }
    }
    
    @Override
    public void addCraftingToCrafters(ICrafting crafting)
    {
    	super.addCraftingToCrafters(crafting);
    	
    	crafting.sendProgressBarUpdate(this, 0, this.catalyticReactor.storage.getEnergyStored());
    }
    
    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting crafting = (ICrafting)this.crafters.get(i);

            if (this.energyStored != this.catalyticReactor.storage.getEnergyStored())
            {
                crafting.sendProgressBarUpdate(this, 0, this.catalyticReactor.storage.getEnergyStored());
            }
        }
        
        this.energyStored = this.catalyticReactor.storage.getEnergyStored();
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotRaw)
    {
        ItemStack stack = null;
        Slot slot = (Slot) inventorySlots.get(slotRaw);
 
        if (slot != null && slot.getHasStack())
        {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();
 
            if (slotRaw < 3 * 9)
            {
                if (!mergeItemStack(stackInSlot, 3 * 9, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(stackInSlot, 0, 3 * 9, false))
            {
                return null;
            }
 
            if (stackInSlot.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        
        return stack;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return catalyticReactor.isUseableByPlayer(player);
    }
}
