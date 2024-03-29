package fr.reborned.effectgui.Tools;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.reborned.effectgui.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

public class Fichier extends File {
    private Main main;
    private int compteur;

    public Fichier(String pathname) {
        super(pathname);
        this.compteur=0;
    }
    public Fichier(String parent, String child) {
        super(parent, child);
    }
    public Fichier(File parent, String child) {
        super(parent, child);
        this.compteur=0;
    }
    public Fichier(File parent, String child, Main main) {
        super(parent, child);
        this.main=main;
        this.compteur=0;
    }

    public Fichier(URI uri) {
        super(uri);
    }

    public YamlConfiguration loadConfigurationConf(){
        File fichier = new File(this.getPath());
        return YamlConfiguration.loadConfiguration(fichier);
    }

    public void CreationFichier(){
        try {
            if (super.getParentFile().mkdirs())
            if (super.createNewFile()) {
                System.out.println("Fichier config vient d'être créer");
                if (this.fichierVide(getAbsoluteFile())) {
                    this.remplissageFile();
                }
            } else {
                System.out.println("Fichier de configuration déjà existant");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void remplissageFile() {
        String key ="";
        YamlConfiguration config = loadConfigurationConf();

        for (EnumTools S : EnumTools.values()){
            key = "ItemInMenu."+S.getCommande()+".";
            config.set(key+"name", "'"+S.getName()+"'");
            config.set(key+"type"," ");
            config.set(key+"lore"," ");
            config.set(key+"itemFlags"," ");
            config.set(key+"enchantement"," ");
            config.set(key+"amplifier"," ");
            config.set(key+"slotID"," ");
            if (S.getType().equalsIgnoreCase("RESEAU")){
                config.set(key+"URL","https://minecraft-heads.com/  //remplacer par 'Minecraft-URL' ");
                config.set(key+"messageReseau"," ");
                config.set(key+"lienReseau"," ");
                config.set(key+"messageHover"," ");
            }
        }
        key="Hotbarmenu.Item.";
        config.set(key+"name"," ");
        config.set(key+"type"," ");
        config.set(key+"lore"," ");
        config.set(key+"itemFlags"," ");
        config.set(key+"enchantement"," ");
        config.set(key+"URL"," ");
        config.set(key+"slotID"," ");

        key = "Menu.";
        config.set(key+"deletedChar",'&');
        config.set(key+"nblignes"," ");
        config.set(key+"title"," ");
        config.set(key+"Item"," ");

        key="Menu.Item";
        config.set(key+"name", "");
        config.set(key+"type"," ");
        config.set(key+"lore"," ");
        config.set(key+"itemFlags"," ");
        config.set(key+"enchantement"," ");
        config.set(key+"amplifier"," ");

        key = "AmplifierMenu.";
        config.set(key+"deletedChar",'&');
        config.set(key+"nblignes"," ");
        config.set(key+"title"," ");

        key="AmplifierMenu.Item";
        config.set(key+"name", "");
        config.set(key+"type"," ");
        config.set(key+"lore"," ");
        config.set(key+"itemFlags"," ");
        config.set(key+"enchantement"," ");
        config.set(key+"amplifier"," ");

        key= "Location.";
        config.set(key+"world", Bukkit.getServer().getWorlds().get(0).getName());

        key="Players.";
        config.set(key+"foodLevelChange",false);
        config.set(key+"featherFalling",false);
        config.set(key+"foodLevel", 20);
        config.set(key+"healthLevel", 20);
        config.set(key+"deletedCharFirst", '[');
        config.set(key+"deletedCharSecond", ']');
        config.set(key+"messInsuffisantePerm", "Redirection vers la [Boutique]");
        config.set(key+"messInsuffisantePermLien", "https://nitrocube.buycraft.net/");
        config.set(key+"messInsuffisantePermHover", "");
        config.set(key+"messActivation", "L'effet % a été activé");
        config.set(key+"messDesactivation", "L'effet % a été désactivé");
        config.set(key+"messDesacAll", "Tous les effets ont été désactivés");
        config.set(key+"dropItems", false);
        saveConfig(config);
    }

    public void saveConfig(YamlConfiguration configuration){
        try {
            configuration.save(super.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean fichierVide(File file) {
        boolean ret = false;
        if (file != null && file.length() == 0L) {
            ret = true;
        }

        return ret;
    }

    public boolean isWorldOk(Player player){
        boolean ret = false;

        if (player.getWorld() == getWorldConf().getWorld()){
            ret=true;
        }


        return ret;
    }

    public int getFoodLevel(){
        String key="Players";
        int ret=0;
        if (isSectionExist(key)) {
            ret = loadConfigurationConf().getConfigurationSection(key).getInt("foodLevel");
            if (ret < 0) {
                ret = Integer.MAX_VALUE;
                sendErrorToCMD(key, "foodLevel");
                System.out.println("Mettre une valeur comprise entre 0 et " + Integer.MAX_VALUE + ". Valeur par défaut = MAX");
            }
        }
        return ret;
    }

    public ItemStack getItem(String key){
        ItemStack itemStack = null;
        if (isSectionExist(key)){
            Material material =Material.getMaterial(loadConfigurationConf().getConfigurationSection(key).getString("type").toUpperCase());
            if (material==null){
                sendErrorToCMD(key,"type");
            }else {
                itemStack = new ItemStack(material);
                itemStack.setItemMeta(getItemMetaCongif(itemStack,key));
            }
        }
        return itemStack;
    }

    public int getHealthLevel(){
        String key="Players";
        int ret=0;
        if (isSectionExist(key)) {
            ret = loadConfigurationConf().getConfigurationSection("Players").getInt("healthLevel");
            if (ret < 0 || ret > 20) {
                ret = 20;
                sendErrorToCMD("Players", "healthLevel");
                System.out.println("Mettre une valeur comprise entre 0 et 20, 20 par défaut");
            }
        }
        return ret;
    }

    public Location getWorldConf(){
        Location location = null;
        String World = loadConfigurationConf().getConfigurationSection("Location").getString("world");
        location = new Location(Bukkit.getWorld(World),0,0,0);

        return location;
    }

    public int getLigneConf(String key, String subkey){
        int ligne =0;
        if (isSectionExist(key)){
            ligne=loadConfigurationConf().getConfigurationSection(key).getInt(subkey);
            if (ligne <0){
                ligne=1;
            }
        }
        return ligne;
    }

    public String getTitleConf(String key, String subkey){
        String str ="";
        if (isSectionExist(key)) {
            str = loadConfigurationConf().getConfigurationSection(key).getString(subkey);

            if (str.equals(" ")) {
                sendErrorToCMD(key, subkey);
                str = ChatColor.DARK_RED + "Pas de titre défini";
            }
            str = colorString(str);
        }
        return str;
    }

    public ItemStack getItemHotbar(){
        ItemStack itemStack = null;
        String key = "Hotbarmenu.Item";

        if (isSectionExist(key)){
            Material material = Material.getMaterial(loadConfigurationConf().getConfigurationSection(key).getString("type").toUpperCase());
            if (material ==null){
                sendErrorToCMD(key,"type");
            }else {
                itemStack = new ItemStack(material);

                itemStack.setItemMeta(getItemMetaCongif(itemStack, key));
            }
        }

        return itemStack;
    }

    public ArrayList<ItemStacked> getItemStackMenuConf(String key){
        ItemStacked itemStacked =null;
        YamlConfiguration configuration = loadConfigurationConf();
        ArrayList<ItemStacked> stackeds = new ArrayList<>();
        if (isSectionExist(key)) {
            for (String itemString : loadConfigurationConf().getConfigurationSection(key).getKeys(false)){
                if (configuration.getConfigurationSection(key).getString(itemString + ".type") != null && !configuration.getConfigurationSection(key).getString(itemString + ".type").equalsIgnoreCase(" ")) {
                    if (Arrays.stream(EnumTools.values()).anyMatch(enumTools -> enumTools.getCommande().equalsIgnoreCase(configuration.getConfigurationSection(key).getString(itemString + ".type"))) && !configuration.getConfigurationSection(key).getString(itemString + ".type").equals(" ")) {

                        ItemStack itemStack = new ItemStack(getSkull(configuration.getConfigurationSection(key).getString(itemString + ".URL")));

                        itemStack.setItemMeta(getItemMetaCongif(itemStack, key+"." + itemString));
                        stackeds.add(new ItemStacked(itemStack, getSlotID(key+"." + itemString)));
                    } else if (configuration.getConfigurationSection(key).getString(itemString + ".type") != null) {
                        Material material = Material.getMaterial(configuration.getConfigurationSection(key).getString(itemString + ".type").toUpperCase());
                        if (material == null) {
                            sendErrorToCMD(key+"." + itemString, "type");
                            System.out.println("Materiel non compris ! (item glass par défaut)");
                            material = Material.GLASS;
                        }
                        ItemStack itemStack = new ItemStack(material);
                        itemStack.setItemMeta(getItemMetaCongif(itemStack, key+"." + itemString));
                        itemStacked = new ItemStacked(itemStack, getSlotID(key+"." + itemString));
                        stackeds.add(itemStacked);
                    }
                }
            }
        }
        return stackeds;
    }

    public ItemMeta getItemMetaCongif(ItemStack item,String key){
        ItemMeta itemMeta= item.getItemMeta();
        if (isSectionExist(key)) {
            ConfigurationSection section = loadConfigurationConf().getConfigurationSection(key);

            if (section.getString(".name") != null && !section.getString(".name").equalsIgnoreCase(" ")) {
                itemMeta.setDisplayName(colorString(section.getString(".name")));
            }
            if (section.getString(".enchantement") != null && !section.getString(".enchantement").equals(" ")) {
                for (String s : section.getStringList(".enchantement")) {
                    String enchName = s.split(":")[0];
                    Integer enchLevel = Integer.valueOf(s.split(":")[1]);
                    boolean enchBool = Boolean.parseBoolean(s.split(":")[2]);
                    itemMeta.addEnchant(Enchantment.getByName(enchName.toUpperCase()), enchLevel, true);
                }
            }
            if (section.getString(".lore") != null && !section.getString(".lore").equals(" ")) {
                itemMeta.setLore(section.getStringList(".lore"));
            }
            if (section.getString(".itemFlags") != null && !section.getString(".itemFlags").equals(" ")) {
                for (String s : section.getStringList(".itemFlags")) {
                    itemMeta.addItemFlags(ItemFlag.valueOf(s));
                }
            }
        }
        return itemMeta;
    }

    public int getSlotID(String key){
        int slot =0;
        if (isSectionExist(key)) {
            try {
                slot = Integer.parseInt(loadConfigurationConf().getConfigurationSection(key).getString("slotID"));
            }catch (Exception e){
                sendErrorToCMD(key, "slotID");
                System.out.println(key + ".slotID dans la config n'est pas précisé a été mi par défaut à " + this.compteur);
                slot = this.compteur;
                this.compteur++;
            }
        }
        return slot;
    }

    public int getIntOfEffect(String key){
        int effect=0;
        if (isSectionExist(key)) {
            effect = loadConfigurationConf().getConfigurationSection(key).getInt(".amplifier");
            if (effect < 0) {
                sendErrorToCMD(key, "amplifier");
                System.out.println("L'amplier a été mis par défaut à 1");
                effect = 1;
            }
        }
        return effect;
    }

    public String getLinkSocialNetwork(String key){
        String s="";
        if (isSectionExist(key)) {
            s = loadConfigurationConf().getConfigurationSection(key).getString("lienReseau");

            if (s.equals(" ")) {
                sendErrorToCMD(key, "lienReseau");
                s = "Link dead";
            }else{
                s = colorString(s);
            }
        }
        return s;
    }

    public boolean getfoodLevelChangeConf(){
        return configBool("Players","foodLevelChange");
    }

    public boolean getFallDamage(){
        return configBool("Players","featherFalling");
    }

    public boolean getDropItems(){
        return configBool("Players","dropItems");
    }

    public Messages getmessInsufisantePerm(){
        Messages messages = null;
        String str;
        String key="Players";
        if (isSectionExist(key)) {
            str = loadConfigurationConf().getConfigurationSection(key).getString("messInsuffisantePerm");

            messages = custom(str);
        }
        return messages;

    }
    public String getURL(String key,String subkey){
        String ret="";
        if (isSectionExist(key)){
            ret=loadConfigurationConf().getConfigurationSection(key).getString(subkey);
        }
        return ret;
    }

    public String getMessageHover(String key, String subkey){
        String str="";
        if (isSectionExist(key)) {
            str = loadConfigurationConf().getConfigurationSection(key).getString(subkey);
            str = colorString(str);
        }
        return str;
    }

    public Messages getMessage(String key, String subkey){
        Messages messages =null;
        String str="";
        if (isSectionExist(key)){
            if (!(loadConfigurationConf().getConfigurationSection(key).getString(subkey).equals(" "))){
                try {
                    str=loadConfigurationConf().getConfigurationSection(key+"."+subkey).getString("messageReseau");
                }catch (Exception e){
                    str="";
                }
                messages=custom(str);


            }else {
                messages =new Messages("","","");
            }
        }
        return messages;
    }

    public String getActivDesactivMessage(String key, String subkey){
        String str="";
        if (isSectionExist(key)){
            str= loadConfigurationConf().getConfigurationSection(key).getString(subkey);
            colorString(str);
        }
        return str;
    }

    private Messages custom(String s){
        Messages messages =null;
        String key="Players";
        if (s.length()>0) {
            if (isSectionExist(key)) {

                String del1 = loadConfigurationConf().getConfigurationSection(key).getString("deletedCharFirst").substring(0,1);
                String del2 = loadConfigurationConf().getConfigurationSection(key).getString("deletedCharSecond").substring(0,1);
                if (del1.equals(" ")) {
                    del1 = "[";
                }
                if (del2.equals(" ")) {
                    del2 = "]";
                }
                if (s.contains(del1) && s.contains(del2)){
                    String s1,s2,s3;
                    try {
                        s1 = s.split(Pattern.quote(del1))[0];
                    }catch (Exception e){
                        s1="";
                    }
                    try {
                        s2 = s.substring(s.indexOf(del1), s.indexOf(del2)+1);
                    }catch (Exception e){
                        s2="";
                    }
                    try {
                        s3 = s.split(Pattern.quote(del2))[1];
                    }catch (Exception e){
                        s3="";
                    }


                    s1 = colorString(s1);
                    s2 = colorString(ChatColor.getLastColors(s1)+s2);
                    s3 = colorString(ChatColor.getLastColors(s2)+s3);
                    messages = new Messages(s1, s2, s3);
                }else {
                    s=colorString(s);
                    messages = new Messages(s,"","");
                }

            }
        }else
        {
            messages= new Messages("","","");
        }
        return messages;
    }

    private String colorString(String s){
        String ret="";
        char fromConf=getHideChar();
        if (s.contains(String.valueOf(fromConf))) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);

                if (c == fromConf) {
                    char id = s.charAt(i + 1);
                    ret += ChatColor.getByChar(id);
                    i++;
                    i++;
                    try {
                        c = s.charAt(i);
                    } catch (StringIndexOutOfBoundsException e) {
                        c = ' ';
                        e.printStackTrace();
                    }

                }
                ret += c;
            }
        }else {
            ret=s;
        }
        return ret;
    }

    private char getHideChar(){
        char ret =' ';
        String key="Menu";
        if (isSectionExist(key)) {
            if (loadConfigurationConf().getConfigurationSection(key).getString("deletedChar").equals(" ")) {
                ret = '&';
            } else {
                ret = loadConfigurationConf().getConfigurationSection(key).getString("deletedChar").charAt(0);
            }
        }
        return ret;
    }

    private static ItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (url.isEmpty())
            return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    private boolean configBool(String key, String subkey){
        boolean ret=false;
        try {
            if (isSectionExist(key)) {
                if (loadConfigurationConf().getConfigurationSection(key).getString(subkey).equals(" ")) {
                    ret = true;
                    sendErrorToCMD(key, subkey);
                    System.out.println("Veuillez verifier la syntaxe de" + subkey);
                } else {
                    ret = loadConfigurationConf().getConfigurationSection(key).getBoolean(subkey);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }


    private boolean isSectionExist(String key){
        boolean ret = true;
        if (loadConfigurationConf().getConfigurationSection(key) == null){
            sendErrorToCMD(key);
            ret = false;
        }
        return ret;
    }

    private void sendErrorToCMD(String key, String subkey){
        System.out.println("Une erreur pour la configuration de "+key+"."+subkey);
    }
    private void sendErrorToCMD(String key){
        System.out.println("Une erreur pour la configuration de "+key);
    }
}

