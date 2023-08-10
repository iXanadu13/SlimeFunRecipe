package pers.xanadu.slimefunrecipe;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import pers.xanadu.slimefunrecipe.commands.MainCommand;
import pers.xanadu.slimefunrecipe.commands.TabCompleter;
import pers.xanadu.slimefunrecipe.config.Config;
import pers.xanadu.slimefunrecipe.config.Lang;
import pers.xanadu.slimefunrecipe.metrics.Metrics;

import java.io.File;
import java.util.*;

import static pers.xanadu.slimefunrecipe.config.Config.enable;
import static pers.xanadu.slimefunrecipe.config.Lang.*;
import static pers.xanadu.slimefunrecipe.manager.ItemManager.*;
import static pers.xanadu.slimefunrecipe.utils.VersionUpdater.checkUpdate;

public final class Main extends JavaPlugin {
	private static Main instance;
	public static Plugin plugin;
	public static File dataF;
	public static File langF;
	public static FileConfiguration data;
	public static FileConfiguration lang;
	@Override
	public void onEnable() {
		instance = this;
		plugin = this;
		info("Enabling plugin...");
		info("Author: Xanadu13");
		registerCommands();
		new Metrics(this,18362);
		reloadAll();
		checkUpdate();
		if(!"1.1.0".equals(Lang.version)){
			warn(Lang.plugin_wrong_file_version.replace("{file_name}",Config.lang + ".yml"));
		}
		if(!"1.0.0".equals(Config.version)){
			warn(Lang.plugin_wrong_file_version.replace("{file_name}","config.yml"));
		}
	}
	public static void reloadAll(){
		getInstance().loadFiles();
		if(enable) getInstance().loadRecipes();
	}
	private void registerCommands(){
		Objects.requireNonNull(getCommand("slimefunrecipe")).setExecutor(new MainCommand());
		Objects.requireNonNull(getCommand("slimefunrecipe")).setTabCompleter(new TabCompleter());
	}
	private void loadRecipes(){
		initRecipeType();

		int cnt=0;
		for (String key : data.getKeys(false)) {
			ConfigurationSection section = (ConfigurationSection) data.get(key);
			if(section == null){
				error(Lang.plugin_data_parsing_error+key);
				return;
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
				return;
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
		info(Lang.plugin_recipes_loaded.replace("%d",String.valueOf(cnt)));
	}
	private void loadFiles(){
		saveDefaultConfig();
		reloadConfig();
		Config.reload(getConfig());
		if (!new File(getDataFolder(), "data.yml").exists()) {
			this.saveResource("data.yml",false);
		}
		dataF = new File(getDataFolder(),"data.yml");
		data = YamlConfiguration.loadConfiguration(dataF);
		if(Config.lang.equals("")) {
			error("Key \"lang\" in config is missing, please check your config.yml.");
			Config.lang = "English";
		}
		String langPath = "lang/" + Config.lang + ".yml";
		if (!new File(getDataFolder(), langPath).exists()) {
			this.saveResource(langPath, false);
		}
		langF = new File(getDataFolder(), langPath);
		lang = YamlConfiguration.loadConfiguration(langF);
		info("Language: §6" + Config.lang);
		Lang.reload(lang);
	}
	public static Main getInstance() {
		return instance;
	}

}
