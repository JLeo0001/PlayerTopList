package cn.JvavRE.playerTopList.ui;

import cn.JvavRE.playerTopList.PlayerTopList;
import cn.JvavRE.playerTopList.config.Config;
import cn.JvavRE.playerTopList.config.UIComponent;
import cn.JvavRE.playerTopList.config.UIConfig;
import cn.JvavRE.playerTopList.data.PlayerData;
import cn.JvavRE.playerTopList.data.TopList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UI {
    private final TopList topList;
    private final Component coloredName;
    private final List<Component> tempList = new ArrayList<>();

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
                    .replaceText(config -> config.match(Config.getPattern())
                            .replacement((matchResult, builder) -> {
                                        String key = matchResult.group();
                                        return switch (key) {
                                            case "{num}" -> Component.text(String.valueOf(num));
                                            case "{playerName}" -> Component.text(playerName);
                                            case "{count}" -> Component.text(String.valueOf(playerData.getCount()));
                                            default -> Component.text(key);
                                        };
                                    }
                            ))
            );
        }
    }

    public void show(Player player, int page) {
        int totalPage = PageMgr.getTotalPages(tempList);

        Bukkit.getAsyncScheduler().runNow(PlayerTopList.getInstance(), task -> {
                    Component nextButton = UIConfig.get(UIComponent.NEXT_BUTTON)
                            .clickEvent(ClickEvent.runCommand("/ptl show " + topList.getName() + " " + (page + 1)));
                    Component prevButton = UIConfig.get(UIComponent.PREV_BUTTON)
                            .clickEvent(ClickEvent.runCommand("/ptl show " + topList.getName() + " " + (page - 1)));

                    player.sendMessage(UIConfig.get(UIComponent.MAIN_UI)
                            .replaceText(config -> config.match(Config.getPattern())
                                    .replacement((matchResult, builder) -> {
                                                String key = matchResult.group();
                                                return switch (key) {
                                                    case "{items}" -> PageMgr.getPage(tempList, page);
                                                    case "{listName}" -> coloredName;
                                                    case "{updateTime}" -> Component.text(topList.getUpdateTime());
                                                    case "{totalIndex}" -> Component.text(String.valueOf(totalPage));
                                                    case "{currentIndex}" -> Component.text(String.valueOf(page));
                                                    case "{prevButton}" -> page > 1 ? prevButton : Component.empty();
                                                    case "{nextButton}" -> page < totalPage ? nextButton : Component.empty();
                                                    default -> Component.text(key);
                                                };
                                            }
                                    )
                            )
                    );
                }
        );
    }

    public Component getColoredName() {
        return coloredName;
    }
}
