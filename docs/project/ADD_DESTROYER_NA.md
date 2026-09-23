# 计划：Blockbench 创建驱逐ナ级（驱逐舰 NA 级）

显示名：**驱逐ナ级** / **驱逐舰 NA 级**。使用已预留 `ID.ShipClass.DDNA = 64`，不新开 class id。玩法与体型对齐已实装的驱逐イ级（`EntityDestroyerI` / `ModelDestroyerI`）。本文只规划，不代表已实装。

## Blockbench 工程（先做模型）

新建 **模组版实体**（不要「Java 版方块/物品」）。

| 栏位 | 值 |
|---|---|
| 文件名 | `destroyer_na`（保存 `destroyer_na.bbmodel`，建议纳入仓库资源旁或 `docs/` 外的 models 源目录，勿当运行时资源） |
| 模型标识符 | `shincolle:destroyer_na` |
| 实体层级 | `main` |
| 导出版本 | **Forge 1.17+ (Mojmaps)** |
| Y 轴翻转 | 先勾选；进游戏上下颠倒再关 |
| UV 尺寸 | **256 × 128**（与イ级 `LayerDefinition` 相同，除非刻意改分辨率） |

零件树尽量沿用イ级名（`PHead`、`PBody`、`PJawBottom`、`PEyeLightL/R` 等），这样 `setupAnim` 可从 `ModelDestroyerI` 复制后再改。贴图导出为 `entitydestroyerna.png`。导出 Java 后 **手抄** 进 `createBodyLayer()`，不要提交 Blockbench JSON 当实体模型。

## 代码接入（模型可用之后）

对标イ级最小集合：

- 实体：`EntityDestroyerNa`（可先无 `*_Mob`，与イ级一致）
- 注册：`ModEntities` `destroyer_na`、属性、`ClientSetup` 图层 + `tex("EntityDestroyerNa")`
- 蛋：`ShipSpawnEgg.ENTITY_MAP` 放入 class **64**；创造栏 `egg.createStack(64)`；蛋图标 switch 加入 `DDNA`（与其它 DD 同为 icon 2）
- 语言：`entity.shincolle.destroyer_na`；蛋为 `item.shincolle.ship_egg_66`（规则是 `ship_egg_(class+2)`）。中文主名「驱逐ナ级」，可在描述/图鉴写「驱逐舰 NA 级」
- `Values`：已有 DDNA 图标档 `{1, 220, 0}`；补属性/抗性/舰种表（可抄 DDI）
- `ShipCalc`：已把 DDNA 排除在部分列表；若要小型池建造，再评估是否加入 `SHIP_SMALL`
- 图鉴 lang（`gui.shincolle.book`）按イ级条目仿写

改 registry / 蛋 NBT 前确认：64 号此前无实体，友好蛋应无旧存档冲突。

## 验证

`./gradlew runClient`，创造栏用 class 64 蛋生成。检查站立、走、坐、倒地、眼睛、手持。改 Java 模型需重启客户端。

## 实现顺序

1. Blockbench 出 `.bbmodel` + PNG  
2. `ModelDestroyerNa` + 贴图  
3. 实体 / 注册 / 蛋 / lang / Values  
4. 进游戏与 readme「现有舰娘」清单更新  


**PKisaragi** 是イ级的**可选换装**，不是身体骨骼。四个发光小件（底座 + 三片鳍）挂在 `GlowPHead` 上，舰娘 GUI 里 **model state 第 0 位（注释写的是 head）** 打开才显示，关闭则隐藏。名字来自舰娘「如月」，是イ级特典外形，**NA 级不要做**。

---

### イ级（已实装 `ModelDestroyerI`）

```text
PBack                          背 / 全身根
├─ PNeck                       颈
│  └─ PHead                    头、上颚
│     └─ PJawBottom            下颚
├─ PBody                       腹
│  ├─ PLegLeft → PLegLeftEnd   左腿 → 左脚
│  └─ PLegRight → PLegRightEnd 右腿 → 右脚
└─ PTail                       尾根
   ├─ PTailLeft → PTailLeftEnd 左尾鳍
   ├─ PTailRight → PTailRightEnd 右尾鳍
   └─ PTailEnd                 尾尖

GlowPBack → GlowPNeck → GlowPHead   与上面对齐的发光空骨架
├─ EyeLightL0 / EyeLightR0         眼 表情0
├─ EyeLightL1 / EyeLightR1         眼 表情1
├─ EyeLightL2 / EyeLightR2         眼 表情2
└─ PKisaragi00–03                  如月换装（可开关，NA 级不用）
```

---

### ナ级 / NA 级（建议骨骼，尚未进游戏）

共用：根、颈、头、下颚、腹、腿、发光眼。  
不同：圆头大嘴、口炮、三烟囱；**没有** 长尾鳍和如月件。

```text
PBack                          仍作全身根（更短、托住圆身体）
├─ PNeck                       可很短，几乎并进头
│  └─ PHead                    大球体
│     ├─ PJawBottom            大圆嘴 / 下牙圈（比イ级夸张）
│     └─ PGun                  嘴里伸出的炮管（イ级没有）
├─ PBody
│  ├─ PLegLeft → PLegLeftEnd
│  └─ PLegRight → PLegRightEnd
└─ PStack0                     后上方烟囱 1（替代イ级长尾）
   PStack1                     烟囱 2  （可都做 PBack 的子级）
   PStack2                     烟囱 3

GlowPBack → GlowPNeck → GlowPHead
├─ EyeLightL0 / EyeLightR0     紫色眼 表情0
├─ EyeLightL1 / EyeLightR1
└─ EyeLightL2 / EyeLightR2
（不要 PKisaragi）
```

`PGun`、`PStack0–2` 是按立绘起的新名字，Blockbench 里用这些即可；以后写 `ModelDestroyerNa` 时 `getChild` 必须同名。