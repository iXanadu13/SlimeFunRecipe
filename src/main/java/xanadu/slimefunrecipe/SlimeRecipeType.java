package xanadu.slimefunrecipe;

import java.util.Map;
import java.util.TreeMap;

public enum SlimeRecipeType {
    ancient_altar,
    armor_forge,
    barter_drop,
    compressor,
    enhanced_crafting_table,
    food_composter,
    food_fabricator,
    freezer,
    geo_miner,
    gold_pan,
    grind_stone,
    heated_pressure_chamber,
    interact,
    juicer,
    magic_workbench,
    mob_drop,
    multiblock,
    nuclear_reactor,
    NULL,
    ore_crusher,
    ore_washer,
    pressure_chamber,
    refinery,
    smeltery,
    _unknown;
    public static final Map<String, SlimeRecipeType> mp = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    public static SlimeRecipeType getByName(String str){
        if(str == null) return _unknown;
        str=str.toLowerCase();
        if("null".equals(str)) return NULL;
        return mp.getOrDefault(str,_unknown);
    }
    static{
        SlimeRecipeType[] values = values();
        for (SlimeRecipeType recipeType : values) {
            String name = recipeType.name();
            mp.put(name, recipeType);
        }
    }
}
