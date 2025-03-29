# [PTL] 基于统计数据的排行榜插件

## 服务端版本
 - paper, purpur, folia等服务端核心
 - 理论上支持1.13+, 目前仅详细测试了1.21, 如有兼容问题可issue提出

## 功能
 - 通过内置统计数据计算排行榜
 - 可以自定义排行榜ui
 - 支持使用表达式更改排行榜数据
 - 支持格式化显示数据
 - 支持使用标签

## 命令
 - ```/ptl reload``` 重载配置
 - ```/ptl show <名称>``` 打开指定的排行榜(all展示所有)

## 配置文件
```yaml
# 每一页上应该有多少玩家
page-size: 10

# 排行榜更新间隔(单位为秒)(不小于30)
update-interval: 60

# 自定义UI部分
ui:
  header: "<bold>================ <hover:show_text:'<yellow>更新时间: <#FFA500>{updateTime}</#FFA500></yellow>'>{listName} ================</bold>"
  footer: "<aqua>{prevButton}当前: (<white>{currentIndex}</white>/<white>{totalIndex}</white>){nextButton}</aqua>"
  prev-button: "<hover:show_text:'上一页'><dark_aqua> <- </dark_aqua></hover>"
  next-button: "<hover:show_text:'下一页'><dark_aqua> -> </dark_aqua></hover>"
  item: "<gray>⬡</gray> <white>{num}. <green>{playerName}</green> <grey>-</grey> <dark_green>{count}</dark_green></white>"

# 排行榜部分
lists:
  #示例:
  #这个是排行榜名称
  挖掘榜-总榜:
    # (可选) 这个是排行榜显示颜色
    color: "#FFA500"

    # (必填) 这个是统计数据类型, 可用值见附录1
    type: MINE_BLOCK

    # (选择性必填) 当类型为Statistic.Type.BLOCK或者Statistic.Type.ITEM时必填
    # 除了材料名称外可用值: all, items, blocks, solid
    # 支持使用标签, 见附录4
    # 其他可用值见附录2
    # 注意!!!该项为列表类型
    material: [solid]

    # (选择性必填) 当类型为Statistic.Type.ENTITY时必填
    # 除了实体名称外可用值: all, alive
    # 支持使用标签, 见附录4
    # 其他可用值见附录3
    # 注意!!!该项为列表类型
    entity: [all]

    # (选填) 数值表达式:
    # 用于修正排行榜数值
    expression: "count / 64"

    # (选填) 数字格式:
    # java格式化字符串
    formatter: "%.1f 组"

  在线时间:
    color: "#FF00FF"
    type: PLAY_ONE_MINUTE
    expression: "count / 20 / 60 / 60"
    formatter: "%.1f 小时"

  挖掘榜-石头:
    color: "#EEEEEE"
    type: MINE_BLOCK
    material: [STONE]

  击杀榜-总榜:
    color: "#FF00FF"
    type: KILL_ENTITY
    entity: [alive]

  击杀榜-玩家:
    color: "#FF0022"
    type: KILL_ENTITY
    entity: [PLAYER]

  击杀榜-亡灵:
    color: "#009900"
    type: KILL_ENTITY
    entity: [undead]

# 附录
# 1.可用统计类型见: https://bukkit.windit.net/javadoc/org/bukkit/Statistic.html
# 2.可用材料类型见: https://bukkit.windit.net/javadoc/org/bukkit/Material.html
# 3.可用实体类型见: https://bukkit.windit.net/javadoc/org/bukkit/entity/EntityType.html
# 4.可用标签及对应材料见: https://zh.minecraft.wiki/w/%E6%A0%87%E7%AD%BE?variant=zh-cn#%E6%A0%87%E7%AD%BE%E5%88%97%E8%A1%A8```
```

## TODO
 - [ ] 优化启动时某些配置加载极慢的问题
 - [ ] 添加对placeholder的支持
 - [x] 添加对内置标签分组的支持
