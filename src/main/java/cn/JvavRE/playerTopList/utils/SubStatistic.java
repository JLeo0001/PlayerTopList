package cn.JvavRE.playerTopList.utils;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubStatistic {
    private static final List<Material> solid = Arrays.stream(Material.values()).filter(Material::isSolid).toList();
    private static final List<Material> blocks = Arrays.stream(Material.values()).filter(Material::isBlock).toList();
    private static final List<Material> items = Arrays.stream(Material.values()).filter(Material::isItem).toList();
    private static final List<Material> allBlocks = List.of(Material.values());

    private static final List<EntityType> alive = Arrays.stream(EntityType.values()).filter(EntityType::isAlive).toList();
    private static final List<EntityType> allEntities = Arrays.stream(EntityType.values()).filter(entityType -> !entityType.equals(EntityType.UNKNOWN)).toList();

    public static boolean isValid(String name) {
        return switch (name.toLowerCase()) {
            case "solid", "blocks", "items", "alive", "all" -> true;
            default -> false;
        };
    }

    public static List<Material> getMaterials(String name) {
        return switch (name.toLowerCase()) {
            case "solid" -> solid;
            case "blocks" -> blocks;
            case "items" -> items;
            case "all" -> allBlocks;
            default -> new ArrayList<>();
        };
    }

    public static List<EntityType> getEntities(String name) {
        return switch (name.toLowerCase()) {
            case "alive" -> alive;
            case "all" -> allEntities;
            default -> new ArrayList<>();
        };
    }
}
