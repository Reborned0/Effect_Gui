package fr.reborned.effectgui.Tools;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class Fichier extends File {
    public Fichier(String pathname) {
        super(pathname);
    }

    public Fichier(String parent, String child) {
        super(parent, child);
    }

    public Fichier(File parent, String child) {
        super(parent, child);
    }

    public Fichier(URI uri) {
        super(uri);
    }

    private YamlConfiguration loadConfiguration(){
        YamlConfiguration configuration = null;
        Fichier fichier = new Fichier(this.getAbsolutePath(),"config.yml");
        configuration = YamlConfiguration.loadConfiguration(fichier);
        return configuration;
    }

    public void CreationFichier(){
        try {
            super.getParentFile().mkdirs();
            if (super.createNewFile()) {
                System.out.println("Fichier config vient d'être créer");
                if (this.fichierVide(super.getAbsoluteFile())) {
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
        for (EnumTools S : EnumTools.values()){
            key = "hotbar."+S.getName()+".";
            loadConfiguration().set(key+"name", S.getName());
            loadConfiguration().set(key+"type"," ");
            loadConfiguration().set(key+"lore"," ");
            loadConfiguration().set(key+"enchantement"," ");
            loadConfiguration().set(key+"slotID"," ");
        }
        key = "menu.";
        loadConfiguration().set(key+"nblignes"," ");
        loadConfiguration().set(key+"title"," ");
        saveConfig(loadConfiguration());
    }

    private void saveConfig(YamlConfiguration configuration){
        try {
            configuration.save(getCanonicalFile());
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
        String World = loadConfiguration().getConfigurationSection("Location").getString("world");
        location = new Location(Bukkit.getWorld(World),0,0,0);

        return location;
    }

    public int getLigneConf(){
        return loadConfiguration().getConfigurationSection("menu").getInt("nblignes");
    }
    public String getTitleConf(){
        return loadConfiguration().getConfigurationSection("menu").getString("title");
    }

    public ItemStack getItemHotbar(){
        ItemStack itemStack = null;
        String key = "hotbar.item1";
        Material material = Material.getMaterial(loadConfiguration().getConfigurationSection(key).getString("type"));
        itemStack = new ItemStack(material);

        itemStack.setItemMeta(getItemMetaCongif(key));

        return itemStack;
    }

    public ArrayList<ItemStacked> getItemStackMenuConf(){
        ItemStacked itemStacked =null;
        ArrayList<ItemStacked> stackeds = new ArrayList<>();
        String key ="iteminmenu.";

        for (String itemString : loadConfiguration().getConfigurationSection(key).getKeys(false)){
            if (loadConfiguration().getConfigurationSection(key).getString(itemString+".type") != null && !loadConfiguration().getConfigurationSection(key).getString(itemString+".type").equalsIgnoreCase(" ")){
                Material material = Material.getMaterial(loadConfiguration().getConfigurationSection(key).getString(itemString+".type"));
                ItemStack itemStack = new ItemStack(material);
                itemStack.setItemMeta(getItemMetaCongif(key+itemString));
                itemStacked = new ItemStacked(itemStack,getSlotID(key+itemString));
                stackeds.add(itemStacked);
            }
        }

        return stackeds;
    }

    public ItemMeta getItemMetaCongif(String key){
        ItemMeta itemMeta = null;
        ConfigurationSection section = loadConfiguration().getConfigurationSection(key);
        if (section.getString(".name") != null && !section.getString(".name").equalsIgnoreCase(" ")){
            itemMeta.setDisplayName(section.getString(".name"));
        }
        else if (section.getString(".enchantement") != null ){
            for (String s : section.getStringList(".enchantement")){
                String enchName = s.split(":")[0];
                Integer enchLevel = Integer.valueOf(s.split(":")[1]);
                boolean enchBool = Boolean.parseBoolean(s.split(":")[2]);
                itemMeta.addEnchant(Enchantment.getByName(enchName),enchLevel,enchBool);
            }
        }
        else if (section.getString(".lore") != null){
            itemMeta.setLore(section.getStringList(".lore"));
        }
        return itemMeta;
    }
    public int getSlotID(String key){
        return loadConfiguration().getConfigurationSection(key).getInt(".slotID");
    }

    public int getIntOfEffect(String key){
        return loadConfiguration().getConfigurationSection(key).getInt(".amplifier");
    }
}

