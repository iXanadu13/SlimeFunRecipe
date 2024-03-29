package pers.xanadu.slimefunrecipe.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Iterator;

public class Config {
    public static String version;
    public static String lang;
    public static boolean enable;
    public static boolean recipe_view_tab;

    public static void reload(FileConfiguration file){
        Field[] fields = Config.class.getFields();
        for(Field field : fields){
            Type type = field.getType();
            if(type.equals(java.util.List.class) || type.equals(java.lang.String.class)){
                try{
                    field.set(null,"");
                }catch (Exception ignored){}
            }
        }
        Iterator<String> it = file.getKeys(true).iterator();
        while (it.hasNext()){
            String str = it.next();
            try{
                if(file.isConfigurationSection(str)) continue;
                Config.class.getField(str.replace(".","_")).set(null, file.get(str));
            }catch (Exception e){
                Lang.error("Config loading error! Key: "+str);
            }
        }

    }
}
