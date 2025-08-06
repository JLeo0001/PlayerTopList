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
            case 1 -> onSubCommand(sender);
            case 2 -> {
                // 根据第一个参数提供不同的补全
                yield switch (args[0].toLowerCase()) {
                    case "show", "showtop" -> onShow(sender);
                    default -> List.of(); // reload 和其他指令不需要第二个参数的补全
                };
            }
            default -> List.of();
        };
    }

    private List<String> onSubCommand(CommandSender sender) {
        List<String> list = new ArrayList<>();
        if (sender.hasPermission("playertoplist.admin")) {
            list.add("reload");
        }
        if (sender.hasPermission("playertoplist.view")) {
            list.add("show");
            list.add("showtop"); // 添加新指令到 Tab 补全
        }
        return list;
    }

    private List<String> onShow(CommandSender sender) {
        if (!sender.hasPermission("playertoplist.view")) return List.of();

        List<String> list = new ArrayList<>(ListsMgr.getListsNameNotHidden());
        if (sender instanceof org.bukkit.entity.Player) {
            list.add("all"); // 只有玩家可以看到 all 列表
        }
        return list;
    }
}
