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

    // TODO: 优化加载方式减少占用
    public static PlayerData of(OfflinePlayer player) {
        return new PlayerData(player, 0);
    }

    public void updateCount(Statistic type, List<?> subArgs) {
        int total = 0;

        switch (type.getType()) {
            case UNTYPED -> total = player.getStatistic(type);
            case BLOCK, ITEM -> {
                List<Material> materials = subArgs.stream().filter(Material.class::isInstance).map(Material.class::cast).toList();
                for (Material material : materials) {
                    try {
                        total += player.getStatistic(type, material);
                    } catch (IllegalArgumentException e) {
                        PlayerTopList.Logger().warning("在加载 " + type.name() + " 时出现错误的材料类型: " + material.name());
                    }
                }
            }
            case ENTITY -> {
                List<EntityType> entities = subArgs.stream().filter(EntityType.class::isInstance).map(EntityType.class::cast).toList();
                for (EntityType entityType : entities) {
                    try {
                        total += player.getStatistic(type, entityType);
                    } catch (IllegalArgumentException e) {
                        PlayerTopList.Logger().warning("在加载 " + type.name() + " 时出现错误的实体类型: " + entityType.name());
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
