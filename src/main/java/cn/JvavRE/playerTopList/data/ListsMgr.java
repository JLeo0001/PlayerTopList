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
        // 关键：首先创建列表实例
        topLists = new ArrayList<>();
        // 然后才从配置文件加载排行榜到这个已存在的列表中
        TopListLoader.loadTopLists(plugin.getConfig().getConfigurationSection("lists"));
    }

    public static void updateAllListsOnce() {
        // 异步执行，避免阻塞服务器
        Bukkit.getAsyncScheduler().runNow(plugin, (task) -> {
            topLists.forEach(AbstractTopList::updateDataList);
            plugin.getLogger().info("首次排行榜数据更新完成。");
        });
    }

    public static void startTask() {
        long interval = Config.getUpdateInterval();
        // 首次延迟设为1秒，避免和服务器启动的其他任务冲突
        updateTask = Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin,
                task -> {
                    topLists.forEach(AbstractTopList::updateDataList);
                    if (Config.isDebugOutput()) plugin.getLogger().info("排行榜已更新");
                },
                1,
                interval,
                TimeUnit.SECONDS
        );
    }

    public static void reset() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        if (topLists != null) {
            topLists.clear();
        }
        UI.resetListsUI();
    }

    public static void addNewList(AbstractTopList newList) {
        // 在这里加一个安全检查，以防万一
        if (topLists == null) {
            plugin.getLogger().severe("ListsMgr.topLists 未初始化！无法添加新的排行榜！");
            return;
        }
        topLists.add(newList);
        UI.addTopListToUI(newList);
    }

    public static List<String> getListsNameNotHidden() {
        if (topLists == null) return new ArrayList<>();
        return topLists.stream().filter(topList -> !topList.isHidden()).map(AbstractTopList::getName).toList();
    }

    public static AbstractTopList getListByName(String name) {
        if (topLists == null) return null;
        return topLists.stream().filter(topList -> topList.getName().equals(name)).findFirst().orElse(null);
    }

    public static List<AbstractTopList> getTopLists() {
        if (topLists == null) return new ArrayList<>();
        return topLists;
    }
}
