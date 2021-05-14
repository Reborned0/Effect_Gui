package fr.reborned.effectgui.invGUI.Templates;

import fr.reborned.effectgui.Tools.EnumTools;
import fr.reborned.effectgui.Tools.FastInv;
import fr.reborned.effectgui.Tools.Fichier;
import fr.reborned.effectgui.Tools.RomanNumber;
import fr.reborned.effectgui.invGUI.InvGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
        this.diplayName=itemStack.getItemMeta().getDisplayName();
        this.compteur=1;
        this.init();
    }


    @Override
    public void init() {
        int amplifier = this.player.getPotionEffect(PotionEffectType.getByName(this.itemStack.getItemMeta().getDisplayName().toUpperCase())).getAmplifier();
        for (int i=this.compteur;i<compteur+5;i++){
            if (i==amplifier){
               //ajouter enchantement
            }
            this.fastInv.setItem(i+10,setAttribute(this.itemStack,i));
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
                                if (this.player.hasPotionEffect(PotionEffectType.getByName(enumTools.getCommande().toUpperCase()))){
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
        itemMeta.setDisplayName(this.diplayName+" "+ RomanNumber.toRoman(i));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
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
}
