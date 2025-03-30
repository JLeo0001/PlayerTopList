package cn.JvavRE.playerTopList.data.playerData;

import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.util.List;

public abstract class AbstractPlayerData {
    protected final OfflinePlayer player;
    protected int count;

    protected AbstractPlayerData(OfflinePlayer player, int count) {
        this.player = player;
        this.count = count;
    }

    public abstract void updateCount(Statistic type, List<?> subArgs);

    public OfflinePlayer getPlayer() {
        return player;
    }

    public int getCount() {
        return count;
    }
}
