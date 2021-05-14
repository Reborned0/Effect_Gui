package fr.reborned.effectgui.invGUI.Templates;

import fr.reborned.effectgui.Main;
import fr.reborned.effectgui.Tools.*;
import fr.reborned.effectgui.invGUI.InvGUI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class EffectsGUI extends InvGUI {

    private Player player;
    private FastInv fastInv;
    private int Ligne;
    private Fichier fichier;

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
                if (potionEffect.getType().getName().toLowerCase().contains(ChatColor.stripColor(itemStacked.getItemStack().getItemMeta().getDisplayName().toLowerCase()))){
                    enchantItemInventory(itemStacked);
                }
            }
            if (this.player.getAllowFlight() && ChatColor.stripColor(itemStacked.getItemStack().getItemMeta().getDisplayName().toUpperCase()).contains(EnumTools.FLY.getCommande())){
                enchantItemInventory(itemStacked);
            }
            this.fastInv.setItem(itemStacked.getSlotID(), itemStacked.getItemStack());
        }

        if (this.fichier.getItem("Menu.Item") != null) {

            for (Integer integer : this.fastInv.getBorders()) {
                ItemStack itemStack = this.fichier.getItem("Menu.Item");
                this.fastInv.setItem(integer, itemStack);
            }
        }


        this.fastInv.addClickHandler((unEvent) -> {
            if (unEvent.getAction().name().contains("HOTBAR")) {
                unEvent.setCancelled(true);
            } else {
                unEvent.setCancelled(true);
                if (Arrays.stream(this.fastInv.getBorders()).noneMatch(value -> {return value == unEvent.getSlot();}))
                if (!unEvent.getCurrentItem().getType().isTransparent()) {

                    Optional<EnumTools> optional = Arrays.stream(EnumTools.values()).filter((en) -> {
                        return ChatColor.stripColor(unEvent.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(en.getCommande());
                    }).findAny();
                    optional.ifPresent((c) -> {
                        if (PotionEffectType.getByName(c.getCommande().toUpperCase()) != null) {

                            AmplierGUI amplierGUI = new AmplierGUI(this.player, itemUniqueWithName(unEvent.getCurrentItem()),this.fichier);

                            if (PotionEffectType.getByName(c.getCommande().toUpperCase())!= null){
                                amplierGUI.openInv();
                                return;
                            }

                            if (c.getCommande().equalsIgnoreCase(ChatColor.stripColor(unEvent.getCurrentItem().getItemMeta().getDisplayName()))) {

                                if (unEvent.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
                                    String name = unEvent.getCurrentItem().getItemMeta().getDisplayName();
                                    Messages messages = this.fichier.getMessage("ItemInMenu", c.getCommande());


                                    TextComponent msgBefore = new TextComponent(messages.getBeforeMessage());
                                    TextComponent msgHover = new TextComponent(messages.getHoverMessage());
                                    TextComponent msgAfter = new TextComponent(messages.getAfterMessage());

                                    msgHover.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, this.fichier.getLinkSocialNetwork("ItemInMenu." + c.getCommande())));
                                    msgHover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.fichier.getMessageHover("ItemInMenu." + c.getCommande(), "messageHover")).create()));

                                    msgBefore.addExtra(msgHover);
                                    msgBefore.addExtra(msgAfter);

                                    this.player.spigot().sendMessage(msgBefore);

                                }
                            }
                            else{
                                this.player.sendMessage(ChatColor.RED+"Vous n'avez pas les permissions suffisantes");
                            }
                        }else {


                            if (c.getCommande().equalsIgnoreCase(ChatColor.stripColor(unEvent.getCurrentItem().getItemMeta().getDisplayName()))) {

                                if (unEvent.getCurrentItem().getType().equals(Material.SKULL_ITEM)){
                                    String name = unEvent.getCurrentItem().getItemMeta().getDisplayName();

                                    Messages messages = this.fichier.getMessage("ItemInMenu",c.getCommande());

                                    if (messages.getBeforeMessage().length()>0) {
                                        TextComponent msgBefore = new TextComponent(messages.getBeforeMessage());
                                        TextComponent msgAfter = new TextComponent(messages.getAfterMessage());
                                        if (!messages.getHoverMessage().equals("")) {
                                            TextComponent msgHover = new TextComponent(messages.getHoverMessage());
                                            msgHover.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, this.fichier.getLinkSocialNetwork("ItemInMenu." + c.getCommande())));
                                            String hovered =this.fichier.getMessageHover("ItemInMenu." + c.getCommande(),"messageHover");
                                            if (hovered.equals("")) {
                                                msgHover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hovered).create()));
                                            }
                                            msgBefore.addExtra(msgHover);
                                        } else {
                                            messages.setHoverMessage(this.fichier.getLinkSocialNetwork("ItemInMenu." + c.getCommande()));
                                            TextComponent msgHover = new TextComponent(messages.getHoverMessage());
                                            msgBefore.addExtra(msgHover);
                                        }
                                        msgBefore.addExtra(msgAfter);

                                        this.player.spigot().sendMessage(msgBefore);
                                    }else {
                                        this.player.sendMessage(ChatColor.RED +""+ ChatColor.BOLD+"Link Dead");
                                    }

                                }


                                if (ChatColor.stripColor(unEvent.getCurrentItem().getItemMeta().getDisplayName()).toUpperCase().equals(EnumTools.DUMP.getCommande())) {

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
                                if (ChatColor.stripColor(unEvent.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(EnumTools.FLY.getName()) && this.player.hasPermission(EnumTools.FLY.getPermission())){
                                    this.player.setAllowFlight(!this.player.getAllowFlight());
                                }else if (ChatColor.stripColor(unEvent.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(EnumTools.FLY.getName()) && !this.player.hasPermission(EnumTools.FLY.getPermission())){
                                    Messages messages = this.fichier.getmessInsufisantePerm();

                                    TextComponent msgBefore = new TextComponent(messages.getBeforeMessage());
                                    TextComponent msgHover = new TextComponent(messages.getHoverMessage());
                                    TextComponent msgAfter = new TextComponent(messages.getAfterMessage());

                                    msgHover.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, this.fichier.getURL("Players","messInsuffisantePermLien")));
                                    msgHover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.fichier.getMessageHover("Players","messInsuffisantePermHover")).create()));

                                    msgBefore.addExtra(msgHover);
                                    msgBefore.addExtra(msgAfter);

                                    this.player.spigot().sendMessage(msgBefore);
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

    private ItemStack itemUniqueWithName(ItemStack itemStack){
        ItemStack itemStack1 = new ItemStack(itemStack.getType());
        ItemMeta itemMeta = itemStack1.getItemMeta();
        itemMeta.setDisplayName(itemStack.getItemMeta().getDisplayName());
        itemStack1.setItemMeta(itemMeta);
        return itemStack1;
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
