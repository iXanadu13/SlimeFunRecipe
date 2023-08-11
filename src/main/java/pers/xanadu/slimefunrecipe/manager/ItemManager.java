package pers.xanadu.slimefunrecipe.manager;

import io.github.mooy1.infinityexpansion.items.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.items.mobdata.MobDataInfuser;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static pers.xanadu.slimefunrecipe.Main.plugin;

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
    public static boolean addRecipe(final ItemStack[] input,final ItemStack result,final RecipeType type,final String id,final String file_name){
        File file = new File(plugin.getDataFolder(), "recipe/add/"+file_name);
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = yml.createSection(id);
        section.set("RecipeType",type.getKey().getKey());
        section.set("size",input.length);
        section.set("input_data_type","default");
        section.set("output_data_type","default");
        ConfigurationSection input_data = section.createSection("input");
        for(int i=0;i<input.length;i++){
            input_data.set(String.valueOf(i+1),input[i]);
        }
        section.set("output",result);
        try {
            yml.save(file);
        }catch (IOException e){
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

    public static RecipeType getByName(final String str){
        SlimeRecipeType slimeRecipeType = SlimeRecipeType.getByName(str);
        if(slimeRecipeType == SlimeRecipeType._unknown) return null;
        return mp.get(slimeRecipeType);
    }
    public static void initRecipeType(){
        recipeTypes.clear();
        registerAll(RecipeType.class);
        if(Bukkit.getPluginManager().isPluginEnabled("InfinityExpansion")){
            try{
                Field infinity_forge = InfinityWorkbench.class.getField("TYPE");
                registerRecipeType(SlimeRecipeType.infinity_forge, (RecipeType) infinity_forge.get(null));
                Field mob_data_infuser = MobDataInfuser.class.getDeclaredField("TYPE");
                mob_data_infuser.setAccessible(true);
                registerRecipeType(SlimeRecipeType.mob_data_infuser,(RecipeType) mob_data_infuser.get(null));
                Field storage_forge = StorageForge.class.getField("TYPE");
                registerRecipeType(SlimeRecipeType.storage_forge, (RecipeType) storage_forge.get(null));
            }catch (ReflectiveOperationException e){
                e.printStackTrace();
            }
        }
        if(Bukkit.getPluginManager().isPluginEnabled("ExoticGarden")){
            registerAll(ExoticGardenRecipeTypes.class);
        }
        if(Bukkit.getPluginManager().isPluginEnabled("TranscEndence")){
            registerAll(TERecipeType.class);
        }
        if(Bukkit.getPluginManager().isPluginEnabled("FlowerPower")){
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
