package fr.reborned.effectgui.Tools;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public final class FastInvManager {
    private static final AtomicBoolean REGISTER = new AtomicBoolean(false);

    public static void register(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");
        if (REGISTER.getAndSet(true)) {
            throw new IllegalStateException("FastInv is already registered");
        } else {
            Bukkit.getPluginManager().registerEvents(new FastInvManager.InventoryListener(plugin), plugin);
        }
    }

    public static void closeAll() {
        Bukkit.getOnlinePlayers().stream().filter((p) -> {
            return p.getOpenInventory().getTopInventory().getHolder() instanceof FastInv;
        }).forEach(HumanEntity::closeInventory);
    }

    public static final class InventoryListener implements Listener {
        private final Plugin plugin;

        public InventoryListener(Plugin plugin) {
            this.plugin = plugin;
        }

        @EventHandler(
                priority = EventPriority.LOW
        )
        public void onInventoryClick(InventoryClickEvent e) {
            if (e.getInventory().getHolder() instanceof FastInv && e.getClickedInventory() != null) {
                FastInv inv = (FastInv)e.getInventory().getHolder();
                boolean wasCancelled = e.isCancelled();
                e.setCancelled(true);
                inv.handleClick(e);
                if (!wasCancelled && !e.isCancelled()) {
                    e.setCancelled(false);
                }
            }

        }

        @EventHandler
        public void onInventoryOpen(InventoryOpenEvent e) {
            if (e.getInventory().getHolder() instanceof FastInv) {
                FastInv inv = (FastInv)e.getInventory().getHolder();
                inv.handleOpen(e);
            }

        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent e) {
            if (e.getInventory().getHolder() instanceof FastInv) {
                FastInv inv = (FastInv)e.getInventory().getHolder();
                if (inv.handleClose(e)) {
                    Bukkit.getScheduler().runTask(this.plugin, () -> {
                        inv.open((Player)e.getPlayer());
                    });
                }
            }

        }

        @EventHandler
        public void onPluginDisable(PluginDisableEvent e) {
            if (e.getPlugin() == this.plugin) {
                FastInvManager.closeAll();
                FastInvManager.REGISTER.set(false);
            }

        }
    }
}