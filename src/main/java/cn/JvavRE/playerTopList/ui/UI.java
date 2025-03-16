package cn.JvavRE.playerTopList.ui;

import cn.JvavRE.playerTopList.config.UIComponent;
import cn.JvavRE.playerTopList.config.UIConfig;
import cn.JvavRE.playerTopList.tasks.PlayerData;
import cn.JvavRE.playerTopList.tasks.TopList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UI {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final TopList topList;
    private final PageMgr pageMgr = new PageMgr();
    private final List<Component> tempList = new ArrayList<>();
    private final Component coloredName;
    private LocalDateTime lastUpdate;

    public UI(TopList topList, TextColor color) {
        this.topList = topList;
        this.coloredName = getColoredName(color);

        update();
    }

    private Component getColoredName(TextColor color) {
        return Component.text("[" + topList.getName() + "]").color(color);
    }

    public void update() {
        tempList.clear();
        for (int i = 0; i < topList.getDataList().size(); i++) {
            PlayerData playerData = topList.getDataList().get(i);
            String playerName = playerData.getPlayer().getName() != null ? playerData.getPlayer().getName() : "null";

            int num = i + 1;
            tempList.add(UIConfig.get(UIComponent.ITEM)
                    .replaceText(config -> config.matchLiteral("{num}").replacement(String.valueOf(num)))
                    .replaceText(config -> config.matchLiteral("{playerName}").replacement(playerName))
                    .replaceText(config -> config.matchLiteral("{spacer}").replacement(UIConfig.get(UIComponent.SPACER)))
                    .replaceText(config -> config.matchLiteral("{count}").replacement(String.valueOf(playerData.getCount())))
            );
        }

        pageMgr.updatePages(tempList);
        lastUpdate = LocalDateTime.now();
    }

    public void show(Player player, int page) {
        player.sendMessage(UIConfig.get(UIComponent.MAIN_UI)
                .replaceText(config -> config.matchLiteral("{items}").replacement(pageMgr.getPage(page)))
                .replaceText(config -> config.matchLiteral("{listName}").replacement(coloredName))
                .replaceText(config -> config.matchLiteral("{updateTime}").replacement(lastUpdate.format(formatter)))
                .replaceText(config -> config.matchLiteral("{totalIndex}").replacement(String.valueOf(pageMgr.getTotalPages())))
                .replaceText(config -> config.matchLiteral("{currentIndex}").replacement(String.valueOf(page)))
                .replaceText(config -> config.matchLiteral("{prevButton}").replacement(
                        page <= 1 ? Component.empty()
                                : UIConfig.get(UIComponent.SPACER)
                                .replaceText(subConfig -> subConfig.matchLiteral("{prevIndex}").replacement(String.valueOf(page - 1)))
                                .replaceText(subConfig -> subConfig.matchLiteral("{listName}").replacement(topList.getName()))

                ))
                .replaceText(config -> config.matchLiteral("{nextButton}").replacement(
                        page >= pageMgr.getTotalPages() ? Component.empty()
                                : UIConfig.get(UIComponent.SPACER)
                                .replaceText(subConfig -> subConfig.matchLiteral("{nextIndex}").replacement(String.valueOf(page + 1)))
                                .replaceText(subConfig -> subConfig.matchLiteral("{listName}").replacement(topList.getName()))
                ))
        );
    }

    public Component getColoredName() {
        return coloredName;
    }
}
