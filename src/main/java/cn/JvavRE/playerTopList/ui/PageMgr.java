package cn.JvavRE.playerTopList.ui;


import java.util.List;

public class PageMgr {
    public static <T> List<T> getListContentAt(List<T> contents, int size, int page) {
        // 分页计算
        int start = (page - 1) * size;
        int end = Math.min(start + size, contents.size());

        return contents.subList(start, end);
    }

    public static <T> int getTotalPages(List<T> contents, int size) {
        return (contents.size() + size - 1) / size;
    }
}
