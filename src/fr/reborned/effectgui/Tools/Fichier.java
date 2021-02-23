package fr.reborned.effectgui.Tools;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sun.xml.internal.messaging.saaj.util.CharWriter;
import fr.reborned.effectgui.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.util.List;
import java.util.UUID;

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
            super.getParentFile().mkdirs();
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
            key = "Iteminmenu."+S.getName()+".";
            config.set(key+"name", S.getName());
            config.set(key+"type"," ");
            config.set(key+"lore"," ");
            config.set(key+"itemFlags"," ");
            config.set(key+"enchantement"," ");
            config.set(key+"URL"," ");
            config.set(key+"amplifier"," ");
            config.set(key+"slotID"," ");
            if (S.getCommande().equalsIgnoreCase("TCHAT")){
                config.set(key+"lienReseau"," ");
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
        config.set(key+"nblignes"," ");
        config.set(key+"title"," ");
        key= "Location.";
        config.set(key+"world", Bukkit.getServer().getWorlds().get(0).getName());

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


    public Location getWorldConf(){
        Location location = null;
        String World = loadConfigurationConf().getConfigurationSection("Location").getString("world");
        location = new Location(Bukkit.getWorld(World),0,0,0);

        return location;
    }

    public int getLigneConf(){
        return loadConfigurationConf().getConfigurationSection("Menu").getInt("nblignes");
    }
    public String getTitleConf(){
        String str =loadConfigurationConf().getConfigurationSection("Menu").getString("title");
        String ret="";

        System.out.println(str);
        for (int i=0;i<str.length();i++){
            char c = str.charAt(i);
            System.out.println(c);
            if (c=='&'){
                char id= str.charAt(i+1);
                ret+=ChatColor.getByChar(id);
                i++;
                i++;
                try {
                    c=str.charAt(i);
                }catch (StringIndexOutOfBoundsException e){
                    c=' ';
                    e.printStackTrace();
                }

            }
                ret += c;
        }
        return ret;
    }

    public ItemStack getItemHotbar(){
        ItemStack itemStack = null;
        String key = "Hotbarmenu.Item";
        YamlConfiguration config = loadConfigurationConf();
        ConfigurationSection configurationSection = config.getConfigurationSection(key);

        if (configurationSection == null){
            System.out.println("Section does not exist !");
        }else {
            Material material = Material.getMaterial(config.getConfigurationSection(key).getString("type").toUpperCase());
            if (material ==null){
                System.out.println("Materiel non compris !");
            }else {
                itemStack = new ItemStack(material);

                itemStack.setItemMeta(getItemMetaCongif(itemStack, key));
            }
        }


        return itemStack;
    }

    public ArrayList<ItemStacked> getItemStackMenuConf(){
        ItemStacked itemStacked =null;
        YamlConfiguration configuration = loadConfigurationConf();
        ArrayList<ItemStacked> stackeds = new ArrayList<>();
        String key ="Iteminmenu.";

        for (String itemString : loadConfigurationConf().getConfigurationSection(key).getKeys(false)){
            if (configuration.getConfigurationSection(key).getString(itemString+".type") != null && !configuration.getConfigurationSection(key).getString(itemString+".type").equalsIgnoreCase(" ")){
                if (Arrays.stream(EnumTools.values()).anyMatch(enumTools -> enumTools.getName().contains(configuration.getConfigurationSection(key).getString(itemString+".type"))) && !configuration.getConfigurationSection(key).getString(itemString+".type").equals(" ")){

                    ItemStack itemStack = new ItemStack(getSkull(configuration.getConfigurationSection(key).getString(itemString+".URL")));

                    itemStack.setItemMeta(getItemMetaCongif(itemStack, key + itemString));
                    stackeds.add(new ItemStacked(itemStack,getSlotID(key+itemString)));
                }
                else if(configuration.getConfigurationSection(key).getString(itemString+".type") != null) {
                    Material material = Material.getMaterial(configuration.getConfigurationSection(key).getString(itemString + ".type").toUpperCase());
                    if (material == null) {
                        System.out.println("Materiel non compris ! (item glass par défaut)");
                        material = Material.GLASS;
                    }
                    ItemStack itemStack = new ItemStack(material);

                    itemStack.setItemMeta(getItemMetaCongif(itemStack, key + itemString));
                    itemStacked = new ItemStacked(itemStack, getSlotID(key + itemString));
                    stackeds.add(itemStacked);
                }
            }
        }

        return stackeds;
    }

    public ItemMeta getItemMetaCongif(ItemStack item,String key){
        ItemMeta itemMeta= item.getItemMeta();
        ConfigurationSection section = loadConfigurationConf().getConfigurationSection(key);

        if (section.getString(".name") != null && !section.getString(".name").equalsIgnoreCase(" ")){
            itemMeta.setDisplayName(section.getString(".name"));
        }
        if (section.getString(".enchantement") != null && !section.getString(".enchantement").equals(" ")){
            for (String s : section.getStringList(".enchantement")) {
                String enchName = s.split(":")[0];
                Integer enchLevel = Integer.valueOf(s.split(":")[1]);
                boolean enchBool = Boolean.parseBoolean(s.split(":")[2]);
                itemMeta.addEnchant(Enchantment.getByName(enchName.toUpperCase()), enchLevel, true);
            }
        }
        if (section.getString(".lore") != null && !section.getString(".lore").equals(" ")){
            itemMeta.setLore(section.getStringList(".lore"));
        }
        if (section.getString(".itemFlags") != null && !section.getString(".itemFlags").equals(" ")){
            for (String s : section.getStringList(".itemFlags")){
                itemMeta.addItemFlags(ItemFlag.valueOf(s));
            }
        }
        return itemMeta;
    }
    public int getSlotID(String key){
        int slot=Integer.parseInt(loadConfigurationConf().getConfigurationSection(key).getString("slotID"));
        if (slot <0) {
            System.out.println(key+".slotID dans la config n'est pas précisé a été mi par défaut à "+this.compteur);
            slot=this.compteur;
            this.compteur++;
        }
        return slot;
    }

    public int getIntOfEffect(String key){

        int effect = loadConfigurationConf().getConfigurationSection(key).getInt(".amplifier");
        if (effect <0){
            System.out.println("L'amplier a été mis par défaut à 1");
            effect=1;
        }

        return effect;
    }

    public String getLinkSocialNetwork(String key){
        String s="";
        if (loadConfigurationConf().getConfigurationSection(key).getString("lienReseau").equals(" ")){
            s="Link dead";
        }
        return s;
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
}

