package cn.JvavRE.playerTopList;

import cn.JvavRE.playerTopList.command.Command;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.data.ListsMgr;
import cn.JvavRE.playerTopList.placeHolder.PTLExpansion;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerTopList extends JavaPlugin {
    public static PlayerTopList getInstance() {
        return getPlugin(PlayerTopList.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        ListsMgr.init();
        Config.init();

        new Command();

        if (Config.isPapiEnabled()) {
            getLogger().info("检测到 PlaceholderAPI, 启用相关功能");
            new PTLExpansion().register();
        }else {
            getLogger().warning("未检测到 PlaceholderAPI, 相关功能将无法使用");
        }

        ListsMgr.startTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
