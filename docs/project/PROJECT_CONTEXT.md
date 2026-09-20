# ShinColle-Reforge project context

Use this as a navigation aid. Repository code/build files are authoritative if this snapshot becomes stale.

## Runtime baseline

| Field | Value |
|---|---|
| Mod ID / root package | `shincolle` / `com.lulan.shincolle` |
| Minecraft / Forge | `1.20.1` / `47.4.0` |
| ForgeGradle | `[6.0.16,6.2)` |
| Mappings | Parchment `2023.09.03-1.20.1` |
| Java | `17` |
| Main mod class | `com.lulan.shincolle.ShinColle` |
| Entity registry | `com.lulan.shincolle.init.ModEntities` |
| Networking | `com.lulan.shincolle.network.ModNetworking` |
| Common config | `shincolle-common.toml` / `ConfigHandler` |

Legacy baseline: `temp_1_10_2`, Minecraft `1.10.2`, Forge `12.18.3.2221`, MCP `stable_29`, Java `8`. It is read-only.

## High-value entry points

Current ship AI is transitional: `BasicEntityShip` uses custom navigation/MoveControl and both Brain-related state (`ai/brain/ShipBrain.java`) and multiple Goals. Do not assume either side is final or authoritative without tracing the feature.

Useful areas when relevant:

- AI: `com/lulan/shincolle/ai`, `ai/brain`, `ai/path`
- entities: `com/lulan/shincolle/entity`
- GUI/menu: `client/gui`, `client/gui/inventory`
- persistence/state: `capability`, `ServerDataManager`, `ShinWorldData`
- helpers affecting gameplay: `TargetHelper`, `FormationHelper`, `InteractHelper`, `CombatHelper`, `EntityHelper`, `InventoryHelper`
- tests: `com/lulan/shincolle/gametest`

For specific legacy/current AI correspondence, use `docs/legacy/LEGACY_CURRENT_AI_MAP.md` instead of expanding this file.
