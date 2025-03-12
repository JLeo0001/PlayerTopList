package cn.JvavRE.playerTopList.config;

public enum UIComponent {
    HEADER("header", "<aqua>================[{listName}]================</aqua>"),
    FOOTER("footer", "<aqua>{prevButton} ( {idx} ) {nxtButton}</aqua>"),
    PREV_BUTTON("previous-button", "<gray> < </gray>"),
    NEXT_BUTTON("next-button", "<gray> > </gray>"),
    INDEX("index", "当前: <white>{currentIndex}</white>/<white>{totalIndex}</white>"),
    ITEM("item", "<white>{num}. <green>{playerName}{spacer}{count}</green></white>"),
    SPACER("spacer", " "),
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
