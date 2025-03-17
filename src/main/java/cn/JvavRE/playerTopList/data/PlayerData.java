package cn.JvavRE.playerTopList.data;

import cn.JvavRE.playerTopList.PlayerTopList;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

import java.util.List;

public class PlayerData {
    private final OfflinePlayer player;
    private int count;

    public PlayerData(OfflinePlayer player, int count) {
        this.player = player;
        this.count = count;
    }

    public static PlayerData of(OfflinePlayer player) {
        return new PlayerData(player, 0);
    }

    public void updateCount(Statistic type, List<?> subArgs) {
        int total = 0;

        // 现在只需一次必要的循环
        switch (type.getType()) {
            case UNTYPED -> total = player.getStatistic(type);
            case BLOCK, ITEM -> {
                for (Object subArg : subArgs) {
                    try {
                        if (subArg instanceof Material material) total += player.getStatistic(type, material);
                    } catch (IllegalArgumentException e) {
                        PlayerTopList.Logger().warning("在加载 " + type.name() + " 时出现错误的材料类型: " + subArg);
                    }
                }
            }
            case ENTITY -> {
                for (Object subArg : subArgs) {
                    try {
                        if (subArg instanceof EntityType entityType) total += player.getStatistic(type, entityType);
                    } catch (IllegalArgumentException e) {
                        PlayerTopList.Logger().warning("在加载 " + type.name() + " 时出现错误的实体类型: " + subArg);
                    }
                }
            }
        }

        this.count = total;
    }

    public int getCount() {
        return count;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }
}
