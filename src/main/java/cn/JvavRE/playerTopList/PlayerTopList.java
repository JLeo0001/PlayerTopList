package cn.JvavRE.playerTopList;

import cn.JvavRE.playerTopList.command.Command;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.data.ListsMgr;
import cn.JvavRE.playerTopList.placeHolder.PTLExpansion;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerTopList extends JavaPlugin {
    private static Economy econ = null;

    public static PlayerTopList getInstance() {
        return getPlugin(PlayerTopList.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        // 必须先初始化配置，才能知道PAPI是否启用
        Config.init();

        new Command();

        if (Config.isPapiEnabled()) {
            getLogger().info("检测到 PlaceholderAPI, 启用相关功能");
            new PTLExpansion().register();
        } else {
            getLogger().warning("未检测到 PlaceholderAPI, 相关功能将无法使用");
        }

        if (setupEconomy()) {
            getLogger().info("检测到 Vault, 已成功挂钩经济功能");
        } else {
            getLogger().warning("未检测到 Vault 或经济插件, 财富榜将无法使用");
        }

        // 加载所有排行榜
        ListsMgr.init();

        // 首次全量更新
        getLogger().info("正在执行首次排行榜数据更新...");
        ListsMgr.updateAllListsOnce();

        // 开启定时更新任务
        ListsMgr.startTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
