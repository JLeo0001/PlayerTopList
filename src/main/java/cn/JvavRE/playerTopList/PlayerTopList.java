package cn.JvavRE.playerTopList;

import cn.JvavRE.playerTopList.command.Command;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.tasks.ListsMgr;
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
        ListsMgr.init(this);
        Config.init(this);

        ListsMgr.startTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
