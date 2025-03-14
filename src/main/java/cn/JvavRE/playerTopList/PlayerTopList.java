package cn.JvavRE.playerTopList;

import cn.JvavRE.playerTopList.command.Command;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.tasks.ListsManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class PlayerTopList extends JavaPlugin {
    private static Logger logger;

    public static Logger Logger() {
        return logger;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        logger = getLogger();

        new Command(this);
        ListsManager.init(this);
        Config.init(this);

        ListsManager.startTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
