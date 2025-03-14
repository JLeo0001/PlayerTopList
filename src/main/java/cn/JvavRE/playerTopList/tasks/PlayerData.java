package cn.JvavRE.playerTopList.tasks;

import cn.JvavRE.playerTopList.PlayerTopList;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

import java.util.List;

public record PlayerData(OfflinePlayer player, int count) {

    public static PlayerData of(OfflinePlayer player, Statistic type, List<?> subArgs) {
        int total = 0;

        if (!type.isSubstatistic()) total = player.getStatistic(type);
        else if (type.isBlock()) {
            List<Material> materials = subArgs.stream().filter(Material.class::isInstance).map(Material.class::cast).toList();
            for (Material material : materials) {
                try {
                    total += player.getStatistic(type, material);
                } catch (IllegalArgumentException e) {
                    PlayerTopList.Logger().warning("在加载 " + type.name() + " 时出现错误的材料类型: " + material.name());
                }
            }
        } else {
            List<EntityType> entities = subArgs.stream().filter(EntityType.class::isInstance).map(EntityType.class::cast).toList();
            for (EntityType entityType : entities) {
                try {
                    total += player.getStatistic(type, entityType);
                } catch (IllegalArgumentException e) {
                    PlayerTopList.Logger().warning("在加载 " + type.name() + " 时出现错误的实体类型: " + entityType.name());
                }
            }
        }

        return new PlayerData(player, total);
    }
}
