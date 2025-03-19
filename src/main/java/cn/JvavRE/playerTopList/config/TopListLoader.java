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
import java.util.function.Function;
import java.util.logging.Logger;

public class TopListLoader {
    private static final Logger logger = PlayerTopList.getInstance().getLogger();
    
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
            logger.warning("不是有效的列表名称: '" + name + "'");
            return;
        }

        if (!isStatistic(type)) {
            logger.warning("不是有效的统计类型: '" + type + "'");
            return;
        }

        if (!isColor(nameColor)) {
            logger.warning("不是有效的颜色: '" + nameColor + "'" + ", 使用默认颜色");
            nameColor = "#FFFFFF";
        }

        logger.info("正在加载列表: " + name);
        
        // 根据类型处理子参数列表
        TextColor color = TextColor.fromHexString(nameColor);
        Statistic statistic = Statistic.valueOf(type);
        switch (statistic.getType()) {
            case ENTITY -> {
                List<EntityType> entities = processNames(EntityType.class, entityNames, "alive", SubStatistic::getEntities);
                entities = entities.stream().distinct().toList();
                ListsMgr.addNewList(name, color, Statistic.valueOf(type), entities);
            }
            case ITEM -> {
                List<Material> items = processNames(Material.class, materialNames, "items", SubStatistic::getMaterials);
                items = items.stream().filter(Material::isItem).distinct().toList();
                ListsMgr.addNewList(name, color, Statistic.valueOf(type), items);
            }
            case BLOCK -> {
                List<Material> blocks = processNames(Material.class, materialNames, "blocks", SubStatistic::getMaterials);
                blocks = blocks.stream().filter(Material::isBlock).distinct().toList();
                ListsMgr.addNewList(name, color, Statistic.valueOf(type), blocks);
            }
            case UNTYPED -> ListsMgr.addNewList(name, color, Statistic.valueOf(type), new ArrayList<>());
        }

        logger.info("成功添加列表: " + name + " (" + type + ")");
    }

    private static <T extends Enum<T>> List<T> processNames(Class<T> tClass, List<String> args, String defaultSub, Function<String, List<T>> subGetter) {
        if (args.isEmpty()) {
            logger.warning("参数列表为空, 使用默认列表");
            return subGetter.apply(defaultSub);
        }

        List<T> result = new ArrayList<>();
        for (String name : args) {
            T obj;

            try{
                obj = Enum.valueOf(tClass, name.toUpperCase());
            }catch (Exception e){
                obj = null;
            }

            if (obj != null) result.add(obj);
            else if (SubStatistic.isValid(name)) result.addAll(subGetter.apply(name));
            else logger.warning("不是有效的参数: '" + name + "'");
        }

        return result;
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
