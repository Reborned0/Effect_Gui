package fr.reborned.effectgui;

import fr.reborned.effectgui.Listener.PListerner;
import fr.reborned.effectgui.Tools.FastInvManager;
import fr.reborned.effectgui.Tools.Fichier;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.ls.LSOutput;

public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        Fichier fichier = new Fichier(getDataFolder(),"config.yml",this);
        fichier.CreationFichier();
    }

    @Override
    public void onEnable() {
        System.out.println("Plugin Effect_Gui Started");
        this.getServer().getPluginManager().registerEvents(new PListerner(this),this);
        FastInvManager.register(this);
    }
}
