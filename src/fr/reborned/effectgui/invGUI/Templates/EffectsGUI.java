package fr.reborned.effectgui.invGUI.Templates;

import fr.reborned.effectgui.Main;
import fr.reborned.effectgui.Tools.*;
import fr.reborned.effectgui.invGUI.InvGUI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


import java.util.Arrays;
import java.util.Optional;

public class EffectsGUI extends InvGUI {

    private Player player;
    private FastInv fastInv;
    private int Ligne;
    private Fichier fichier;
    private Main main;

    public EffectsGUI(Player player, Fichier fichier){
        this.player=player;
        this.fichier = new Fichier(fichier.getPath());
        this.Ligne = fichier.getLigneConf("Menu","nblignes");
        this.fastInv = new FastInv(9*Ligne, fichier.getTitleConf("Menu","title"));
        this.init();
    }

    @Override
    public void init() {
        for (ItemStacked itemStacked: fichier.getItemStackMenuConf("ItemInMenu")) {
            for (PotionEffect potionEffect : this.player.getActivePotionEffects()){
                if (potionEffect.getType().getName().toLowerCase().contains(itemStacked.getItemStack().getItemMeta().getDisplayName().toLowerCase())){
                    enchantItemInventory(itemStacked);
                }
            }
            if (this.player.getAllowFlight() && itemStacked.getItemStack().getItemMeta().getDisplayName().toUpperCase().contains(EnumTools.FLY.getCommande())){
                enchantItemInventory(itemStacked);
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
                            if (this.player.hasPotionEffect(PotionEffectType.getByName(c.getCommande().toUpperCase()))) {
                                try {
                                    for (PotionEffect potionEffect : this.player.getActivePotionEffects()) {
                                        if (potionEffect.getType().equals(PotionEffectType.getByName((c.getCommande()).toUpperCase()))) {
                                            this.player.removePotionEffect(potionEffect.getType());
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else if (!this.player.hasPotionEffect(PotionEffectType.getByName(c.getCommande().toUpperCase()))) {
                                try {
                                    this.player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(c.getCommande().toUpperCase()), Integer.MAX_VALUE, fichier.getIntOfEffect("ItemInMenu." + c.getName()), true, true));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else
                            {
                                this.player.sendMessage(ChatColor.RED+"Vous n'avez pas les permissions suffisantes");
                            }
                        }else {
                            if (c.getCommande().equalsIgnoreCase(unEvent.getCurrentItem().getItemMeta().getDisplayName())) {

                                if (unEvent.getCurrentItem().getType().equals(Material.SKULL_ITEM)){
                                    String name = unEvent.getCurrentItem().getItemMeta().getDisplayName();
                                    Messages messages = this.fichier.getMessage("ItemInMenu",c.getCommande());


                                    TextComponent msgBefore = new TextComponent(messages.getBeforeMessage());
                                    TextComponent msgHover = new TextComponent(messages.getHoverMessage());
                                    TextComponent msgAfter = new TextComponent(messages.getAfterMessage());

                                    msgHover.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, this.fichier.getLinkSocialNetwork("ItemInMenu."+ c.getCommande())));
                                    msgHover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.fichier.getMessageHover("ItemInMenu." + c.getCommande(),"messageHover")).create()));

                                    msgBefore.addExtra(msgHover);
                                    msgBefore.addExtra(msgAfter);

                                    this.player.spigot().sendMessage(msgBefore);

                                }


                                if (unEvent.getCurrentItem().getItemMeta().getDisplayName().contains("SPEED") || unEvent.getCurrentItem().getItemMeta().getDisplayName().contains("JUMP") || unEvent.getCurrentItem().getItemMeta().getDisplayName().contains("DUMP")) {

                                    try {
                                        for (PotionEffect potionEffect : this.player.getActivePotionEffects()) {
                                            this.player.removePotionEffect(potionEffect.getType());
                                        }
                                        if (this.player.getAllowFlight()){
                                            this.player.setAllowFlight(false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (unEvent.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(EnumTools.FLY.getName()) && this.player.hasPermission(EnumTools.FLY.getPermission())){
                                    this.player.setAllowFlight(!this.player.getAllowFlight());
                                }else if (unEvent.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(EnumTools.FLY.getName()) && !this.player.hasPermission(EnumTools.FLY.getPermission())){
                                    this.player.sendMessage(fichier.getmessInsufisantePerm());
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

    private void enchantItemInventory(ItemStacked itemStacked){
        ItemMeta itemMeta =itemStacked.getItemStack().getItemMeta();
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStacked.getItemStack().setItemMeta(itemMeta);
    }

}
