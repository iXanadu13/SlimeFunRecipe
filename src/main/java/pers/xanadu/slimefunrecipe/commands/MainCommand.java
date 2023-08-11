package pers.xanadu.slimefunrecipe.commands;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.xanadu.slimefunrecipe.config.Lang;
import pers.xanadu.slimefunrecipe.gui.GUISize;
import pers.xanadu.slimefunrecipe.manager.GuiManager;
import pers.xanadu.slimefunrecipe.manager.RecipeManager;

import static pers.xanadu.slimefunrecipe.Main.reloadAll;
import static pers.xanadu.slimefunrecipe.config.Config.enable;
import static pers.xanadu.slimefunrecipe.config.Lang.*;
import static pers.xanadu.slimefunrecipe.manager.GuiManager.openSizedSlimeInv;
import static pers.xanadu.slimefunrecipe.manager.GuiManager.openSlimeInv;
import static pers.xanadu.slimefunrecipe.manager.ItemManager.isEmpty;

public class MainCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
		if(args.length == 0){
			sendCommandTips(sender);
			return false;
		}
		switch (args[0].toLowerCase()){
			case "reload" : {
				if(!sender.hasPermission("slimefunrecipe.admin")){
					sendFeedback(sender, command_no_permission);
					return false;
				}
				if(args.length != 1){
					sendCommandTips(sender);
					return false;
				}
				reloadAll();
				sendFeedback(sender,Lang.command_reload_config);
				return true;
			}
			//sfr add [size] [id] (file.yml)
			case "add" : {
				if(!sender.hasPermission("slimefunrecipe.admin")){
					sendFeedback(sender, command_no_permission);
					return false;
				}
				if (!(sender instanceof Player)) {
					sendFeedback(sender, Lang.command_only_player);
					return false;
				}
				Player p = (Player) sender;
				if(args.length == 3){
					GUISize size = GUISize.of(args[1]);
					String id = args[2];
					String file_name = id+".yml";
					String key = file_name+"$"+id;
					if(RecipeManager.isKeyConflict(key)){
						sendFeedback(p, command_key_conflict);
						return false;
					}
					else GuiManager.openRecipeAddInv(p,size,key);
					return true;
				}
				else if(args.length == 4){
					GUISize size = GUISize.of(args[1]);
					String id = args[2];
					String file_name = args[3];
					String key = file_name+"$"+id;
					if(RecipeManager.isKeyConflict(key)){
						sendFeedback(p, command_key_conflict);
						return false;
					}
					else GuiManager.openRecipeAddInv(p,size,key);
					return true;
				}
				break;
			}
			case "edit" : {
				if(!sender.hasPermission("slimefunrecipe.admin")){
					sendFeedback(sender, command_no_permission);
					return false;
				}
				if(!enable){
					sendFeedback(sender,plugin_fun_not_enable);
					return false;
				}
				if (!(sender instanceof Player)) {
					sendFeedback(sender, Lang.command_only_player);
					return false;
				}
				Player p = (Player) sender;
				ItemStack item = p.getItemInHand();
				if(isEmpty(item)){
					sendFeedback(sender, command_item_error);
					return false;
				}
				SlimefunItem si = SlimefunItem.getByItem(item);
				if(si==null) {
					sendFeedback(sender, command_item_error);
					return false;
				}
				if(args.length == 1){
					openSlimeInv(p, si);
					return true;
				}
				else if(args.length == 2){
					openSizedSlimeInv(p,si, GUISize.of(args[1]));
					return true;
				}
				break;
			}
			case "view" : {
				if(!sender.hasPermission("slimefunrecipe.view")){
					sendFeedback(sender, command_no_permission);
					return false;
				}
				if(!enable){
					sendFeedback(sender,plugin_fun_not_enable);
					return false;
				}
				if (!(sender instanceof Player)) {
					sendFeedback(sender, Lang.command_only_player);
					return false;
				}
				Player p = (Player) sender;
				if(args.length == 2){
					GuiManager.viewRecipe(p,args[1]);
					return true;
				}
				else if(args.length == 3){
					GuiManager.viewRecipe(p,args[1]+"$"+args[2]);
					return true;
				}
				break;
			}
		}
		sendCommandTips(sender);
		return false;
	}
	private static void sendCommandTips(CommandSender sender){
		for(String str : CommandTips){
			sender.sendMessage(str);
		}
	}
}
