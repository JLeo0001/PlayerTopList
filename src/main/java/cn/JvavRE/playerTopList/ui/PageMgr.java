package cn.JvavRE.playerTopList.ui;


import cn.JvavRE.playerTopList.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class PageMgr {
    private final List<Component> contents = new ArrayList<>();

    public void updatePages(List<Component> components) {
        contents.clear();
        contents.addAll(components);
    }

    public int getTotalPages() {
        return (contents.size() + Config.getPageSize() - 1) / Config.getPageSize();
    }

    public Component getPage(int page) {
        // 分页计算
        int pageSize = Config.getPageSize();
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, contents.size());

        TextComponent.Builder builder = Component.text();
        for (int i = start; i < end; i++) {
            builder.append(contents.get(i)).appendNewline();
        }

        return builder.build();
    }
}
