package cn.JvavRE.playerTopList.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public enum UIComponent {
    HEADER("header", "<aqua>================[ {listName} ]================</aqua>"),
    FOOTER("footer", "<aqua>{prevButton}当前: (<white>{currentIndex}</white>/<white>{totalIndex}</white>){nextButton}</aqua>"),
    PREV_BUTTON("previous-button", "<click:run_command:'/ptl show {listName} {prevIndex}'><hover:show_text:'上一页'><dark_aqua> <- </dark_aqua></hover></click>"),
    NEXT_BUTTON("next-button", "<click:run_command:'/ptl show {listName} {nextIndex}'><hover:show_text:'下一页'><dark_aqua> -> </dark_aqua></hover></click>"),
    ITEM("item", "<gray>⬡</gray> <white>{num}. <green>{playerName} <grey>{spacer}</grey> {count}</green></white>"),
    SPACER("spacer", "-"),
    MAIN_UI("you-should-not-use-this", "this-will-generate-on-default"),
    ;


    private final String name;
    private final Component component;

    UIComponent(String name, String component) {
        this.name = name;
        this.component = MiniMessage.miniMessage().deserialize(component);
    }

    public Component getComponent() {
        return component;
    }

    public String getName() {
        return name;
    }
}
