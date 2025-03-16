package cn.JvavRE.playerTopList.ui;


import cn.JvavRE.playerTopList.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public class PageMgr {
    public static int getTotalPages(List<Component> contents) {
        return (contents.size() + Config.getPageSize() - 1) / Config.getPageSize();
    }

    public static Component getPage(List<Component> contents, int page) {
        // 分页计算
        int pageSize = Config.getPageSize();
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, contents.size());

        // 添加组件
        TextComponent.Builder builder = Component.text();
        for (int i = start; i < end; i++) {
            builder.append(contents.get(i)).appendNewline();
        }

        return builder.build();
    }
}
