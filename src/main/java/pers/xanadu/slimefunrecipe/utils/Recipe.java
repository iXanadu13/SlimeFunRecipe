package pers.xanadu.slimefunrecipe.utils;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

public class Recipe {
    protected RecipeType recipeType;
    protected int size;
    protected ItemStack[] input;
    protected ItemStack output;
    protected String id;
    protected String file_name;
    public Recipe(final ItemStack[] input,final ItemStack result,final RecipeType type,final String id,final String file_name){
        this.input=input;
        this.size=input.length;
        this.output=result;
        this.recipeType=type;
        this.id=id;
        this.file_name=file_name;
    }
    public RecipeType getRecipeType(){
        return recipeType;
    }
    public int getSize(){
        return size;
    }
    public ItemStack[] getInput(){
        return input;
    }
    public ItemStack getOutput(){
        return output;
    }
}
