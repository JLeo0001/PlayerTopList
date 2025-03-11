package cn.JvavRE.playerTopList.tasks;

import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

public record PlayerData(OfflinePlayer player, int count) {

    public static PlayerData of(OfflinePlayer player, Statistic type) {
        return new PlayerData(player, player.getStatistic(type));
    }

    //TODO: 添加自定义格式
    public String toMiniMessage() {
        return player.getName() + ": " + count;
    }
}
