package cn.JvavRE.playerTopList.data;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.config.TopListLoader;
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
        // 从配置文件加载排行榜
        TopListLoader.loadTopLists(plugin.getConfig().getConfigurationSection("lists"));
    }

    public static void updateAllListsOnce() {
        // 异步执行，避免阻塞服务器启动
        Bukkit.getAsyncScheduler().runNow(plugin, (task) -> {
            topLists.forEach(AbstractTopList::updateDataList);
            plugin.getLogger().info("首次排行榜数据更新完成");
        });
    }

    public static void startTask() {
        // 首次延迟和后续周期都使用配置的间隔
        long interval = Config.getUpdateInterval();
        updateTask = Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin,
                task -> {
                    topLists.forEach(AbstractTopList::updateDataList);
                    if (Config.isDebugOutput()) plugin.getLogger().info("排行榜已更新");
                },
                interval, // 首次延迟
                interval, // 后续周期
                TimeUnit.SECONDS
        );
    }

    public static void reset() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        topLists.clear();
        UI.resetListsUI();
    }

    public static void addNewList(AbstractTopList newList) {
        topLists.add(newList);
        UI.addTopListToUI(newList);
    }

    public static List<String> getListsNameNotHidden() {
        return topLists.stream().filter(topList -> !topList.isHidden()).map(AbstractTopList::getName).toList();
    }

    public static AbstractTopList getListByName(String name) {
        return topLists.stream().filter(topList -> topList.getName().equals(name)).findFirst().orElse(null);
    }

    public static List<AbstractTopList> getTopLists() {
        return topLists;
    }
}
