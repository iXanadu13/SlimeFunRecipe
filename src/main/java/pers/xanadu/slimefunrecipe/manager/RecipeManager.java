package pers.xanadu.slimefunrecipe.manager;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pers.xanadu.slimefunrecipe.commands.TabCompleter;
import pers.xanadu.slimefunrecipe.config.Lang;
import pers.xanadu.slimefunrecipe.utils.Recipe;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import static pers.xanadu.slimefunrecipe.Main.data;
import static pers.xanadu.slimefunrecipe.Main.plugin;
import static pers.xanadu.slimefunrecipe.config.Lang.error;
import static pers.xanadu.slimefunrecipe.config.Lang.info;
import static pers.xanadu.slimefunrecipe.manager.ItemManager.*;

public class RecipeManager {
    private static final Map<String, Recipe> mp = new HashMap<>();
    public static Recipe getRecipe(final String key){
        return mp.get(key);
    }
    public static boolean isKeyConflict(final String key){
        return mp.containsKey(key);
    }
    public static boolean isKeyConflict(final String id,final String file_name){
        String key = file_name+"$"+id;
        return mp.containsKey(key);
    }
    public static void loadRecipes(){
        try{
            initRecipeType();
        }catch (Exception e){
            e.printStackTrace();
        }
        int cnt=0;
        for (String key : data.getKeys(false)) {
            ConfigurationSection section = (ConfigurationSection) data.get(key);
            if(section == null){
                error(Lang.plugin_data_parsing_error+key);
                continue;
            }
            RecipeType type = getByName(section.getString("RecipeType"));
            if(type == null) {
                error(Lang.item_unknown_recipe_type.replace("%item%",key));
                continue;
            }
            SlimefunItem si = SlimefunItem.getById(key);
            if(si == null){
                error(Lang.item_not_slimefun.replaceAll("%item%",key));
                continue;
            }
            ConfigurationSection section1 = section.getConfigurationSection("data");
            if(section1 == null){
                error(Lang.plugin_data_parsing_error+key);
                continue;
            }
            int size = section.getInt("size",9);
            ItemStack[] recipe = new ItemStack[size];
            for(int i=0;i<size;i++){
                recipe[i] = readAsItem(section1, String.valueOf(i+1));
            }
            si.setRecipeType(type);
            setRecipe(si,recipe);
            cnt++;
        }
        int add = loadAddedRecipes();
        cnt+=add;
        info(Lang.plugin_recipes_loaded.replace("%d",String.valueOf(cnt)));
    }
    public static int loadAddedRecipes(){
        TabCompleter.recipe_add_files.clear();
        TabCompleter.recipe_mp.clear();
        File folder = new File(plugin.getDataFolder(),"recipe/add");
        if(!folder.exists()) return 0;
        File[] files = folder.listFiles();
        if(files == null) return 0;
        int cnt = 0;
        for(File file : files){
            if(!file.getName().endsWith(".yml")) continue;
            Lang.info(Lang.plugin_read_file + file.getName());
            TabCompleter.recipe_add_files.add(file.getName());
            List<String> keys = new ArrayList<>();
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
            Iterator it = fileConfiguration.getKeys(false).iterator();
            while (it.hasNext()){
                String name = (String) it.next();
                String key = file.getPath()+"("+name+")";
                ConfigurationSection section = fileConfiguration.getConfigurationSection(name);
                if(section == null){
                    error(Lang.plugin_data_parsing_error+key);
                    continue;
                }
                RecipeType type = getByName(section.getString("RecipeType"));
                if(type == null) {
                    error(Lang.item_unknown_recipe_type.replace("%item%",key));
                    continue;
                }
                ConfigurationSection section1 = section.getConfigurationSection("input");
                if(section1 == null){
                    error(Lang.plugin_data_parsing_error+key);
                    continue;
                }
                int size = section.getInt("size");
                ItemStack[] input = new ItemStack[size];
                for(int i=0;i<size;i++){
                    input[i] = readAsItem(section1, String.valueOf(i+1));
                }
                ItemStack output = readAsItem(section,"output");
                if(isEmpty(output)) continue;
                registerRecipe(type,input,output);
                String recipe_key = file.getName()+"$"+name;
                mp.put(recipe_key,new Recipe(input,output,type,name,file.getName()));
                keys.add(name);
                ++cnt;
            }
            TabCompleter.recipe_mp.put(file.getName(),keys);
        }
        return cnt;
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
    private static void registerRecipe(RecipeType type,ItemStack[] input,ItemStack result){
        try{
            type.register(input,result);
        }catch (Exception e){
            //e.printStackTrace();
            SlimefunItem si = SlimefunItem.getByItem(result);
            if (si != null) {
                type.register(input,si.getItem());
            }
        }
    }
}
