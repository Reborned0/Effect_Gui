package fr.reborned.effectgui.invGUI.Templates;

import fr.reborned.effectgui.Main;
import fr.reborned.effectgui.Tools.FastInv;
import fr.reborned.effectgui.Tools.Fichier;
import fr.reborned.effectgui.invGUI.InvGUI;
import org.bukkit.entity.Player;

public class AmplierGUI extends InvGUI {

    private Player player;
    private FastInv fastInv;
    private int Ligne;
    private Fichier fichier;
    private Main main;

    public AmplierGUI(Player player, Fichier fichier){
        this.player=player;
        this.fichier = new Fichier(fichier.getPath());
        this.Ligne = fichier.getLigneConf("Menu","nblignes");
        this.fastInv = new FastInv(9*Ligne, fichier.getTitleConf("Menu","title"));
        this.init();
    }


    @Override
    public void init() {

    }

    @Override
    public void openInv() {

    }

    @Override
    public InvGUI get() {
        return null;
    }

    @Override
    public FastInv getFastInventory() {
        return null;
    }
}
