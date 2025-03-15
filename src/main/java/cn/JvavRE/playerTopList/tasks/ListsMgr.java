package cn.JvavRE.playerTopList.tasks;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListsMgr {
    private static TextComponent.Builder listsUI;
    private static PlayerTopList plugin;
    private static ArrayList<TopList> topLists;
    private static ScheduledTask updateTask;

    public static void init(PlayerTopList plugin) {
        ListsMgr.plugin = plugin;
        listsUI = Component.text();
        topLists = new ArrayList<>();
    }

    public static void startTask() {
        updateTask = Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin,
                task -> {
                    topLists.forEach(TopList::updateTopList);
                    plugin.getLogger().info("排行榜已更新");
                },
                1,
                Config.getUpdateInterval(),
                TimeUnit.SECONDS
        );
    }

    public static void stopTask() {
        updateTask.cancel();
    }

    public static void restart() {
        stopTask();
        topLists.clear();
        listsUI = Component.text();
        startTask();
    }

    public static void addNewList(String name, Statistic type, List<?> subArgs) {
        topLists.add(new TopList(name, type, subArgs));

        listsUI.append(Component.text(" [" + name + "] ")
                .decorate(TextDecoration.BOLD)
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/ptl show " + name))
                .hoverEvent(HoverEvent.showText(Component.text("点击查看排行榜[" + name + "]")))
        );
    }

    public static void showLists(Player player) {
        player.sendMessage(listsUI.build());
    }

    public static TopList getListByName(String name) {
        return topLists.stream().filter(topList -> topList.getName().equals(name)).findFirst().orElse(null);
    }

    public static TopList getListByType(Statistic type) {
        return topLists.stream().filter(topList -> topList.getType().equals(type)).findFirst().orElse(null);
    }
}
