package fr.reborned.effectgui.Listener;

import fr.reborned.effectgui.Main;
import fr.reborned.effectgui.Tools.Fichier;
import fr.reborned.effectgui.invGUI.Templates.EffectsGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

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
        if (fichier.isWorldOk(e.getPlayer())){
         ajoutHotBar(e.getPlayer(), fichier.getItemHotbar(), fichier.getSlotID("hotbar.item1"));
        }
    }

    @EventHandler
    public void onInterract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack itemStack = e.getItem();

        if (itemStack != null){
            if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK) && itemStack.getType().equals(Material.SUGAR)){
                EffectsGUI effectsGUI = new EffectsGUI(player);
                effectsGUI.openInv();
            }
        }


    }


    private void ajoutHotBar(Player Joueur, ItemStack itemStack, int slotID){
        Joueur.getInventory().setItem(slotID,itemStack);
        Joueur.updateInventory();
    }
}
