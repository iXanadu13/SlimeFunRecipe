package xanadu.slimefunrecipe;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xanadu.slimefunrecipe.config.Lang;
import xanadu.slimefunrecipe.manager.ItemManager;
import xanadu.slimefunrecipe.utils.GuiUtils;

import java.util.Vector;

public class GUI extends ChestMenu {
    private static final Vector<ItemStack> items = new Vector<>();
    private int index;
    private int chosen;
    public GUI(String title) {
        super(title);
        index=1;
        chosen=-1;
        initVector();
    }
    public void initVector(){
        items.clear();
        for(RecipeType recipeType : ItemManager.recipeTypes){
            ItemStack item = recipeType.toItem();
            if(item == null) items.add(new CustomItemStack(Material.BARRIER, Lang.gui_RecipeType_null_name,Lang.gui_RecipeType_null_lore));
            else items.add(item);
        }
    }
    public int getPage(){
        return index;
    }
    public int getChosen(){
        return chosen;
    }
    public boolean setChosen(int slot){
        int j=(index-1)*9+slot-36;
        if(j>=items.size()) return false;
        chosen=j;
        updateChosen();
        return true;
    }
    public void setPage(Player p,int to){
        if(to<1||to>getSize()) return;
        index=to;
        fillPagedItems(to);
        updateButton(p,to);
    }
    private void updateChosen(){
        for(int i=0;i<9;i++){
            int j=(index-1)*9+i;
            this.replaceExistingItem(i+45,j==chosen? GuiUtils.getItem_chosen() : GuiUtils.getItem_not_chosen());
        }
    }
    private void fillPagedItems(int page){
        for(int i=0;i<9;i++){
            int j=(page-1)*9+i;
            this.replaceExistingItem(i+36,j<items.size()? items.get(j):GuiUtils.getBackground());
            this.replaceExistingItem(i+45,j==chosen? GuiUtils.getItem_chosen() : GuiUtils.getItem_not_chosen());
        }
    }
    private void updateButton(Player p, int page){
        this.replaceExistingItem(28, ChestMenuUtils.getPreviousButton(p,page,getSize()));
        this.replaceExistingItem(34,ChestMenuUtils.getNextButton(p,page,getSize()));
    }
    public int getSize(){
        return (items.size()-1)/9+1;
    }
}
