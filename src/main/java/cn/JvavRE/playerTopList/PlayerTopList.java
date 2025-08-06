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
    private static PlayerTopList instance;
    private static boolean papiEnabled = false; // 新增字段

    public static PlayerTopList getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // 步骤 1: 保存默认配置, 让文件在磁盘上存在
        saveDefaultConfig();

        // 步骤 2: 挂钩所有外部依赖 (Vault, PAPI)
        setupEconomy();
        setupPlaceholderAPI();

        // 步骤 3: 从磁盘加载配置文件的值到内存中
        Config.loadConfigValues();

        // 步骤 4: 初始化排行榜管理器
        ListsMgr.init();

        // 步骤 5: 注册指令
        new Command();

        // 步骤 6: 启动后台任务
        getLogger().info("正在执行首次排行榜数据更新...");
        ListsMgr.updateAllListsOnce();
        ListsMgr.startTask();

        getLogger().info("PlayerTopList 插件已成功启用。");
    }

    @Override
    public void onDisable() {
        getLogger().info("PlayerTopList 插件已卸载。");
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning("未找到 Vault, 财富榜功能将无法使用。");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().warning("未找到经济插件 (如 EssentialsX), 财富榜功能将无法使用。");
            return;
        }
        econ = rsp.getProvider();
        if (econ != null) {
            getLogger().info("已成功挂钩 Vault 经济功能。");
        } else {
            getLogger().warning("挂钩 Vault 失败，财富榜功能将无法使用。");
        }
    }

    private void setupPlaceholderAPI() {
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papiEnabled = true; // 设置为 true
            getLogger().info("检测到 PlaceholderAPI, 启用相关功能。");
            new PTLExpansion().register();
        } else {
            papiEnabled = false; // 确保为 false
            getLogger().warning("未检测到 PlaceholderAPI, 相关功能将无法使用。");
        }
    }

    public static Economy getEconomy() {
        return econ;
    }

    // 新增的 Getter 方法，供 TopListLoader 调用
    public static boolean isPapiEnabled() {
        return papiEnabled;
    }
}
