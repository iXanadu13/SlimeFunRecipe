package pers.xanadu.slimefunrecipe.manager;

import io.github.mooy1.infinityexpansion.infinitylib.machines.MachineRecipeType;
import io.github.mooy1.infinityexpansion.items.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.items.mobdata.MobData;
import io.github.mooy1.infinityexpansion.items.storage.StorageForge;
import io.github.thebusybiscuit.exoticgarden.ExoticGardenRecipeTypes;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.ncbpfluffybear.flowerpower.multiblocks.MagicBasin;
import me.sfiguz7.transcendence.lists.TERecipeType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pers.xanadu.slimefunrecipe.Main;
import pers.xanadu.slimefunrecipe.SlimeRecipeType;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ItemManager {
    public static final HashMap<SlimeRecipeType,RecipeType> mp = new HashMap<>();
    public static final Vector<RecipeType> recipeTypes = new Vector<>();
    public static ItemStack readAsItem(final ConfigurationSection section,final String path){
        String nbt = section.getString(path);
        if (nbt == null) return null;
        return section.getItemStack(path);
    }
    public static boolean saveRecipe(final ItemStack[] recipe,final SlimefunItem si){
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("RecipeType",si.getRecipeType().getKey().getKey());
        yaml.set("size",recipe.length);
        ConfigurationSection section = yaml.createSection("data");
        for(int i=0;i<recipe.length;i++){
            section.set(String.valueOf(i+1),recipe[i]);
        }
        Main.data.set(si.getId(),yaml);
        try {
            Main.data.save(Main.dataF);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static void addLore(final ItemStack item,final String lore){
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
    public static boolean isEmpty(final ItemStack item){
        if(item == null || item.getType() == Material.AIR) return true;
        return false;
    }
    public static void setRecipe(final SlimefunItem si,final ItemStack[] recipe){
        if(recipe.length == 9){
            si.setRecipe(recipe);
        }
        else{
            try {
                Class<?> clazz = Class.forName("io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem");
                Field si_recipe = clazz.getDeclaredField("recipe");
                si_recipe.setAccessible(true);
                si_recipe.set(si,recipe);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static RecipeType getByName(final String str){
        SlimeRecipeType slimeRecipeType = SlimeRecipeType.getByName(str);
        if(slimeRecipeType == SlimeRecipeType._unknown) return null;
        return mp.get(slimeRecipeType);
    }
    public static void initRecipeType(){
        recipeTypes.clear();
        registerAll(RecipeType.class);
        if(Bukkit.getPluginManager().getPlugin("InfinityExpansion")!=null){
            registerRecipeType(SlimeRecipeType.infinity_forge,InfinityWorkbench.TYPE);
            final RecipeType mob_data_infuser = new MachineRecipeType("mob_data_infuser", MobData.INFUSER);
            registerRecipeType(SlimeRecipeType.mob_data_infuser,mob_data_infuser);
            registerRecipeType(SlimeRecipeType.storage_forge,StorageForge.TYPE);
        }
        if(Bukkit.getPluginManager().getPlugin("ExoticGarden")!=null){
            registerAll(ExoticGardenRecipeTypes.class);
        }
        if(Bukkit.getPluginManager().getPlugin("TranscEndence")!=null){
            registerAll(TERecipeType.class);
        }
        if(Bukkit.getPluginManager().getPlugin("FlowerPower")!=null){
            registerRecipeType(SlimeRecipeType.magic_basin, MagicBasin.BASIN_RECIPE);
        }
    }
    private static void registerAll(final Class<?> clazz){
        Field[] fields = clazz.getFields();
        for(Field field : fields){
            Type type = field.getType();
            if(!type.equals(RecipeType.class)) continue;
            final SlimeRecipeType slimeRecipeType = SlimeRecipeType.getByName(field.getName());
            if(slimeRecipeType==SlimeRecipeType._unknown) continue;
            try {
                registerRecipeType(slimeRecipeType,(RecipeType) field.get(null));
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static void registerRecipeType(final SlimeRecipeType my_type,final RecipeType recipeType){
        mp.put(my_type,recipeType);
        recipeTypes.add(recipeType);
    }
    public static ItemStack read(ConfigurationSection section, String str, String str2, Material material){
        if(material == null) return new ItemStack(Material.AIR);
        return new ItemStack(material,1);
    }
}
