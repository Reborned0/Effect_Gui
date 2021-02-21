package fr.reborned.effectgui.Tools;

import org.bukkit.permissions.Permission;

public enum EnumTools {
    SPEED("Fly away", "Speed Boost", ""),
    JUMP("Fly away", "Jump Boost", ""),
    FLY("Fly away","FLY","nitroutils.fly");

    private String name;
    private String commande;
    private String permission;
    EnumTools( String name, String commande,String permission) {
        this.name=name;
        this.commande=commande;
        this.permission=permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommande() {
        return commande;
    }

    public void setCommande(String commande) {
        this.commande = commande;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
