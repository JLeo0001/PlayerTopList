package cn.JvavRE.playerTopList.tasks;

import cn.JvavRE.playerTopList.PlayerTopList;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TaskManager {
    private static PlayerTopList plugin;
    private static ArrayList<TopList> topLists;
    private static ScheduledTask updateTask;

    public TaskManager(PlayerTopList plugin){
        TaskManager.plugin = plugin;
        topLists = new ArrayList<>();
    }

    public static void startTask(){
        updateTask = Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin,
                task -> topLists.forEach(TopList::updateTopList),
                1,
                60,
                TimeUnit.SECONDS
        );
    }

    public static void stopTask(){
        updateTask.cancel();
    }

    public static void addNewList(TopList topList){
        topLists.add(topList);
    }
}
