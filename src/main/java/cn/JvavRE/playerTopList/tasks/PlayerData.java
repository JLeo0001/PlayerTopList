package cn.JvavRE.playerTopList.tasks;

import cn.JvavRE.playerTopList.config.UIComponent;
import cn.JvavRE.playerTopList.config.UIConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.util.List;

public record PlayerData(OfflinePlayer player, int count) {

    public static PlayerData of(OfflinePlayer player, Statistic type, List<Material> materials) {
        int total = 0;

        if (!type.isSubstatistic()) {

            total = player.getStatistic(type);

        } else {
            for (Material material : materials) {
                try {

                    total += player.getStatistic(type, material);

                } catch (IllegalArgumentException e) {

                    Bukkit.getLogger().warning("在加载 " + type.name() + " 时出现错误的材料: " + material.name());

                }
            }
        }

        return new PlayerData(player, total);
    }

    //TODO: 添加自定义格式
    public String toMiniMessage() {
        String playerName = player.getName() != null ? player.getName() : "null";

        return UIConfig.get(UIComponent.ITEM)
                .replace("{playerName}", playerName)
                .replace("{spacer}", UIConfig.get(UIComponent.SPACER))
                .replace("{count}", String.valueOf(count));
    }
}
