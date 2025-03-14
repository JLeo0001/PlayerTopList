package cn.JvavRE.playerTopList.command;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.tasks.ListsMgr;
import cn.JvavRE.playerTopList.tasks.TopList;
import cn.JvavRE.playerTopList.utils.Digit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Command implements CommandExecutor {
    private final PlayerTopList plugin;

    public Command(PlayerTopList plugin) {
        this.plugin = plugin;
        plugin.getCommand("ptl").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "reload" -> onReload(sender, args);
                case "show" -> onShow(sender, args);
                default -> sender.sendMessage("用法错误");
            }
            return true;
        }

        sender.sendMessage("用法错误");
        return true;
    }

    private void onReload(CommandSender sender, String[] args) {
    }

    private void onShow(CommandSender sender, String[] args) {
        if (!sender.hasPermission("playertoplist.view")) {
            sender.sendMessage("你没有权限");
            return;
        }

        if (!(args.length > 1)) {
            sender.sendMessage("用法错误");
            return;
        }

        String name = args[1];
        String pageStr = args.length > 2 ? args[2] : "1";

        if (!Digit.isDigit(pageStr)) {
            sender.sendMessage("用法错误");
            return;
        }

        int page = Integer.parseInt(pageStr);
        TopList topList = ListsMgr.getListByName(name);

        if (topList == null) {
            sender.sendMessage("未找到列表");
            return;
        }

        sender.sendMessage(topList.toComponent(page));
    }
}
