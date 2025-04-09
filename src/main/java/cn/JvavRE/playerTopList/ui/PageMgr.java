package cn.JvavRE.playerTopList.ui;


import cn.JvavRE.playerTopList.config.Config;

import java.util.List;

public class PageMgr {
    public static <T> List<T> getListContentAt(List<T> contents, int page) {
        // 分页计算
        int pageSize = Config.getPageSize();
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, contents.size());

        return contents.subList(start, end);
    }

    public static <T> int getTotalPages(List<T> contents) {
        return (contents.size() + Config.getPageSize() - 1) / Config.getPageSize();
    }
}
