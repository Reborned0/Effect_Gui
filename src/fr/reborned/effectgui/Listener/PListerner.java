package fr.reborned.effectgui.Listener;

import fr.reborned.effectgui.Main;
import fr.reborned.effectgui.Tools.Fichier;
import fr.reborned.effectgui.invGUI.Templates.EffectsGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import javax.swing.*;

public class PListerner implements Listener {
    Main main;
    Fichier fichier;
    Location location;

    public PListerner(Main main) {
        this.main=main;
        fichier = new Fichier(main.getDataFolder(),"config.yml");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        safePlayer(e.getPlayer());
        ajoutHotBar(e.getPlayer(), fichier.getItemHotbar(), fichier.getSlotID("Hotbarmenu.Item"));
      /*  if (fichier.isWorldOk(e.getPlayer())){

        }*/
    }

    @EventHandler
    public void onInterract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack itemStack = e.getItem();


        if (itemStack != null){
            if ((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && itemStack.equals(fichier.getItemHotbar())){
                EffectsGUI effectsGUI = new EffectsGUI(player,fichier);
                effectsGUI.openInv();
            }
        }

    }
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e){
        e.setCancelled(fichier.getfoodLevelChangeConf());
    }

    @EventHandler
    public void onFalling(EntityDamageEvent e){
        e.setCancelled(fichier.getFallDamage());
    }

    @EventHandler
    public void onDropEvent(PlayerDropItemEvent e){
        e.setCancelled(fichier.getDropItems());
    }

    private void safePlayer(Player p){
        for (PotionEffect potionEffect : p.getActivePotionEffects()){
            p.removePotionEffect(potionEffect.getType());
        }
        p.setFoodLevel(fichier.getFoodLevel());
        p.setHealth(fichier.getHealthLevel());
    }

    private void ajoutHotBar(Player Joueur, ItemStack itemStack, int slotID){
        Joueur.getInventory().setItem(slotID,itemStack);
        Joueur.updateInventory();
    }
}
