package fr.reborned.effectgui.invGUI.Templates;

import fr.reborned.effectgui.Main;
import fr.reborned.effectgui.Tools.EnumTools;
import fr.reborned.effectgui.Tools.FastInv;
import fr.reborned.effectgui.Tools.Fichier;
import fr.reborned.effectgui.Tools.RomanNumber;
import fr.reborned.effectgui.invGUI.InvGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
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
        for (int i=this.compteur;i<compteur+6;i++){
            System.out.println(compteur);
            this.fastInv.setItem(i,setAttribute(this.itemStack,i));
        }
        ItemStack itemStack = new ItemStack(Material.BLAZE_ROD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Go back");
        itemStack.setItemMeta(itemMeta);
        this.fastInv.setItem(this.ligne*9-1,itemStack);


        this.fastInv.addClickHandler(inventoryClickEvent -> {
            if (inventoryClickEvent.getAction().name().contains("HOTBAR")){
                inventoryClickEvent.setCancelled(true);
            }else{
                if (!inventoryClickEvent.getCurrentItem().getType().isTransparent()){
                    Optional<EnumTools> optional = Arrays.stream(EnumTools.values()).filter(enumTools -> {
                        return ChatColor.stripColor(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName()).split(" ")[0].equalsIgnoreCase(enumTools.getCommande());
                    }).findAny();
                    optional.ifPresent(enumTools -> {
                        String str= inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1];
                        int c = RomanNumber.toInt(str);
                        try {
                            this.player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(enumTools.getCommande()),Integer.MAX_VALUE,c-1,true));
                        }catch (Exception e){
                            System.out.println("Effet non trouvé");
                        }

                        this.player.closeInventory();
                        this.player.updateInventory();
                    });
                    if (inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName().equals("Go back")){
                        EffectsGUI effectsGUI = new EffectsGUI(this.player,this.fichier);
                        effectsGUI.openInv();
                    }
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
