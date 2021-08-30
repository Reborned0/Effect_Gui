package fr.reborned.effectgui.invGUI.Templates;

import fr.reborned.effectgui.Tools.*;
import fr.reborned.effectgui.invGUI.InvGUI;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Optional;

public class AmplierGUI extends InvGUI {

    private Player player;
    private FastInv fastInv;
    private int ligne;
    private Fichier fichier;
    private ItemStack itemStack;
    private int compteur;
    private String diplayName;

    public AmplierGUI(Player player,ItemStack itemStack, Fichier fichier){
        this.player=player;
        this.fichier = new Fichier(fichier.getPath());
        this.ligne = this.fichier.getLigneConf("AmplifierMenu","nblignes");
        this.fastInv = new FastInv(9*ligne, this.fichier.getTitleConf("AmplifierMenu","title"));
        this.itemStack=itemStack;
        this.diplayName=itemStack.getItemMeta().getDisplayName().substring(2);
        this.compteur=1;
        this.init();
    }

    @Override
    public void init() {
        int amplifier = 0;
        if (this.player.hasPotionEffect(PotionEffectType.getByName(diplayName))) {
            amplifier = this.player.getPotionEffect(PotionEffectType.getByName(diplayName)).getAmplifier() + 1;
        }
            for (int i = this.compteur; i < compteur + 5; i++) {
                if (i == amplifier) {
                    this.fastInv.setItem(i + 10, setAttribute(enchantItemInventory(this.itemStack), i,true)); //enchanté
                    removeEnchant(this.itemStack);
                }else{
                    this.fastInv.setItem(i + 10, setAttribute(this.itemStack, i,false));
                }
            }

        if (this.fichier.getItem("AmplifierMenu.Item")!=null) {
            this.fastInv.setItem(this.ligne * 9 - 5, this.fichier.getItem("AmplifierMenu.Item"));
        }

        this.fastInv.addClickHandler(inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().name().contains("HOTBAR")){
                inventoryClickEvent.setCancelled(true);
            }else{
                if (!inventoryClickEvent.getCurrentItem().getType().isTransparent()){
                    if (inventoryClickEvent.getCurrentItem().isSimilar(this.fichier.getItem("AmplifierMenu.Item"))){
                        EffectsGUI effectsGUI = new EffectsGUI(this.player,this.fichier);
                        effectsGUI.openInv();
                        return;
                    }
                    Optional<EnumTools> optional = Arrays.stream(EnumTools.values()).filter(enumTools -> {
                        return ChatColor.stripColor(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName()).split(" ")[0].equalsIgnoreCase(enumTools.getCommande());
                    }).findAny();
                    optional.ifPresent(enumTools -> {
                        String str= inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1];
                        int c = RomanNumber.toInt(str);

                        if (this.player.hasPotionEffect(PotionEffectType.getByName(enumTools.getCommande().toUpperCase()))){
                            for (PotionEffect potionEffect : this.player.getActivePotionEffects()){
                                if (this.player.hasPotionEffect(PotionEffectType.getByName(diplayName)) && PotionEffectType.getByName(diplayName).equals(potionEffect.getType())){
                                    if (potionEffect.getAmplifier() == c-1){
                                        this.player.removePotionEffect(potionEffect.getType());

                                    }else {
                                        try {
                                            this.player.removePotionEffect(potionEffect.getType());
                                            this.player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(enumTools.getCommande()),Integer.MAX_VALUE,c-1,true));
                                        }catch (Exception e){
                                            System.out.println("Effet non trouvé");
                                        }
                                    }
                                }
                            }
                        }else {
                            try {
                                this.player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(enumTools.getCommande()),Integer.MAX_VALUE,c-1,true));
                            }catch (Exception e){
                                System.out.println("Effet non trouvé");
                            }
                        }
                        this.player.closeInventory();
                        this.player.updateInventory();
                    });
                }
            }
        });
    }
    private ItemStack setAttribute(ItemStack itemStack, int i){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET+""+ this.diplayName+" "+ RomanNumber.toRoman(i));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack setAttribute(ItemStack itemStack, int i, boolean b){
        if (b){
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RESET+""+ ChatColor.GOLD+ this.diplayName+" "+ RomanNumber.toRoman(i));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }else {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RESET+""+ this.diplayName+" "+ RomanNumber.toRoman(i));
            itemStack.setItemMeta(itemMeta);
            return itemStack;

        }
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
        return fastInv;
    }
    private ItemStack enchantItemInventory(ItemStack itemStack){
        ItemMeta itemMeta =itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    private ItemStack removeEnchant (ItemStack itemStack){
        ItemMeta itemMeta =itemStack.getItemMeta();
        itemMeta.removeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
