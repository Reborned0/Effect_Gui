package fr.reborned.effectgui.Tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class FastInv implements InventoryHolder {
    private final Map<Integer, Consumer<InventoryClickEvent>> itemHandlers;
    private Set<Consumer<InventoryOpenEvent>> openHandlers;
    private Set<Consumer<InventoryCloseEvent>> closeHandlers;
    private Set<Consumer<InventoryClickEvent>> clickHandlers;
    private Predicate<Player> closeFilter;
    private Inventory inventory;

    public FastInv(int size) {
        this(size, InventoryType.CHEST.getDefaultTitle());
    }

    public FastInv(int size, String title) {
        this(size, InventoryType.CHEST, title);
    }

    public FastInv(InventoryType type) {
        this(type, type.getDefaultTitle());
    }

    public FastInv(InventoryType type, String title) {
        this(0, type, title);
    }

    private FastInv(int size, InventoryType type, String title) {
        this.itemHandlers = new HashMap();
        if (type == InventoryType.CHEST && size > 0) {
            this.inventory = Bukkit.createInventory(this, size, title);
        } else {
            this.inventory = Bukkit.createInventory(this, (InventoryType)Objects.requireNonNull(type, "type"), title);
        }

        if (this.inventory.getHolder() != this) {
            throw new IllegalStateException("Inventory holder is not FastInv, found: " + this.inventory.getHolder());
        }
    }

    protected void onOpen(InventoryOpenEvent event) {
    }

    protected void onClick(InventoryClickEvent event) {
    }

    protected void onClose(InventoryCloseEvent event) {
    }

    public void addItem(ItemStack item) {
        this.addItem(item, (Consumer)null);
    }

    public void addItem(ItemStack item, Consumer<InventoryClickEvent> handler) {
        int slot = this.inventory.firstEmpty();
        if (slot >= 0) {
            this.setItem(slot, item, handler);
        }

    }

    public void setItem(int slot, ItemStack item) {
        this.setItem(slot, item, (Consumer)null);
    }

    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> handler) {
        this.inventory.setItem(slot, item);
        if (handler != null) {
            this.itemHandlers.put(slot, handler);
        } else {
            this.itemHandlers.remove(slot);
        }

    }

    public void setItems(int slotFrom, int slotTo, ItemStack item) {
        this.setItems(slotFrom, slotTo, item, (Consumer)null);
    }

    public void setItems(int slotFrom, int slotTo, ItemStack item, Consumer<InventoryClickEvent> handler) {
        for(int i = slotFrom; i <= slotTo; ++i) {
            this.setItem(i, item, handler);
        }

    }

    public void setItems(int[] slots, ItemStack item) {
        this.setItems(slots, item, (Consumer)null);
    }

    public void setItems(int[] slots, ItemStack item, Consumer<InventoryClickEvent> handler) {
        int[] var7 = slots;
        int var6 = slots.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            int slot = var7[var5];
            this.setItem(slot, item, handler);
        }

    }

    public void removeItem(int slot) {
        this.inventory.clear(slot);
        this.itemHandlers.remove(slot);
    }

    public void removeItems(int... slots) {
        int[] var5 = slots;
        int var4 = slots.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            int slot = var5[var3];
            this.removeItem(slot);
        }

    }

    public void setCloseFilter(Predicate<Player> closeFilter) {
        this.closeFilter = closeFilter;
    }

    public void addOpenHandler(Consumer<InventoryOpenEvent> openHandler) {
        if (this.openHandlers == null) {
            this.openHandlers = new HashSet();
        }

        this.openHandlers.add(openHandler);
    }

    public void addCloseHandler(Consumer<InventoryCloseEvent> closeHandler) {
        if (this.closeHandlers == null) {
            this.closeHandlers = new HashSet();
        }

        this.closeHandlers.add(closeHandler);
    }

    public void addClickHandler(Consumer<InventoryClickEvent> clickHandler) {
        if (this.clickHandlers == null) {
            this.clickHandlers = new HashSet();
        }

        this.clickHandlers.add(clickHandler);
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
    }

    public int[] getBorders() {
        int size = this.inventory.getSize();
        return IntStream.range(0, size).filter((i) -> {
            return size < 27 || i < 9 || i % 9 == 0 || (i - 8) % 9 == 0 || i > size - 9;
        }).toArray();
    }

    public int[] getCorners() {
        int size = this.inventory.getSize();
        return IntStream.range(0, size).filter((i) -> {
            return i % 9 == 0 && (i < 10 || i > size - 19) || (i - 1) % 9 == 0 && (i < 2 || i > size - 9) || (i - 7) % 9 == 0 && (i < 9 || i > size - 3) || (i - 8) % 9 == 0 && (i < 18 || i > size - 11);
        }).toArray();
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    void handleOpen(InventoryOpenEvent e) {
        this.onOpen(e);
        if (this.openHandlers != null) {
            this.openHandlers.forEach((c) -> {
                c.accept(e);
            });
        }

    }

    boolean handleClose(InventoryCloseEvent e) {
        this.onClose(e);
        if (this.closeHandlers != null) {
            this.closeHandlers.forEach((c) -> {
                c.accept(e);
            });
        }

        return this.closeFilter != null && this.closeFilter.test((Player)e.getPlayer());
    }

    void handleClick(InventoryClickEvent e) {
        this.onClick(e);
        if (this.clickHandlers != null) {
            this.clickHandlers.forEach((c) -> {
                c.accept(e);
            });
        }

        Consumer<InventoryClickEvent> clickConsumer = (Consumer)this.itemHandlers.get(e.getRawSlot());
        if (clickConsumer != null) {
            clickConsumer.accept(e);
        }

    }
}