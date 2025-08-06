package cn.JvavRE.playerTopList.config;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.data.ListsMgr;
import cn.JvavRE.playerTopList.data.topList.*;
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
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class TopListLoader {
    private static final Plugin plugin = PlayerTopList.getInstance();

    public static void loadTopLists(ConfigurationSection section) {
        if (section == null) return;

        // 遍历榜单项目
        for (String name : section.getKeys(false)) {
            ConfigurationSection listSection = section.getConfigurationSection(name);
            if (listSection == null) continue;

            // 榜单参数
            String type = listSection.getString("type", "");
            String nameColor = listSection.getString("color", "#FFFFFF");
            String expressionString = listSection.getString("expression", "");
            String formatter = listSection.getString("formatter", "%.0f");
            boolean hidden = listSection.getBoolean("hidden", false);
            boolean reversed = listSection.getBoolean("reversed", false);
            List<String> material = listSection.getStringList("material");
            List<String> entity = listSection.getStringList("entity");

            // 检查名称
            if (!isName(name)) {
                plugin.getLogger().warning("不是有效的列表名称: '" + name + "'");
                continue;
            }

            plugin.getLogger().info("正在加载列表: " + name);

            // 多种列表分别注册
            if (isStatistic(type)) {
                addStatisticListToManager(name, nameColor, type, hidden, reversed, expressionString, formatter, material, entity);
            } else if (PlayerTopList.isPapiEnabled() && isPlaceHolder(type)) { // <--- 这里是修改点
                addPlaceHolderListToManager(name, nameColor, type, hidden, reversed, expressionString, formatter);
            } else if (type.equalsIgnoreCase("VAULT_BALANCE")) {
                addVaultListToManager(name, nameColor, hidden, reversed, expressionString, formatter);
            } else {
                plugin.getLogger().warning("不是有效的排行榜类型: '" + type + "'");
            }
        }
    }

    private static void addVaultListToManager(String name, String colorName, boolean hidden, boolean reversed, String expressionString, String formatter) {
        if (PlayerTopList.getEconomy() == null) {
            plugin.getLogger().warning("无法加载财富榜 '" + name + "', 因为未找到 Vault 或经济插件。");
            return;
        }

        TextColor color = getColor(colorName);
        Expression exp = getExpression(expressionString);

        ListsMgr.addNewList(new VaultTopList(name, color, hidden, reversed, exp, formatter));
        plugin.getLogger().info("成功添加财富榜: " + name);
    }


    private static void addPlaceHolderListToManager(String name, String colorName, String typeName,
                                                    boolean hidden, boolean reversed,
                                                    String expressionString, String formater) {

        TextColor color = getColor(colorName);
        Expression exp = getExpression(expressionString);

        ListsMgr.addNewList(new PlaceHolderTopList(name, color, hidden, reversed, exp, formater, typeName));
        plugin.getLogger().info("成功添加列表: " + name + " (" + typeName + ")");
    }

    private static void addStatisticListToManager(String name, String colorName, String typeName,
                                                  boolean hidden, boolean reversed,
                                                  String expressionString, String formater,
                                                  List<String> materialNames, List<String> entityNames) {

        Statistic statistic = Statistic.valueOf(typeName);
        TextColor color = getColor(colorName);
        Expression exp = getExpression(expressionString);

        // 根据类型处理子参数列表
        switch (statistic.getType()) {
            case ENTITY -> {
                List<EntityType> subArgs = getSubArgStream(
                        EntityType.class,
                        entityNames,
                        "alive",
                        SubStatistic::getEntities,
                        TagStatistic::getEntitiesFromTag
                ).toList();

                ListsMgr.addNewList(new EntityTypeStatisticTopList(name, color, hidden, reversed, exp, formater, statistic, subArgs));
            }

            case ITEM -> {
                List<Material> subArgs = getSubArgStream(
                        Material.class,
                        materialNames,
                        "items",
                        SubStatistic::getMaterials,
                        TagStatistic::getItemsFromTag
                ).filter(Material::isItem).toList();

                ListsMgr.addNewList(new MaterialStatisticTopList(name, color, hidden, reversed, exp, formater, statistic, subArgs));
            }

            case BLOCK -> {
                List<Material> subArgs = getSubArgStream(
                        Material.class,
                        materialNames,
                        "blocks",
                        SubStatistic::getMaterials,
                        TagStatistic::getBlocksFromTag
                ).filter(Material::isBlock).toList();

                ListsMgr.addNewList(new MaterialStatisticTopList(name, color, hidden, reversed, exp, formater, statistic, subArgs));
            }

            case UNTYPED ->
                    ListsMgr.addNewList(new CommonStatisticTopList(name, color, hidden, reversed, exp, formater, statistic));
        }

        plugin.getLogger().info("成功添加列表: " + name + " (" + typeName + ")");
    }

    private static <T extends Enum<T> & Keyed> Stream<T> getSubArgStream(Class<T> tClass, List<String> args, String defaultSub,
                                                                         Function<String, List<T>> subGetter, Function<String, List<T>> tagGetter) {

        if (args.isEmpty()) {
            plugin.getLogger().warning("参数列表为空, 使用默认列表");
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
            else plugin.getLogger().warning("不是有效的参数: '" + name + "'");
        }

        return result.stream();
    }

    private static boolean isPlaceHolder(String type) {
        return type.startsWith("%") && type.endsWith("%");
    }

    private static boolean isStatistic(String type) {
        try {
            Statistic.valueOf(type);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isName(String name) {
        return !name.isBlank() && Config.getListNamePattern().matcher(name).matches();
    }

    private static TextColor getColor(String color) {
        TextColor rColor = TextColor.fromHexString(color);

        if (rColor != null) {
            return rColor;
        } else {
            plugin.getLogger().warning("不是有效的颜色: '" + color + "'" + ", 使用默认颜色");
            return TextColor.color(255, 255, 255);
        }
    }

    private static Expression getExpression(String expression) {
        try {
            return expression.isEmpty() ?
                    null :
                    new ExpressionBuilder(expression).variable("count").build();

        } catch (Exception e) {
            plugin.getLogger().warning("表达式出现错误, 已禁用表达式");
            return null;
        }
    }
}
