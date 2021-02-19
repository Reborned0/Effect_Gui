package fr.reborned.effectgui.invGUI.Templates;

import fr.reborned.effectgui.Main;
import fr.reborned.effectgui.Tools.FastInv;
import fr.reborned.effectgui.Tools.Fichier;
import fr.reborned.effectgui.Tools.ItemStacked;
import fr.reborned.effectgui.invGUI.InvGUI;
import org.bukkit.entity.Player;

import java.util.Arrays;

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
//TODO
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
