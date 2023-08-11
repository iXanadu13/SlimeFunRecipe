package pers.xanadu.slimefunrecipe.utils;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pers.xanadu.slimefunrecipe.config.Lang;

public class GuiUtils {
    private static final ItemStack item_chosen = new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE," ");
    private static final ItemStack item_not_chosen = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE," ");
    private static final ItemStack black_background = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE," ");
    private static final ItemStack gray_background = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE," ");
    private static final ItemStack input_background = new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE, "§9Input");
    private static final ItemStack output_background = new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,"§6Output");
    public static final int[] black_border_3x3 = {0,1,2, 6,7,8,9,10,11, 15,17,18,19,20, 24,25,26};
    public static final int[] gray_border_3x3 = {27,28,29,30,31,32,33,34,35};
    public static final int[] item_slot_3x3 = {3,4,5, 12,13,14, 21,22,23};
    public static final int[] black_border_6x6 = {35};
    public static final int[] gray_border_6x6 = {};
    public static final int[] item_slot_6x6 = {0,1,2,3,4,5,
            9,10,11,12,13,14,
            18,19,20,21,22,23,
            27,28,29,30,31,32,
            36,37,38,39,40,41,
            45,46,47,48,49,50};
    public static final int[] black_border_1x4 = {0,1,2, 6,7,8,9,10,11,13,14,15,16,17,18,19,20,21,22,23,24,25,26};
    public static final int[] item_slot_1x4 = {3,4,5,12};
    public static ItemStack getItem_chosen(){
        return item_chosen;
    }
    public static ItemStack getItem_not_chosen(){
        return item_not_chosen;
    }
    public static ItemStack getBackground(){
        return black_background;
    }
    public static ItemStack getGray_background(){
        return gray_background;
    }
    public static ItemStack getInput_background(){
        return input_background;
    }
    public static ItemStack getOutput_background(){
        return output_background;
    }


}
