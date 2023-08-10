package pers.xanadu.slimefunrecipe.utils;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GuiUtils {
    private static final ItemStack item_chosen = new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE," ");
    private static final ItemStack item_not_chosen = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE," ");
    private static final ItemStack black_background = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE," ");
    private static final ItemStack gray_background = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE," ");
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


}
