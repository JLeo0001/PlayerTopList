package cn.JvavRE.playerTopList.config;

public enum UIComponent {
    HEADER("header", "<aqua>================[ {listName} ]================</aqua>"),
    FOOTER("footer", "<aqua>{prevButton}当前: (<white>{currentIndex}</white>/<white>{totalIndex}</white>){nextButton}</aqua>"),
    PREV_BUTTON("previous-button", "<click:run_command:'/ptl show {listName} {prevIndex}'><hover:show_text:'上一页'><dark_aqua> <- </dark_aqua></hover></click>"),
    NEXT_BUTTON("next-button", "<click:run_command:'/ptl show {listName} {nextIndex}'><hover:show_text:'下一页'><dark_aqua> -> </dark_aqua></hover></click>"),
    ITEM("item", "<gray>⬡</gray> <white>{num}. <green>{playerName} <grey>{spacer}</grey> {count}</green></white>"),
    SPACER("spacer", "-"),
    ;


    private final String name;
    private final String component;

    UIComponent(String name, String component) {
        this.name = name;
        this.component = component;
    }

    public String getComponent() {
        return component;
    }

    public String getName() {
        return name;
    }
}
