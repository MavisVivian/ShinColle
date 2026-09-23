# ShinColle（Forge 1.20.1）

本仓库基于 [kousakirai/ShinColle-Reforge](https://github.com/kousakirai/ShinColle-Reforge.git)，在其 Forge 1.20.1 移植之上继续做版本适配与 bugfix。

Reforge 将 PinkaLulan 的 ShinColle 在授权下从 Forge 1.10.2 公开版移植到 1.20.1，内容对应原作、没有额外新要素。欢迎通过 issue 或评论提出需求。

- 本仓库上游：[kousakirai / ShinColle-Reforge](https://github.com/kousakirai/ShinColle-Reforge.git)
- 原作：[PinkaLulan / ShinColle](https://github.com/PinkaLulan/ShinColle)
- 移植作者：[X](https://x.com/kousakirai)
- 支持：[Discord](https://discord.gg/t76XJgJugQ)
- 参考：[舰娘百科](https://zh.kcwiki.cn/wiki/%E6%8A%A4%E5%8D%AB%E6%A0%96%E5%A7%AC)

在游戏中收集可爱的舰娘。功能包括：友好与敌对舰娘、舰载装备、等级系统。

## 现有舰娘

与 `ModEntities` 注册一致，创造物品栏名称如下。部分舰娘有友好与野生两套实体、共用同一模型与贴图；深海イ级等没有 `*_MOB` 野生实体。

- 驱逐：イ / ロ / ハ / ニ、岛风、晓、响、雷、电
- 重巡：リ / ネ、爱宕、高雄
- 轻巡：天龙、龙田
- 航母：ヲ、加贺、赤城
- 战舰：ル / タ / レ、长门、大和、金刚、比叡、榛名、雾岛
- 潜艇：U511、Ro500、カ / ヨ / ソ
- 姬：机场、战舰、驱逐、重巡、港湾、北方、空母、离岛、中途岛、潜水、潜水新（SSNH）
- 水鬼：空母（CVWD）
- 运输：ワ级

## 开发

环境：JDK **17**，Minecraft **1.20.1**，Forge **47.4.0**。不必安装 IDE 或第三方启动器；不必把 Forge installer 放进本仓库。`runClient` 由 Gradle 拉取对应 Forge，与手动安装器无关。

几何与 UV 使用 [Blockbench](https://blockbench.net/) 的 **Modded Entity**。动画写在各舰 `setupAnim` / `applyDeadPose` 的 Java 中。不要使用已停更的 Tabula；不要把 Blockbench JSON 或 GeckoLib 工程直接当作本模组的实体模型。导出后抄进对应 `Model*.java` 的 `createBodyLayer()`，贴图分辨率必须与 `LayerDefinition` 宽高一致（例如驱逐イ级为 256×128）。

```bash
./gradlew compileJava     # 仅编译
./gradlew build           # 编译并打出 build/libs 下的 shincolle jar
./gradlew runClient       # 开发客户端（推荐测模型与玩法）
./gradlew runServer       # 无 GUI 专用服务端
./gradlew gameTestServer  # GameTest
```

Windows 将 `./gradlew` 换成 `.\gradlew.bat`。`compileJava` 不会生成可放入 `mods` 的 jar；正式档案测试需先 `build`，再拷贝 `build/libs` 中非 `sources` 的 jar。

创造模式测外观：用对应舰娘蛋生成。驱逐イ级为 ship class **0**（`ID.ShipClass.DDI`）。

## 可添加舰娘（ID 已预留、尚未做成实体）

下列 `ID.ShipClass` 已占号，部分在 `Values.java` 中有配方或属性草稿，但没有 `ModEntities` 注册。此处只作名单，不表示已实装或即将实装。负值的 `Player*` 不是舰娘，不要占用。

若新增名单外的舰名，需要新的 class id，并先评估蛋、NBT、图鉴与存档兼容。

### 深海常规级

| class id | 常量 | 说明 |
|---|---|---|
| 4 | CLHO | 轻巡ホ级 |
| 5 | CLHE | 轻巡ヘ级 |
| 6 | CLTO | 轻巡ト级 |
| 7 | CLTSU | 轻巡ツ级 |
| 8 | CLTCHI | 重雷装チ级 |
| 11 | CVLNU | 轻母ヌ级 |
| 64 | DDNA | 驱逐ナ级 |
| 65 | DDAH | 驱逐エ级 |
| 55 | Raiden | 雷电联动位（现仅雷/电实体上的 flag） |

### 姬 / 鬼 / 水鬼（中段预留）

| class id | 常量 | 说明 |
|---|---|---|
| 22 | ArmoredCVHime | 装甲空母姬 |
| 23 | AnchorageHime | 泊地姬 |
| 24 | HarbourWD | 港湾水鬼 |
| 25 | AnchorageWD | 泊地水鬼 |
| 32 | SouthernHime | 南方栖姬 |
| 34 | CLDemon | 轻巡鬼 |
| 35 | BBWD | 战舰水鬼 |
| 40 | STHime | 海峡姬（ST） |
| 41 | AirdefenseHime | 防空姬 |
| 42 | PTImp | PT 小鬼 |
| 43 | CLHime | 轻巡姬 |
| 45 | DDWD | 驱逐水鬼 |
| 50 | SupplyDepotHime | 补给地姬 |

### 后期 event 姬 / 水姬（66–84，已实装 SSNH=72 除外）

| class id | 常量 | 说明 |
|---|---|---|
| 66 | STWH | 海峡水姬 |
| 67 | NorthernWH | 北方水姬 |
| 68 | JellyfishHime | 水母姬 |
| 69 | EscortHime | 护卫姬 |
| 70 | EuropeanHime | 欧州姬 |
| 71 | CentralHime | 中枢姬 |
| 73 | FrenchHime | 法国姬 |
| 74 | NightStraitHime | 夜海峡姬 |
| 75 | EntombedAAHime | 对空埋姬 |
| 76 | NorthlandHime | 北陆姬 |
| 77 | SupplyDepotSH | 补给地水姬 |
| 78 | SSSH | 潜水水姬 |
| 79 | BBSH | 战舰水姬 |
| 80 | CASH | 重巡水姬 |
| 81 | CVSH | 空母水姬 |
| 82 | HarbourSH | 港湾水姬 |
| 83 | LycorisHime | 彼岸花姬 |
| 84 | TwinHime | 双子姬 |

## 后续：创建新舰（尚未实现）

指定舰名后，优先使用上表已有 class id。最小路径：复用相近舰（深海小型级可参考驱逐イ级）→ 新 `Entity*` → `ModEntities` 与属性 → 蛋 → `ClientSetup` 模型层与贴图 → lang / 图鉴。模型在 Blockbench 完成后抄入 `createBodyLayer()`。

驱逐ナ级（驱逐舰 NA 级，`DDNA = 64`）的 Blockbench 与接入步骤见 [docs/project/ADD_DESTROYER_NA.md](docs/project/ADD_DESTROYER_NA.md)。
