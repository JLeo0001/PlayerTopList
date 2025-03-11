package cn.JvavRE.playerTopList;

import cn.JvavRE.playerTopList.tasks.ListsManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerTopList extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        new ListsManager(this);
        ListsManager.addNewList("time", org.bukkit.Statistic.PLAY_ONE_MINUTE);
        ListsManager.startTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
