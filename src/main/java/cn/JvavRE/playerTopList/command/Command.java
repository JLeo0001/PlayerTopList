package cn.JvavRE.playerTopList.command;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.data.ListsMgr;
import cn.JvavRE.playerTopList.data.topList.AbstractTopList;
import cn.JvavRE.playerTopList.ui.UI;
import cn.JvavRE.playerTopList.utils.Digit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Command implements CommandExecutor {
    private final PlayerTopList plugin = PlayerTopList.getInstance();

    public Command() {
        // 注册命令执行
        Objects.requireNonNull(plugin.getCommand("ptl")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("ptl")).setTabCompleter(new CommandCompleter());
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
        if (!sender.hasPermission("playertoplist.admin")) {
            sender.sendMessage("你没有权限");
            return;
        }

        // 耗时太长, 改成异步加载
        Bukkit.getAsyncScheduler().runNow(plugin, task -> {
            sender.sendMessage("正在重新加载配置...");

            ListsMgr.reset();
            Config.reloadConfig();
            ListsMgr.startTask();

            sender.sendMessage("重载成功");
        });
    }

    private void onShow(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家才能使用此命令");
            return;
        }

        if (!player.hasPermission("playertoplist.view")) {
            player.sendMessage("你没有权限");
            return;
        }

        if (!(args.length > 1)) {
            player.sendMessage("用法错误");
            return;
        }

        String name = args[1];
        String pageStr = args.length > 2 ? args[2] : "1";

        if (!Digit.isDigit(pageStr)) {
            player.sendMessage("用法错误");
            return;
        }

        if (name.equalsIgnoreCase("all")) {
            UI.showListsUI(player);
            return;
        }

        int page = Integer.parseInt(pageStr);
        AbstractTopList topList = ListsMgr.getListByName(name);
        if (topList == null) {
            player.sendMessage("未找到列表");
            return;
        }

        UI.showTopListUI(player, topList, page);
    }
}
