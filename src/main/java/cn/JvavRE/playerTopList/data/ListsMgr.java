package cn.JvavRE.playerTopList.data;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.data.topList.AbstractTopList;
import cn.JvavRE.playerTopList.ui.UI;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListsMgr {
    private static final PlayerTopList plugin = PlayerTopList.getInstance();
    private static List<AbstractTopList> topLists;
    private static ScheduledTask updateTask;

    public static void init() {
        topLists = new ArrayList<>();
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

    public static void reset() {
        stopTask();
        topLists.clear();
        UI.resetListsUI();
    }

    public static void stopTask() {
        updateTask.cancel();
    }

    public static void addNewList(AbstractTopList newList) {
        topLists.add(newList);
        UI.addTopListToUI(newList);
    }

    public static List<String> getListsName() {
        return topLists.stream().map(AbstractTopList::getName).toList();
    }

    public static AbstractTopList getListByName(String name) {
        return topLists.stream().filter(topList -> topList.getName().equals(name)).findFirst().orElse(null);
    }
}
