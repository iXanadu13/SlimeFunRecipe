package pers.xanadu.slimefunrecipe.manager;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.xanadu.slimefunrecipe.gui.*;
import pers.xanadu.slimefunrecipe.Main;
import pers.xanadu.slimefunrecipe.config.Lang;
import pers.xanadu.slimefunrecipe.utils.GuiUtils;

import static pers.xanadu.slimefunrecipe.config.Lang.*;
import static pers.xanadu.slimefunrecipe.config.Lang.plugin_file_save_error;
import static pers.xanadu.slimefunrecipe.manager.ItemManager.*;

public class GuiManager {
    private static final int[] black_border_3x3 = {0,1,2, 6,7,8,9,10,11, 15,16,17,18,19,20, 24,25,26};
    private static final int[] gray_border_3x3 = {27,28,29,30,31,32,33,34,35};
    private static final int[] item_slot_3x3 = {3,4,5, 12,13,14, 21,22,23};
    private static final int[] black_border_6x6 = {7,16,25,34,43,52,26,35};
    private static final int[] item_slot_6x6 = {0,1,2,3,4,5,
                                                9,10,11,12,13,14,
                                                18,19,20,21,22,23,
                                                27,28,29,30,31,32,
                                                36,37,38,39,40,41,
                                                45,46,47,48,49,50};
    private static final int[] black_border_1x4 = {0,1,2, 6,7,8,9,10,11,13,14,15,16,17,18,19,20,21,22,23,24,25,26};
    private static final int[] item_slot_1x4 = {3,4,5,12};
    public static void openSlimeInv(final Player p,final SlimefunItem si) {
        GUI menu = null;
        int size = si.getRecipe().length;
        if(size == 9){
            menu = new GUI3x3(gui_title.replaceAll("%item%",si.getItemName()));
            handle_3x3(menu,si);
        }
        else if(size == 36){
            menu = new GUI6x6(gui_title.replaceAll("%item%",si.getItemName()));
            handle_6x6(menu,si);
        }
        else if(size == 4){
            menu = new GUI1x4(gui_title.replaceAll("%item%",si.getItemName()));
            handle_1x4(menu,si);
        }
        if(menu != null){
            init_menu(menu,si);
            menu.open(p);
            menu.setPage(p,1);
        }
        else{
            p.sendMessage(command_unknown_item_size);
        }
    }
    public static void openSizedSlimeInv(final Player p, final SlimefunItem si, final GUISize size){
        GUI menu;
        switch (size){
            case size_1x4: {
                menu = new GUI1x4(gui_title.replaceAll("%item%",si.getItemName()));
                handle_1x4(menu,si);
                break;
            }
            case size_3x3: {
                menu = new GUI3x3(gui_title.replaceAll("%item%",si.getItemName()));
                handle_3x3(menu,si);
                break;
            }
            case size_6x6: {
                menu = new GUI6x6(gui_title.replaceAll("%item%",si.getItemName()));
                handle_6x6(menu,si);
                break;
            }
            default: {
                p.sendMessage(command_unknown_item_size);
                return;
            }
        }
        init_menu(menu,si);
        menu.open(p);
        menu.setPage(p,1);
    }
    private static void init_menu(final GUI menu,final SlimefunItem si){
        menu.setPlayerInventoryClickable(true);
        menu.setEmptySlotsClickable(true);
        ItemStack machine = si.getRecipeType().toItem();
        if(isEmpty(machine)) menu.replaceExistingItem(menu.getMachineIndex(), new CustomItemStack(Material.BARRIER, Lang.gui_RecipeType_null_name,Lang.gui_RecipeType_null_lore));
        else menu.replaceExistingItem(menu.getMachineIndex(), machine);
        menu.replaceExistingItem(menu.getItemShowIndex(), si.getItem());
        menu.replaceExistingItem(menu.getCancelButtonIndex(), new CustomItemStack(Material.RED_STAINED_GLASS_PANE, gui_button_cancel));
        menu.addMenuClickHandler(menu.getCancelButtonIndex(), (player, slot, item, action) -> {
            player.closeInventory();
            sendFeedback(player, gui_recipe_cancel);
            return false;
        });
        menu.replaceExistingItem(menu.getVerifyButtonIndex(), new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, gui_button_verify));
        menu.addMenuClickHandler(menu.getPrevPageButtonIndex(),(player,slot,item,action)->{
            menu.prev(player);
            return false;
        });
        menu.addMenuClickHandler(menu.getNextPageButtonIndex(),(player,slot,item,action)->{
            menu.next(player);
            return false;
        });
    }
    public static void handle_3x3(final GUI menu,final SlimefunItem si){
        for (int i : black_border_3x3) {
            menu.addItem(i, GuiUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : gray_border_3x3) {
            menu.addItem(i, GuiUtils.getGray_background(), ChestMenuUtils.getEmptyClickHandler());
        }
        for(int i=45;i<54;i++){
            menu.addItem(i,GuiUtils.getItem_not_chosen(),ChestMenuUtils.getEmptyClickHandler());
        }
        menu.addMenuClickHandler(menu.getVerifyButtonIndex(), (player, slot, item1, action) -> {
            ItemStack[] recipe = new ItemStack[9];
            for(int i=0;i<9;i++){
                ItemStack item = menu.getItemInSlot(item_slot_3x3[i]);
                if (isEmpty(item)) recipe[i] = null;
                else recipe[i] = item;
            }
            int chosen = menu.getChosen();
            if(chosen!=-1) si.setRecipeType(ItemManager.recipeTypes.get(chosen));
            si.setRecipe(recipe);
            if(!saveRecipe(recipe,si)){
                error(plugin_file_save_error.replaceAll("\\{file_name}", Main.dataF.getName()));
                player.sendMessage(plugin_file_save_error.replaceAll("\\{file_name}", Main.dataF.getName()));
            }
            else sendFeedback(player, gui_recipe_saved);
            player.closeInventory();
            return false;
        });
        for(int i=36;i<45;i++){
            menu.addMenuClickHandler(i,(player,slot,item,action)->{
                if(menu.setChosen(slot)) menu.replaceExistingItem(menu.getMachineIndex(),item);
                return false;
            });
        }
        ItemStack[] recipe = si.getRecipe();
        int size = Math.min(recipe.length,9);
        menu.addMenuOpeningHandler(player -> {
            for(int i=0;i<size;i++){
                menu.replaceExistingItem(item_slot_3x3[i], recipe[i]);
            }
        });
    }
    public static void handle_1x4(final GUI menu,final SlimefunItem si){
        for (int i : black_border_1x4) {
            menu.addItem(i, GuiUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : gray_border_3x3) {
            menu.addItem(i, GuiUtils.getGray_background(), ChestMenuUtils.getEmptyClickHandler());
        }
        for(int i=45;i<54;i++){
            menu.addItem(i,GuiUtils.getItem_not_chosen(),ChestMenuUtils.getEmptyClickHandler());
        }
        menu.addMenuClickHandler(menu.getVerifyButtonIndex(), (player, slot, item1, action) -> {
            ItemStack[] recipe = new ItemStack[4];
            for(int i=0;i<4;i++){
                ItemStack item = menu.getItemInSlot(item_slot_1x4[i]);
                if (isEmpty(item)) recipe[i] = null;
                else recipe[i] = item;
            }
            int chosen = menu.getChosen();
            if(chosen!=-1) si.setRecipeType(ItemManager.recipeTypes.get(chosen));
            setRecipe(si,recipe);
            if(!saveRecipe(recipe,si)){
                error(plugin_file_save_error.replaceAll("\\{file_name}", Main.dataF.getName()));
                player.sendMessage(plugin_file_save_error.replaceAll("\\{file_name}", Main.dataF.getName()));
            }
            else sendFeedback(player, gui_recipe_saved);
            player.closeInventory();
            return false;
        });
        for(int i=36;i<45;i++){
            menu.addMenuClickHandler(i,(player,slot,item,action)->{
                if(menu.setChosen(slot)) menu.replaceExistingItem(menu.getMachineIndex(),item);
                return false;
            });
        }
        ItemStack[] recipe = si.getRecipe();
        int size = Math.min(recipe.length,4);
        menu.addMenuOpeningHandler(player -> {
            for(int i=0;i<size;i++){
                menu.replaceExistingItem(item_slot_1x4[i], recipe[i]);
            }
        });
    }
    public static void handle_6x6(final GUI menu,final SlimefunItem si){
        for (int i : black_border_6x6) {
            menu.addItem(i, GuiUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for(int i=0;i<6;i++){
            menu.addItem(i*9+6,GuiUtils.getItem_not_chosen(),ChestMenuUtils.getEmptyClickHandler());
        }
        menu.addMenuClickHandler(menu.getVerifyButtonIndex(), (player, slot, item1, action) -> {
            ItemStack[] recipe = new ItemStack[36];
            for(int i=0;i<36;i++){
                ItemStack item = menu.getItemInSlot(item_slot_6x6[i]);
                if (isEmpty(item)) recipe[i] = null;
                else recipe[i] = item;
            }
            int chosen = menu.getChosen();
            if(chosen!=-1) si.setRecipeType(ItemManager.recipeTypes.get(chosen));
            setRecipe(si,recipe);
            if(!saveRecipe(recipe,si)){
                error(plugin_file_save_error.replaceAll("\\{file_name}", Main.dataF.getName()));
                player.sendMessage(plugin_file_save_error.replaceAll("\\{file_name}", Main.dataF.getName()));
            }
            else sendFeedback(player, gui_recipe_saved);
            player.closeInventory();
            return false;
        });
        for(int i=0;i<6;i++){
            menu.addMenuClickHandler(i*9+7,(player,slot,item,action)->{
                if(menu.setChosen(slot)) menu.replaceExistingItem(menu.getMachineIndex(),item);
                return false;
            });
        }
        ItemStack[] recipe = si.getRecipe();
        int size = Math.min(recipe.length,36);
        menu.addMenuOpeningHandler(player -> {
            for(int i=0;i<size;i++){
                menu.replaceExistingItem(item_slot_6x6[i], recipe[i]);
            }
        });
    }


}
