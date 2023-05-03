package xanadu.slimefunrecipe.commands;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xanadu.slimefunrecipe.config.Lang;

import static xanadu.slimefunrecipe.Main.reloadAll;
import static xanadu.slimefunrecipe.config.Config.enable;
import static xanadu.slimefunrecipe.config.Lang.*;
import static xanadu.slimefunrecipe.manager.GuiManager.openSlimeInv;
import static xanadu.slimefunrecipe.manager.ItemManager.isEmpty;

public class MainCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
		if(args.length == 0){
			sendCommandTips(sender);
			return false;
		}
		if(args.length == 1){
			if(!sender.hasPermission("slimefunrecipe.admin")){
				sendFeedback(sender, command_no_permission);
				return false;
			}
			switch (args[0].toLowerCase()){
				case "reload" : {
					//closeAllInventory();
					reloadAll();
					sendFeedback(sender,Lang.command_reload_config);
					return true;
				}
				case "edit" : {
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
//					ItemStack[] recipe = new ItemStack[]{null,null,null,null,null,null,null,null,null};
//					si.setRecipe(recipe);
					if(si==null) {
						sendFeedback(sender, command_item_error);
						return false;
					}
					openSlimeInv(p, si);
					return true;
				}
				default : {
					sendCommandTips(sender);
					return false;
				}
			}
		}
		sendCommandTips(sender);
		return true;
	}
	private static void sendCommandTips(CommandSender sender){
		for(String str : CommandTips){
			sender.sendMessage(str);
		}
	}
}
