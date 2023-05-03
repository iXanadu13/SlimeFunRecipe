package xanadu.slimefunrecipe.manager;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xanadu.slimefunrecipe.Main;
import xanadu.slimefunrecipe.SlimeRecipeType;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static xanadu.slimefunrecipe.Main.dataF;

public class ItemManager {
    public static final HashMap<SlimeRecipeType,RecipeType> mp = new HashMap<>();
    public static final Vector<RecipeType> recipeTypes = new Vector<>();
    public static ItemStack readAsItem(ConfigurationSection section, String path){
        String nbt = section.getString(path);
        if (nbt == null) return null;
        return section.getItemStack(path);
    }
    public static boolean saveRecipe(ItemStack[] recipe, SlimefunItem si){
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("RecipeType",si.getRecipeType().getKey().getKey());
        ConfigurationSection section = yaml.createSection("data");
        for(int i=0;i<9;i++){
            section.set(String.valueOf(i+1),recipe[i]);
        }
        Main.data.set(si.getId(),yaml);
        try {
            Main.data.save(dataF);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static void addLore(ItemStack item, String lore){
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            List<String> lores = meta.getLore();
            if (lores != null) {
                lores.add(0,lore);
                meta.setLore(lores);
            }
            else {
                meta.setLore(Collections.singletonList(lore));
            }
            item.setItemMeta(meta);
        }
    }
    public static boolean isEmpty(ItemStack item){
        if(item == null || item.getType() == Material.AIR) return true;
        return false;
    }
    public static RecipeType getByName(String str){
        SlimeRecipeType slimeRecipeType = SlimeRecipeType.getByName(str);
        if(slimeRecipeType == SlimeRecipeType._unknown) return null;
        return mp.get(slimeRecipeType);
    }
    public static void initRecipeType(){
        recipeTypes.clear();
        Field[] fields = RecipeType.class.getFields();
        for(Field field : fields){
            Type type = field.getType();
            if(!type.equals(RecipeType.class)) continue;
            SlimeRecipeType slimeRecipeType = SlimeRecipeType.getByName(field.getName());
            if(slimeRecipeType==SlimeRecipeType._unknown) continue;
            try {
                mp.put(slimeRecipeType, (RecipeType) field.get(null));
                recipeTypes.add((RecipeType) field.get(null));
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static ItemStack read(ConfigurationSection section, String str, String str2, Material material){
        if(material == null) return new ItemStack(Material.AIR);
        return new ItemStack(material,1);
    }
}
