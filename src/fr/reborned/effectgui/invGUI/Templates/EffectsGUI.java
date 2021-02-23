package fr.reborned.effectgui.invGUI.Templates;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.reborned.effectgui.Main;
import fr.reborned.effectgui.Tools.EnumTools;
import fr.reborned.effectgui.Tools.FastInv;
import fr.reborned.effectgui.Tools.Fichier;
import fr.reborned.effectgui.Tools.ItemStacked;
import fr.reborned.effectgui.invGUI.InvGUI;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class EffectsGUI extends InvGUI {

    private Player player;
    private FastInv fastInv;
    private int Ligne;
    private Fichier fichier;
    private Main main;

    public EffectsGUI(Player player, Fichier fichier){
        this.player=player;
        this.fichier = new Fichier(fichier.getPath());
        this.Ligne = fichier.getLigneConf();
        this.fastInv = new FastInv(9*Ligne, fichier.getTitleConf());
        this.init();
    }

    @Override
    public void init() {
        for (ItemStacked itemStacked: fichier.getItemStackMenuConf()) {
            for (PotionEffect potionEffect : this.player.getActivePotionEffects()){
                if (potionEffect.getType().getName().equalsIgnoreCase(itemStacked.getItemStack().getItemMeta().getDisplayName())){
                    //itemStacked.getItemStack().addUnsafeEnchantment(Enchantment.DAMAGE_ALL,4);
                    ItemMeta itemMeta =itemStacked.getItemStack().getItemMeta();
                    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,2,true);
                    //itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    itemStacked.getItemStack().setItemMeta(itemMeta);
                    //TODO Voir les enchantements

                }
            }
            this.fastInv.setItem(itemStacked.getSlotID(), itemStacked.getItemStack());
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
                        if (PotionEffectType.getByName(c.getCommande().toUpperCase()) != null) {
                            if (this.player.hasPotionEffect(PotionEffectType.getByName(c.getCommande().toUpperCase())) && this.player.hasPermission(c.getPermission())) {
                                try {
                                    for (PotionEffect potionEffect : this.player.getActivePotionEffects()) {
                                        if (potionEffect.getType().equals(PotionEffectType.getByName((c.getCommande()).toUpperCase()))) {
                                            this.player.removePotionEffect(potionEffect.getType());
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else if (!this.player.hasPotionEffect(PotionEffectType.getByName(c.getCommande().toUpperCase())) && this.player.hasPermission(c.getPermission())) {
                                try {
                                    this.player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(c.getCommande().toUpperCase()), Integer.MAX_VALUE, fichier.getIntOfEffect("Iteminmenu." + c.getName()), true, true));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else
                            {
                                this.player.sendMessage("Vous n'avez pas les permissions suffisantes");
                            }
                        }else {
                            if (c.getName().equalsIgnoreCase(unEvent.getCurrentItem().getItemMeta().getDisplayName())) {
                                if (c.getCommande().equalsIgnoreCase(unEvent.getCurrentItem().getItemMeta().getDisplayName())) {

                                    try {
                                        for (PotionEffect potionEffect : this.player.getActivePotionEffects()) {
                                            this.player.removePotionEffect(potionEffect.getType());
                                        }
                                        for (int i = 0; i < getFastInventory().getInventory().getSize(); i++) {
                                            if (getFastInventory().getInventory().getItem(i) != null) {
                                                for (Enchantment enchantment : getFastInventory().getInventory().getItem(i).getEnchantments().keySet()) {
                                                    unEvent.getCurrentItem().getItemMeta().removeEnchant(enchantment);
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    String name = unEvent.getCurrentItem().getItemMeta().getDisplayName();
                                    this.player.sendMessage("Suis nous sur "+ name +" : "+ this.fichier.getLinkSocialNetwork("Iteminmenu."+name+"."));
                                }
                            }
                        }
                        unEvent.setCancelled(true);
                        this.player.closeInventory();
                        this.player.updateInventory();
                    });
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
