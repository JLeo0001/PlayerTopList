package cn.JvavRE.playerTopList.ui;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.config.UIComponent;
import cn.JvavRE.playerTopList.config.UIConfig;
import cn.JvavRE.playerTopList.data.playerData.PlayerData;
import cn.JvavRE.playerTopList.data.topList.AbstractTopList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class UI {
    private static TextComponent.Builder listsUI;

    static {
        resetListsUI();
    }

    public static void resetListsUI() {
        listsUI = Component.text();
        listsUI.append(Component.text("=========| 点击下方名称打开排行榜 |=========").decorate(TextDecoration.BOLD)).appendNewline();
    }

    public static void addTopListToUI(AbstractTopList topList) {
        if (topList.isHidden()) return;

        listsUI.append(getColoredListName(topList)
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/ptl show " + topList.getName()))
                .hoverEvent(HoverEvent.showText(Component.text("点击查看排行榜")))
        ).appendSpace();
    }

    public static void showListsUI(Player player) {
        Bukkit.getAsyncScheduler().runNow(
                PlayerTopList.getInstance(),
                task -> player.sendMessage(listsUI.build())
        );
    }

    public static void showTopListUI(Player player, AbstractTopList topList, int page) {
        Bukkit.getAsyncScheduler().runNow(
                PlayerTopList.getInstance(),
                task -> player.sendMessage(generateTopListUI(topList, page))
        );
    }

    private static Component generateTopListUI(AbstractTopList topList, int page) {
        TextComponent.Builder builder = Component.text();

        // 添加头部
        builder.append(UIConfig.get(UIComponent.HEADER))
                .appendNewline()
                .appendNewline();

        // 排行榜项目
        List<PlayerData> pageList = PageMgr.getListContentAt(topList.getDataList(), page);
        for (int i = 0; i < pageList.size(); i++) {
            PlayerData playerData = pageList.get(i);
            String playerName = playerData.getPlayer().getName();

            int num = i + 1;
            Component listItem = UIConfig.get(UIComponent.ITEM)
                    .replaceText(config -> config.match(Config.getUIReplacePattern())
                            .replacement((matchResult, textBuilder) -> {
                                String key = matchResult.group();
                                return switch (key) {
                                    case "{num}" -> Component.text(String.valueOf(num));
                                    case "{playerName}" -> Component.text(playerName != null ? playerName : "null");
                                    case "{count}" -> Component.text(topList.getFormattedData(playerData));
                                    default -> Component.text(key);
                                };
                            })
                    );

            builder.append(listItem).appendNewline();
        }

        // 尾部
        builder.appendNewline().append(UIConfig.get(UIComponent.FOOTER));

        // 按钮
        Component nextButton = UIConfig.get(UIComponent.NEXT_BUTTON)
                .clickEvent(ClickEvent.runCommand("/ptl show " + topList.getName() + " " + (page + 1)));
        Component prevButton = UIConfig.get(UIComponent.PREV_BUTTON)
                .clickEvent(ClickEvent.runCommand("/ptl show " + topList.getName() + " " + (page - 1)));

        int totalPages = PageMgr.getTotalPages(topList.getDataList());

        // 变量替换
        return builder.build().replaceText(config -> config.match(Config.getUIReplacePattern())
                .replacement((matchResult, textBuilder) -> {
                    String key = matchResult.group();
                    return switch (key) {
                        case "{listName}" -> getColoredListName(topList);
                        case "{updateTime}" -> Component.text(topList.getUpdateTime());
                        case "{totalIndex}" -> Component.text(totalPages);
                        case "{currentIndex}" -> Component.text(page);
                        case "{prevButton}" -> page > 1 ? prevButton : Component.empty();
                        case "{nextButton}" -> page < totalPages ? nextButton : Component.empty();
                        default -> Component.text(key);
                    };
                })
        );
    }

    private static Component getColoredListName(AbstractTopList topList) {
        return Component.text("[" + topList.getName() + "]").color(topList.getNameColor());
    }
}
