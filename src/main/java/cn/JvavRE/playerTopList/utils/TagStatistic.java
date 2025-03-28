package cn.JvavRE.playerTopList.utils;

import org.bukkit.*;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class TagStatistic {
    public static <T extends Keyed> boolean isValid(Class<T> type, String name) {
        return switch (type.getSimpleName()) {
            case "Material" ->
                    Bukkit.getTag(Tag.REGISTRY_BLOCKS, NamespacedKey.minecraft(name), Material.class) != null || Bukkit.getTag(Tag.REGISTRY_ITEMS, NamespacedKey.minecraft(name), Material.class) != null;
            case "EntityType" ->
                    Bukkit.getTag(Tag.REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft(name), EntityType.class) != null;
            default -> false;
        };
    }

    public static ArrayList<Material> getBlocksFromTag(String name) {
        ArrayList<Material> list = new ArrayList<>();

        Tag<Material> tag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, NamespacedKey.minecraft(name), Material.class);
        if (tag != null) list.addAll(tag.getValues());

        return list;
    }

    public static ArrayList<EntityType> getEntitiesFromTag(String name) {
        ArrayList<EntityType> list = new ArrayList<>();

        Tag<EntityType> tag = Bukkit.getTag(Tag.REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft(name), EntityType.class);
        if (tag != null) list.addAll(tag.getValues());

        return list;
    }

    public static ArrayList<Material> getItemsFromTag(String name) {
        ArrayList<Material> list = new ArrayList<>();

        Tag<Material> tag = Bukkit.getTag(Tag.REGISTRY_ITEMS, NamespacedKey.minecraft(name), Material.class);
        if (tag != null) list.addAll(tag.getValues());

        return list;
    }
}
