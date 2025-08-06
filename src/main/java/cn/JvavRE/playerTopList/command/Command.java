package cn.JvavRE.playerTopList.command;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.data.ListsMgr;
import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import cn.JvavRE.playerTopList.data.topList.AbstractTopList;
import cn.JvavRE.playerTopList.ui.UI;
import cn.JvavRE.playerTopList.utils.Digit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
            switch (args[0].toLowerCase()) {
                case "reload" -> onReload(sender);
                case "show" -> onShow(sender, args);
                case "showtop" -> onShowTop(sender, args); // 新增指令处理
                default -> sender.sendMessage("用法错误: /ptl <reload|show|showtop>");
            }
            return true;
        }

        sender.sendMessage("用法错误: /ptl <reload|show|showtop>");
        return true;
    }

    private void onReload(CommandSender sender) {
        if (!sender.hasPermission("playertoplist.admin")) {
            sender.sendMessage("你没有权限");
            return;
        }

        // 耗时太长, 改成异步加载
        Bukkit.getAsyncScheduler().runNow(plugin, task -> {
            sender.sendMessage("正在重新加载配置...");
            ListsMgr.reset();
            Config.reloadConfig();
            ListsMgr.init(); // 重新加载榜单
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

        if (args.length < 2) {
            player.sendMessage("用法错误: /ptl show <排行榜名称> [页码]");
            return;
        }

        String name = args[1];
        String pageStr = args.length > 2 ? args[2] : "1";

        if (!Digit.isDigit(pageStr)) {
            player.sendMessage("页码必须是数字");
            return;
        }

        if (name.equalsIgnoreCase("all")) {
            UI.showListsUI(player);
            return;
        }

        int page = Integer.parseInt(pageStr);
        AbstractTopList topList = ListsMgr.getListByName(name);
        if (topList == null) {
            player.sendMessage("未找到名为 '" + name + "' 的排行榜");
            return;
        }

        UI.showTopListUI(player, topList, page);
    }

    // 新增的方法，用于处理 showtop 指令
    private void onShowTop(CommandSender sender, String[] args) {
        // 该指令允许后台和机器人使用
        if (args.length < 2) {
            sender.sendMessage("用法错误: /ptl showtop <排行榜名称>");
            return;
        }

        String listName = args[1];
        AbstractTopList topList = ListsMgr.getListByName(listName);
        if (topList == null) {
            sender.sendMessage("未找到名为 '" + listName + "' 的排行榜");
            return;
        }

        List<PlayerData> dataList = topList.getDataList();
        if (dataList.isEmpty()) {
            sender.sendMessage(topList.getName() + " 排行榜暂无数据");
            return;
        }

        StringBuilder result = new StringBuilder();
        result.append("--- ").append(topList.getName()).append(" Top 10 ---").append("\n");

        int limit = Math.min(10, dataList.size());

        for (int i = 0; i < limit; i++) {
            PlayerData playerData = dataList.get(i);
            String playerName = playerData.getPlayer().getName();
            if (playerName == null) {
                playerName = "未知玩家";
            }
            String formattedCount = topList.getFormattedData(playerData);

            result.append(i + 1)
                    .append(". ")
                    .append(playerName)
                    .append(" - ")
                    .append(formattedCount);

            if (i < limit - 1) {
                result.append("\n");
            }
        }

        sender.sendMessage(result.toString());
    }
}
