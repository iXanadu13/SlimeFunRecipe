package pers.xanadu.slimefunrecipe.gui;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.xanadu.slimefunrecipe.config.Lang;
import pers.xanadu.slimefunrecipe.manager.ItemManager;
import pers.xanadu.slimefunrecipe.utils.GuiUtils;

import java.util.Vector;

public abstract class GUI extends ChestMenu {
    protected static final Vector<ItemStack> items = new Vector<>();
    protected int index;
    protected int chosen;
    protected int capacity;
    protected GUIType type;
    protected String input;
    protected GUI(String title,GUIType type){
        super(title);
        index=1;
        chosen=-1;
        capacity=0;
        this.type = type;
        input = title;
        initVector();
    }
    protected GUI(String title) {
        this(title,GUIType.edit);
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
    public int getCapacity(){
        return capacity;
    }
    public GUIType getType(){
        return type;
    }
    public String getTitle(){
        return input;
    }
    public void setPage(final Player p, int to){
        if(to<1||to>getRowSize()) return;
        index=to;
        fillPagedItems(to);
        updateButton(p,to);
    }
    public void prev(final Player p){
        if(index==1) return;
        --index;
        fillPagedItems(index);
        updateButton(p,index);
    }
    public void next(final Player p){
        if(index==getRowSize()) return;
        ++index;
        fillPagedItems(index);
        updateButton(p,index);
    }
    public int getChosen(){
        return chosen;
    }
    public boolean setChosen(int slot){
        return false;
    }
    protected void updateChosen(){

    }
    protected void fillPagedItems(int page){

    }
    protected void updateButton(final Player p, int page){
        this.replaceExistingItem(getPrevPageButtonIndex(), ChestMenuUtils.getPreviousButton(p,page,getRowSize()));
        this.replaceExistingItem(getNextPageButtonIndex(), ChestMenuUtils.getNextButton(p,page,getRowSize()));
    }
    public abstract int getRowSize();
    public abstract int getCancelButtonIndex();
    public abstract int getVerifyButtonIndex();
    public abstract int getMachineIndex();
    public abstract int getItemShowIndex();
    public abstract int getPrevPageButtonIndex();
    public abstract int getNextPageButtonIndex();
    public abstract int[] getBlack_border();
    public abstract int[] getGray_border();
    public abstract int[] getItemSlots();
}
