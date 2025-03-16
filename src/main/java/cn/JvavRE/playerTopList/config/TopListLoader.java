package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.data.ListsMgr;
import cn.JvavRE.playerTopList.utils.SubStatistic;
import net.kyori.adventure.text.format.TextColor;
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

    private static boolean isColor(String color) {
        TextColor r = TextColor.fromHexString(color);
        return r != null;
    }

    private static void addTopListToManager(String name, String nameColor, String type, List<String> materialNames, List<String> entityNames) {
        if (!isName(name)) {
            PlayerTopList.Logger().warning("不是有效的列表名称: '" + name + "'");
            return;
        }

        if (!isStatistic(type)) {
            PlayerTopList.Logger().warning("不是有效的统计类型: '" + type + "'");
            return;
        }

        if (!isColor(nameColor)) {
            PlayerTopList.Logger().warning("不是有效的颜色: '" + nameColor + "'" + ", 使用默认颜色");
            nameColor = "#FFFFFF";
        }

        TextColor color = TextColor.fromHexString(nameColor);

        // 根据类型处理子参数列表
        Statistic statistic = Statistic.valueOf(type);
        switch (statistic.getType()) {
            case ENTITY -> {
                // 实体类型加载
                List<EntityType> entities = new ArrayList<>();

                if (entityNames.isEmpty()) {
                    PlayerTopList.Logger().warning(name + " -> 实体类型列表为空, 使用默认实体");
                    entities = SubStatistic.getEntities("alive");
                }

                for (String entityName : entityNames) {
                    EntityType entityType;

                    try {
                        entityType = EntityType.valueOf(entityName.toUpperCase());
                    } catch (Exception e) {
                        entityType = null;
                    }

                    if (entityType != null) {
                        entities.add(entityType);
                    } else if (SubStatistic.isValid(entityName)) {
                        entities.addAll(SubStatistic.getEntities(entityName));
                    } else {
                        PlayerTopList.Logger().warning(name + " -> 不是有效的实体: '" + entityName + "'");
                    }
                }

                // 去重
                entities = entities.stream().distinct().toList();

                ListsMgr.addNewList(name, color, Statistic.valueOf(type), entities);
            }
            case BLOCK -> {
                // 材料类型加载
                List<Material> materials = new ArrayList<>();

                if (materialNames.isEmpty()) {
                    PlayerTopList.Logger().warning(name + " -> 材料类型列表为空, 使用默认材料");
                    materials = SubStatistic.getMaterials("blocks");
                }

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

                // 整理列表
                materials = materials.stream().distinct().filter(Material::isBlock).toList();

                ListsMgr.addNewList(name, color, Statistic.valueOf(type), materials);
            }
            case ITEM -> {
                // 材料类型加载
                List<Material> materials = new ArrayList<>();

                if (materialNames.isEmpty()) {
                    PlayerTopList.Logger().warning(name + " -> 材料类型列表为空, 使用默认材料");
                    materials = SubStatistic.getMaterials("item");
                }

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

                // 整理列表
                materials = materials.stream().distinct().filter(Material::isItem).toList();

                ListsMgr.addNewList(name, color, Statistic.valueOf(type), materials);
            }
            case UNTYPED -> ListsMgr.addNewList(name, color, Statistic.valueOf(type), new ArrayList<>());
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
            String nameColor = listSection.getString("color", "#FFFFFF");
            List<String> material = listSection.getStringList("material");
            List<String> entity = listSection.getStringList("entity");

            addTopListToManager(name, nameColor, type, material, entity);
        }
    }
}
