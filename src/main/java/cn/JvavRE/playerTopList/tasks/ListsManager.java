package cn.JvavRE.playerTopList.tasks;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ListsManager {
    private static PlayerTopList plugin;
    private static ArrayList<TopList> topLists;
    private static ScheduledTask updateTask;

    public static void init(PlayerTopList plugin) {
        ListsManager.plugin = plugin;
        topLists = new ArrayList<>();
    }

    public static void startTask() {
        updateTask = Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin,
                task -> topLists.forEach(TopList::updateTopList),
                1,
                Config.getUpdateInterval(),
                TimeUnit.SECONDS
        );
    }

    public static void stopTask() {
        updateTask.cancel();
    }

    public static void restartTask() {
        stopTask();
        startTask();
    }

    public static void addNewList(String name, Statistic type) {
        topLists.add(new TopList(name, type));
    }

    public static TopList getListByName(String name) {
        return topLists.stream().filter(topList -> topList.getName().equals(name)).findFirst().orElse(null);
    }

    public static TopList getListByType(Statistic type) {
        return topLists.stream().filter(topList -> topList.getType().equals(type)).findFirst().orElse(null);
    }
}
