package fr.reborned.effectgui.Tools;

public enum EnumTools {
    SPEED("Speed", "SPEED", " ","POTION"),
    JUMP("Jump", "JUMP", " ","POTION"),
    FLY("Fly","FLY","nitroutils.fly","POTION"),
    DUMP("Reset","DUMP"," ","POTION"),
    FACEBOOK("Facebook","FACEBOOK"," ","RESEAU"),
    YOUTUBE("Youtube", "YOUTUBE"," ","RESEAU"),
    INSTAGRAM("Instagram","INSTAGRAM", " ","RESEAU"),
    FORUM("Forum","FORUM"," ","RESEAU"),
    TWITTER("Twitter","TWITTER"," ","RESEAU");

    private String name;
    private String commande;
    private String permission;
    private String type;

    EnumTools( String name, String commande,String permission, String type) {
        this.name=name;
        this.commande=commande;
        this.permission=permission;
        this.type=type;
    }

    public String getType() {
        return type;
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
