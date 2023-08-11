package pers.xanadu.slimefunrecipe;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import static pers.xanadu.slimefunrecipe.manager.RecipeManager.loadRecipes;
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
		if(!"1.2.0".equals(Lang.version)){
			warn(Lang.plugin_wrong_file_version.replace("{file_name}",Config.lang + ".yml"));
		}
		if(!"1.2.0".equals(Config.version)){
			warn(Lang.plugin_wrong_file_version.replace("{file_name}","config.yml"));
		}
	}
	public static void reloadAll(){
		getInstance().loadFiles();
		if(enable) loadRecipes();
	}
	private void registerCommands(){
		Objects.requireNonNull(getCommand("slimefunrecipe")).setExecutor(new MainCommand());
		Objects.requireNonNull(getCommand("slimefunrecipe")).setTabCompleter(new TabCompleter());
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
