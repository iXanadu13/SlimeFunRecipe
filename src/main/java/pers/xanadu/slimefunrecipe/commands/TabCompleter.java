package pers.xanadu.slimefunrecipe.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private static final List<String> arguments_1 = Arrays.asList("reload","edit");
    private static final List<String> arguments_2 = Arrays.asList("1x4","3x3","6x6");
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
            if (args[0].equalsIgnoreCase("edit")) {
                for(String s : arguments_2){
                    if(s.toLowerCase().startsWith(args[1].toLowerCase())){
                        result.add(s);
                    }
                }
            }
            return result;
        }
        return null;
    }
}
