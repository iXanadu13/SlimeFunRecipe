package pers.xanadu.slimefunrecipe.manager;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.xanadu.slimefunrecipe.gui.*;
import pers.xanadu.slimefunrecipe.Main;
import pers.xanadu.slimefunrecipe.config.Lang;
import pers.xanadu.slimefunrecipe.utils.GuiUtils;
import pers.xanadu.slimefunrecipe.utils.Recipe;

import static pers.xanadu.slimefunrecipe.config.Lang.*;
import static pers.xanadu.slimefunrecipe.config.Lang.plugin_file_save_error;
import static pers.xanadu.slimefunrecipe.manager.ItemManager.*;
import static pers.xanadu.slimefunrecipe.manager.RecipeManager.setRecipe;

public class GuiManager {
    public static void viewRecipe(final Player p,final String key){
        Recipe recipe = RecipeManager.getRecipe(key);
        if(recipe==null){
            p.sendMessage(command_recipe_not_found);
            return;
        }
        ItemStack[] input = recipe.getInput();
        int size = input.length;
        if(size<=9){
            viewRecipe_3x3(p,input,recipe.getOutput(),recipe.getRecipeType());
        }
        else if(size==36){
            viewRecipe_6x6(p,input,recipe.getOutput(),recipe.getRecipeType());
        }
    }
    private static void viewRecipe_3x3(final Player p,final ItemStack[] input,final ItemStack output,final RecipeType recipeType){
        GUI menu = new GUI3x3(gui_title_view);
        menu.setPlayerInventoryClickable(false);
        menu.setEmptySlotsClickable(false);
        for(int i=0;i<27;i++){
            menu.addItem(i, GuiUtils.getBackground(),ChestMenuUtils.getEmptyClickHandler());
        }
        menu.replaceExistingItem(menu.getItemShowIndex(),output);
        menu.replaceExistingItem(menu.getMachineIndex(),recipeType.toItem());
        int[] item_slot = menu.getItemSlots();
        for(int i=0;i<input.length;i++){
            menu.replaceExistingItem(item_slot[i],input[i]);
        }
        for(int i=input.length;i<9;i++){
            menu.replaceExistingItem(item_slot[i],GuiUtils.getBackground());
        }
        menu.open(p);
    }
    private static void viewRecipe_6x6(final Player p,final ItemStack[] input,final ItemStack output,final RecipeType recipeType){
        GUI menu = new GUI6x6(gui_title_view);
        menu.setPlayerInventoryClickable(false);
        menu.setEmptySlotsClickable(false);
        for(int i=0;i<54;i++){
            menu.addItem(i, GuiUtils.getBackground(),ChestMenuUtils.getEmptyClickHandler());
        }
        menu.replaceExistingItem(43,output);
        menu.replaceExistingItem(16,recipeType.toItem());
        int[] item_slot = menu.getItemSlots();
        for(int i=0;i<36;i++){
            menu.replaceExistingItem(item_slot[i],input[i]);
        }
        menu.open(p);
    }
    public static void openRecipeAddInv(final Player p,final GUISize guiSize,final String title){
        GUI menu = null;
        if(guiSize == GUISize.size_1x4){
            menu = new GUI1x4(title,GUIType.add);
            handle_1x4(menu);
        }
        else if(guiSize == GUISize.size_3x3){
            menu = new GUI3x3(title,GUIType.add);
            handle_3x3(menu);
        }
        else if(guiSize == GUISize.size_6x6){
            menu = new GUI6x6(title,GUIType.add);
            handle_6x6(menu);
        }
        if(menu != null){
            init_menu(menu,null);
            menu.open(p);
            menu.setPage(p,1);
        }
        else{
            p.sendMessage(command_unknown_item_size);
        }
    }
    public static void openSlimeInv(final Player p,final SlimefunItem si) {
        GUI menu = null;
        int size = si.getRecipe().length;
        if(size == 9){
            menu = new GUI3x3(gui_title.replaceAll("%item%",si.getItemName()));
            handle_3x3(menu);
        }
        else if(size == 36){
            menu = new GUI6x6(gui_title.replaceAll("%item%",si.getItemName()));
            handle_6x6(menu);
        }
        else if(size == 4){
            menu = new GUI1x4(gui_title.replaceAll("%item%",si.getItemName()));
            handle_1x4(menu);
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
                handle_1x4(menu);
                break;
            }
            case size_3x3: {
                menu = new GUI3x3(gui_title.replaceAll("%item%",si.getItemName()));
                handle_3x3(menu);
                break;
            }
            case size_6x6: {
                menu = new GUI6x6(gui_title.replaceAll("%item%",si.getItemName()));
                handle_6x6(menu);
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
        for (int i : menu.getBlack_border()) {
            menu.addItem(i, GuiUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : menu.getGray_border()) {
            menu.addItem(i, GuiUtils.getGray_background(), ChestMenuUtils.getEmptyClickHandler());
        }
        if(menu.getType() == GUIType.edit){
            ItemStack machine = si.getRecipeType().toItem();
            if(isEmpty(machine)) menu.replaceExistingItem(menu.getMachineIndex(), new CustomItemStack(Material.BARRIER, Lang.gui_RecipeType_null_name,Lang.gui_RecipeType_null_lore));
            else menu.replaceExistingItem(menu.getMachineIndex(), machine);
            menu.replaceExistingItem(menu.getItemShowIndex(), si.getItem());
            menu.addMenuClickHandler(menu.getItemShowIndex(),ChestMenuUtils.getEmptyClickHandler());
        }
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
        GUIType type = menu.getType();
        final int size = menu.getCapacity();
        final int[] item_slot = menu.getItemSlots();
        if(type==GUIType.edit){
            menu.addMenuClickHandler(menu.getVerifyButtonIndex(), (player, slot, item1, action) -> {
                ItemStack[] recipe = new ItemStack[size];
                for(int i=0;i<size;i++){
                    ItemStack item = menu.getItemInSlot(item_slot[i]);
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
            ItemStack[] recipe = si.getRecipe();
            int old_recipe_size = Math.min(recipe.length,size);
            menu.addMenuOpeningHandler(player -> {
                for(int i=0;i<old_recipe_size;i++){
                    menu.replaceExistingItem(item_slot[i], recipe[i]);
                }
            });
        }
        else if(type==GUIType.add){
            menu.addMenuClickHandler(menu.getVerifyButtonIndex(), (player, slot, item1, action) -> {
                ItemStack[] recipe = new ItemStack[size];
                for(int i=0;i<size;i++){
                    ItemStack item = menu.getItemInSlot(item_slot[i]);
                    if (isEmpty(item)) recipe[i] = null;
                    else recipe[i] = item;
                }
                int chosen = menu.getChosen();
                if(chosen==-1){
                    player.sendMessage(command_recipe_type_absence);
                    return false;
                }
                RecipeType recipeType = ItemManager.recipeTypes.get(chosen);
                ItemStack result = menu.getItemInSlot(menu.getItemShowIndex());
                if(recipeType==null){
                    player.sendMessage(command_recipe_type_absence);
                    return false;
                }
                String[] splits = menu.getTitle().split("\\$");
                String file_name = splits[0];
                String id = splits[1];
                if(!addRecipe(recipe,result,recipeType,id,file_name)){
                    error(plugin_file_save_error.replaceAll("\\{file_name}", file_name));
                    player.sendMessage(plugin_file_save_error.replaceAll("\\{file_name}", file_name));
                }
                else sendFeedback(player, gui_recipe_saved);
                player.closeInventory();
                return false;
            });
        }
    }
    public static void handle_3x3(final GUI menu){
        for(int i=45;i<54;i++){
            menu.addItem(i,GuiUtils.getItem_not_chosen(),ChestMenuUtils.getEmptyClickHandler());
        }
        for(int i=36;i<45;i++){
            menu.addMenuClickHandler(i,(player,slot,item,action)->{
                if(menu.setChosen(slot)) menu.replaceExistingItem(menu.getMachineIndex(),item);
                return false;
            });
        }
    }
    public static void handle_1x4(final GUI menu){
        for(int i=45;i<54;i++){
            menu.addItem(i,GuiUtils.getItem_not_chosen(),ChestMenuUtils.getEmptyClickHandler());
        }
        for(int i=36;i<45;i++){
            menu.addMenuClickHandler(i,(player,slot,item,action)->{
                if(menu.setChosen(slot)) menu.replaceExistingItem(menu.getMachineIndex(),item);
                return false;
            });
        }
    }
    public static void handle_6x6(final GUI menu){
        for(int i=0;i<6;i++){
            menu.addItem(i*9+6,GuiUtils.getItem_not_chosen(),ChestMenuUtils.getEmptyClickHandler());
        }
        for(int i=0;i<6;i++){
            menu.addMenuClickHandler(i*9+7,(player,slot,item,action)->{
                if(menu.setChosen(slot)) menu.replaceExistingItem(menu.getMachineIndex(),item);
                return false;
            });
        }
    }


}
