package cn.JvavRE.playerTopList.data;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.data.topList.AbstractTopList;
import cn.JvavRE.playerTopList.data.topList.StatisticTopList;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.objecthunter.exp4j.Expression;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListsMgr {
    private static TextComponent.Builder listsUI;
    private static PlayerTopList plugin;
    private static List<AbstractTopList> topLists;
    private static ScheduledTask updateTask;

    public static void init(PlayerTopList plugin) {
        ListsMgr.plugin = plugin;
        topLists = new ArrayList<>();

        initListsUI();
    }

    public static void startTask() {
        // 运行更新任务
        updateTask = Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin,
                task -> {
                    topLists.forEach(AbstractTopList::updateDataList);
                    plugin.getLogger().info("排行榜已更新");
                },
                1,
                Config.getUpdateInterval(),
                TimeUnit.SECONDS
        );
    }

    private static void initListsUI() {
        // 初始化列表UI
        listsUI = Component.text();
        listsUI.append(Component.text("==================").decorate(TextDecoration.BOLD)).appendNewline();
    }

    public static void reset() {
        stopTask();
        topLists.clear();
        initListsUI();
    }

    public static void stopTask() {
        updateTask.cancel();
    }

    public static void addStatisticList(String name, TextColor color, Statistic type, List<?> subArgs,
                                        Expression expression, String formatter) {

        StatisticTopList newList = new StatisticTopList(name, color, type, subArgs, expression, formatter);
        topLists.add(newList);

        // 列表UI添加项目
        listsUI.append(newList.getColoredName()
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/ptl show " + name))
                .hoverEvent(HoverEvent.showText(Component.text("点击查看排行榜")))
        ).appendSpace();
    }

    public static List<String> getListsName() {
        return topLists.stream().map(AbstractTopList::getName).toList();
    }

    public static void showLists(Player player) {
        player.sendMessage(listsUI.build());
    }

    public static AbstractTopList getListByName(String name) {
        return topLists.stream().filter(topList -> topList.getName().equals(name)).findFirst().orElse(null);
    }
}
