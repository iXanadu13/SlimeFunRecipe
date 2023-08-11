package pers.xanadu.slimefunrecipe.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pers.xanadu.slimefunrecipe.config.Config;

import java.util.*;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private static final List<String> arguments_1 = Arrays.asList("reload","add","edit","view");
    private static final List<String> arguments_2 = Arrays.asList("1x4","3x3","6x6");
    public static final List<String> recipe_add_files = new ArrayList<>();
    public static final Map<String,List<String>> recipe_mp = new HashMap<>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            for (String s : arguments_1) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(s);
                }
            }
            return result;
        }
        else if(args.length==2){
            if (args[0].equals("add") || args[0].equals("edit")) {
                for(String s : arguments_2){
                    if(s.toLowerCase().startsWith(args[1].toLowerCase())){
                        result.add(s);
                    }
                }
            }
            else if(Config.recipe_view_tab && args[0].equals("view")){
                for(String s : recipe_add_files){
                    if(s.toLowerCase().startsWith(args[1].toLowerCase())){
                        result.add(s);
                    }
                }
            }
            return result;
        }
        else if(args.length==3){
            if(args[0].equals("add")){
                if("id".startsWith(args[2].toLowerCase())){
                    result.add("id");
                }
            }
            else if(Config.recipe_view_tab && args[0].equals("view")){
                List<String> list = recipe_mp.get(args[1]);
                if(list!=null){
                    for(String s : list){
                        if(s.toLowerCase().startsWith(args[2].toLowerCase())){
                            result.add(s);
                        }
                    }
                }
            }
            return result;
        }
        else if(args.length==4){
            if(args[0].equals("add")){
                for(String s : recipe_add_files){
                    if(s.toLowerCase().startsWith(args[3].toLowerCase())){
                        result.add(s);
                    }
                }
            }
            return result;
        }
        return null;
    }
}
