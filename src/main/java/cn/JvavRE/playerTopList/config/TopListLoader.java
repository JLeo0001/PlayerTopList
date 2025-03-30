package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.data.ListsMgr;
import cn.JvavRE.playerTopList.utils.SubStatistic;
import cn.JvavRE.playerTopList.utils.TagStatistic;
import net.kyori.adventure.text.format.TextColor;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

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

    private static TextColor getColor(String color) {
        TextColor rColor = TextColor.fromHexString(color);

        if (rColor != null) {
            return rColor;
        } else {
            logger.warning("不是有效的颜色: '" + color + "'" + ", 使用默认颜色");
            return TextColor.color(255, 255, 255);
        }
    }

    private static Expression getExpression(String expression) {
        try {
            return expression.isEmpty() ?
                    null :
                    new ExpressionBuilder(expression).variable("count").build();

        } catch (Exception e) {
            logger.warning("表达式出现错误, 已禁用表达式");
            return null;
        }
    }

    private static void addTopListToManager(String name, String colorName, String type,
                                            List<String> materialNames, List<String> entityNames,
                                            String expressionString, String formater) {

        // 检查名称
        if (!isName(name)) {
            logger.warning("不是有效的列表名称: '" + name + "'");
            return;
        }

        logger.info("正在加载列表: " + name);

        // 检查类型
        if (!isStatistic(type)) {
            logger.warning("不是有效的统计类型: '" + type + "'");
            return;
        }

        // 加载属性
        Statistic statistic = Statistic.valueOf(type);
        TextColor color = getColor(colorName);
        Expression exp = getExpression(expressionString);

        // 根据类型处理子参数列表
        List<?> subArgs = switch (statistic.getType()) {
            case ENTITY -> getSubArgStream(
                    EntityType.class,
                    entityNames,
                    "alive",
                    SubStatistic::getEntities,
                    TagStatistic::getEntitiesFromTag
            ).toList();

            case ITEM -> getSubArgStream(
                    Material.class,
                    materialNames,
                    "items",
                    SubStatistic::getMaterials,
                    TagStatistic::getItemsFromTag
            ).filter(Material::isItem).toList();

            case BLOCK -> getSubArgStream(
                    Material.class,
                    materialNames,
                    "blocks",
                    SubStatistic::getMaterials,
                    TagStatistic::getBlocksFromTag
            ).filter(Material::isBlock).toList();

            default -> new ArrayList<Material>();
        };

        ListsMgr.addStatisticList(name, color, statistic, subArgs, exp, formater);
        logger.info("成功添加列表: " + name + " (" + type + ")");
    }

    private static <T extends Enum<T> & Keyed> Stream<T> getSubArgStream(Class<T> tClass, List<String> args, String defaultSub,
                                                                         Function<String, List<T>> subGetter, Function<String, List<T>> tagGetter) {

        if (args.isEmpty()) {
            logger.warning("参数列表为空, 使用默认列表");
            return subGetter.apply(defaultSub).stream();
        }

        // 将字符串转换为材料列表
        Set<T> result = new HashSet<>();
        for (String name : args) {
            T obj;
            try {
                obj = Enum.valueOf(tClass, name.toUpperCase());
            } catch (Exception e) {
                obj = null;
            }

            if (obj != null) result.add(obj);
            else if (SubStatistic.isValid(name)) result.addAll(subGetter.apply(name));
            else if (TagStatistic.isValid(tClass, name)) result.addAll(tagGetter.apply(name));
            else logger.warning("不是有效的参数: '" + name + "'");
        }

        return result.stream();
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
            String expressionString = listSection.getString("expression", "");
            String formatter = listSection.getString("formatter", "%.0f");
            List<String> material = listSection.getStringList("material");
            List<String> entity = listSection.getStringList("entity");

            addTopListToManager(name, nameColor, type, material, entity, expressionString, formatter);
        }
    }
}
