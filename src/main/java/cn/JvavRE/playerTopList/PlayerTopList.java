package cn.JvavRE.playerTopList;

import cn.JvavRE.playerTopList.command.Command;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.data.ListsMgr;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerTopList extends JavaPlugin {
    public static PlayerTopList getInstance() {
        return getPlugin(PlayerTopList.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

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
