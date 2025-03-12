package cn.JvavRE.playerTopList.tasks;

import cn.JvavRE.playerTopList.config.UIComponent;
import cn.JvavRE.playerTopList.config.UIConfig;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

public record PlayerData(OfflinePlayer player, int count) {

    public static PlayerData of(OfflinePlayer player, Statistic type) {
        return new PlayerData(player, player.getStatistic(type));
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
