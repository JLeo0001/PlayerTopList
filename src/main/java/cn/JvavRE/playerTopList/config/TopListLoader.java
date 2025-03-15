package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.tasks.ListsMgr;
import cn.JvavRE.playerTopList.utils.SubStatistic;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class TopListLoader {
    private static boolean isStatistic(String type) {
        try {
            Statistic.valueOf(type);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isName(String name) {
        return name != null && !name.isEmpty();
    }

    private static void addTopListToManager(String name, String type, List<String> materialNames, List<String> entityNames) {
        if (!isName(name)) {
            PlayerTopList.Logger().warning("不是有效的列表名称: '" + name + "'");
            return;
        }

        if (!isStatistic(type)) {
            PlayerTopList.Logger().warning("不是有效的统计类型: '" + type + "'");
            return;
        }

        // 材料类型加载
        List<Material> materials = new ArrayList<>();
        for (String materialName : materialNames) {
            Material material = Material.matchMaterial(materialName);
            if (material != null) {
                materials.add(material);
            } else if (SubStatistic.isValid(materialName)) {
                materials.addAll(SubStatistic.getMaterials(materialName));
            } else {
                PlayerTopList.Logger().warning(name + " -> 不是有效的材料: '" + materialName + "'");
            }
        }

        // 实体类型加载
        List<EntityType> entities = new ArrayList<>();
        for (String entityName : entityNames) {
            EntityType entityType = EntityType.fromName(entityName);
            if (entityType != null) {
                entities.add(entityType);
            } else if (SubStatistic.isValid(entityName)) {
                entities.addAll(SubStatistic.getEntities(entityName));
            } else {
                PlayerTopList.Logger().warning(name + " -> 不是有效的实体: '" + entityName + "'");
            }
        }

        // 去重
        materials = materials.stream().distinct().toList();
        entities = entities.stream().distinct().toList();

        // 根据type类型设置对应子项目
        Statistic statistic = Statistic.valueOf(type);
        if (statistic.isBlock()) {
            ListsMgr.addNewList(name, Statistic.valueOf(type), materials);
        } else {
            ListsMgr.addNewList(name, Statistic.valueOf(type), entities);
        }

        PlayerTopList.Logger().info("成功添加列表: " + name + " (" + type + ")");
    }

    protected static void loadTopLists(ConfigurationSection section) {
        if (section == null) return;

        // 遍历榜单项目
        for (String name : section.getKeys(false)) {
            ConfigurationSection listSection = section.getConfigurationSection(name);
            if (listSection == null) continue;

            // 榜单参数
            String type = listSection.getString("type");
            List<String> material = listSection.getStringList("material");
            List<String> entity = listSection.getStringList("entity");

            addTopListToManager(name, type, material, entity);
        }
    }
}
