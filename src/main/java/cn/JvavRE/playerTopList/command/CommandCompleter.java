package cn.JvavRE.playerTopList.command;

import cn.JvavRE.playerTopList.data.ListsMgr;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandCompleter implements org.bukkit.command.TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return switch (args.length) {
            case 1 -> onSubCommand(sender, args);
            case 2 -> switch (args[0].toLowerCase()) {
                case "show" -> onShow(sender, args);
                case "reload" -> onReload(sender, args);
                default -> List.of();
            };
            default -> List.of();
        };
    }

    private List<String> onSubCommand(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (sender.hasPermission("playertoplist.admin")) list.add("reload");
        if (sender.hasPermission("playertoplist.view")) list.add("show");
        return list;
    }

    private List<String> onShow(CommandSender sender, String[] args) {
        if (!sender.hasPermission("playertoplist.view")) return List.of();

        List<String> list = new ArrayList<>(ListsMgr.getListsName());
        list.add("all");
        return list;
    }

    private List<String> onReload(CommandSender sender, String[] args) {
        return List.of();
    }
}
