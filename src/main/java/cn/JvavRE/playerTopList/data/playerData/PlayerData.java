package cn.JvavRE.playerTopList.data.playerData;

import org.bukkit.OfflinePlayer;

public class PlayerData {
    protected final OfflinePlayer player;
    protected int count;

    public PlayerData(OfflinePlayer player) {
        this.player = player;
        this.count = 0;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
