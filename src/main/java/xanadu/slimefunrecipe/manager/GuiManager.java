package xanadu.slimefunrecipe.manager;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xanadu.slimefunrecipe.GUI;
import xanadu.slimefunrecipe.config.Lang;
import xanadu.slimefunrecipe.utils.GuiUtils;

import static xanadu.slimefunrecipe.Main.dataF;
import static xanadu.slimefunrecipe.config.Lang.*;
import static xanadu.slimefunrecipe.config.Lang.plugin_file_save_error;
import static xanadu.slimefunrecipe.manager.ItemManager.isEmpty;
import static xanadu.slimefunrecipe.manager.ItemManager.saveRecipe;

public class GuiManager {
    private static final int[] black_border = {0,1,2, 6,7,8,9,10,11, 15,16,17,18,19,20, 24,25,26};
    private static final int[] gray_border = {27,28,29,30,31,32,33,34,35};
    private static final int[] item_slot = {3,4,5, 12,13,14, 21,22,23};
    public static void openSlimeInv(Player p, SlimefunItem si) {
        GUI menu = new GUI(gui_title.replaceAll("%item%",si.getItemName()));
        menu.setPlayerInventoryClickable(true);
        menu.setEmptySlotsClickable(true);
        for (int i : black_border) {
            menu.addItem(i, GuiUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : gray_border) {
            menu.addItem(i, GuiUtils.getGray_background(), ChestMenuUtils.getEmptyClickHandler());
        }
        for(int i=45;i<54;i++){
            menu.addItem(i,GuiUtils.getItem_not_chosen(),ChestMenuUtils.getEmptyClickHandler());
        }
        ItemStack machine = si.getRecipeType().toItem();
        if(isEmpty(machine)) menu.replaceExistingItem(10, new CustomItemStack(Material.BARRIER, Lang.gui_RecipeType_null_name,Lang.gui_RecipeType_null_lore));
        else menu.replaceExistingItem(10, machine);
        menu.replaceExistingItem(16, si.getItem());
        menu.replaceExistingItem(8, new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, gui_button_verify));
        menu.addMenuClickHandler(8, (player, slot, item1, action) -> {
            ItemStack[] recipe = new ItemStack[9];
            for(int i=0;i<9;i++){
                ItemStack item = menu.getItemInSlot(item_slot[i]);
                if (isEmpty(item)) recipe[i] = null;
                else recipe[i] = item;
            }
            int chosen = menu.getChosen();
            if(chosen!=-1) si.setRecipeType(ItemManager.recipeTypes.get(chosen));
            si.setRecipe(recipe);
            if(!saveRecipe(recipe,si)){
                error(plugin_file_save_error.replaceAll("\\{file_name}",dataF.getName()));
                p.sendMessage(plugin_file_save_error.replaceAll("\\{file_name}",dataF.getName()));
            }
            else sendFeedback(player, gui_recipe_saved);
            player.closeInventory();
            return false;
        });
        menu.replaceExistingItem(0, new CustomItemStack(Material.RED_STAINED_GLASS_PANE, gui_button_cancel));
        menu.addMenuClickHandler(0, (player, slot, item, action) -> {
            player.closeInventory();
            sendFeedback(player, gui_recipe_cancel);
            return false;
        });
        menu.addMenuClickHandler(28,(player,slot,item,action)->{
            if(menu.getPage()==1) return false;
            menu.setPage(player,menu.getPage()-1);
            return false;
        });
        menu.addMenuClickHandler(34,(player,slot,item,action)->{
            if(menu.getPage()==menu.getSize()) return false;
            menu.setPage(player,menu.getPage()+1);
            return false;
        });
        for(int i=36;i<45;i++){
            menu.addMenuClickHandler(i,(player,slot,item,action)->{
                if(menu.setChosen(slot)) menu.replaceExistingItem(10,item);
                return false;
            });
        }
        menu.addMenuOpeningHandler(player -> {
            for(int i=0;i<9;i++){
                menu.replaceExistingItem(item_slot[i], si.getRecipe()[i]);
            }
        });
        menu.open(p);
        menu.setPage(p,1);
    }

}
