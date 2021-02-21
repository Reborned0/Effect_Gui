package fr.reborned.effectgui.invGUI.Templates;

import fr.reborned.effectgui.Main;
import fr.reborned.effectgui.Tools.EnumTools;
import fr.reborned.effectgui.Tools.FastInv;
import fr.reborned.effectgui.Tools.Fichier;
import fr.reborned.effectgui.Tools.ItemStacked;
import fr.reborned.effectgui.invGUI.InvGUI;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Struct;
import java.util.Arrays;
import java.util.Optional;

public class EffectsGUI extends InvGUI {

    private Player player;
    private FastInv fastInv;
    private int Ligne;
    private Fichier fichier;
    private Main main;

    public EffectsGUI(Player player){
        this.player=player;
        this.fichier = new Fichier(main.getDataFolder(),"config.yml");
        this.Ligne = fichier.getLigneConf();
        this.fastInv = new FastInv(9*Ligne, fichier.getTitleConf());
        this.init();
    }

    @Override
    public void init() {
        for (ItemStacked itemStacked: fichier.getItemStackMenuConf()) {
            this.fastInv.setItem(itemStacked.getSlotID(),itemStacked.getItemStack());
        }

        this.fastInv.addClickHandler((unEvent) -> {
            if (unEvent.getAction().name().contains("HOTBAR")) {
                unEvent.setCancelled(true);
            } else {
                unEvent.setCancelled(true);
                if (!unEvent.getCurrentItem().getType().isTransparent()) {

                    Optional<EnumTools> optional = Arrays.stream(EnumTools.values()).filter((en) -> {
                        return unEvent.getCurrentItem().getItemMeta().getDisplayName().equals(en.getName());
                    }).findAny();
                    optional.ifPresent((c) -> {
                        if (this.player.hasPotionEffect(PotionEffectType.getByName(c.getCommande())) && this.player.hasPermission(c.getPermission())){
                            this.player.getActivePotionEffects().remove(PotionEffectType.getByName(c.getCommande()));
                        }else{
                            this.player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(c.getCommande()),Integer.MAX_VALUE,fichier.getIntOfEffect("hotbar"+c.getName()),true,true));
                        }
                    });
                    this.player.closeInventory();
                    this.player.updateInventory();
                }
            }
        });
    }

    @Override
    public void openInv() {
        this.fastInv.open(this.player);
    }

    @Override
    public InvGUI get() {
        return this;
    }

    @Override
    public FastInv getFastInventory() {
        return this.fastInv;
    }
}
