package fr.reborned.effectgui.Tools;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemStacked extends ItemStack{
    private ItemStack itemStack;
    private int slotID;

    public ItemStacked(ItemStack itemStack, int slotID){
        this.itemStack=itemStack;
        this.slotID=slotID;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }
}
