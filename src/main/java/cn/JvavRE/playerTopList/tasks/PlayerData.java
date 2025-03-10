package cn.JvavRE.playerTopList.tasks;

import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

public class PlayerData {
    private OfflinePlayer player;
    private int count;

    public PlayerData(OfflinePlayer player, int count) {
        this.player = player;
        this.count = count;
    }

    public static PlayerData fromPlayer(OfflinePlayer player, Statistic type){
        return new PlayerData(player, player.getStatistic(type));
    }

    public int getCount() {
        return count;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    //TODO: 添加自定义格式
    public String toMiniMessage(){
        return player.getName() + ": " + count;
    }
}
