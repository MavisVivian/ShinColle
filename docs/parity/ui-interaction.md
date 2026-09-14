# UI and interaction parity

Search/open only when the task touches this domain. Status codes: see `INDEX.md`.

## GUI

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Ship inventory GUI | legacy GUI → `GuiShipInventory` | NA | Layout + interaction semantics |
| Formation | legacy GUI → `GuiFormation` | PC | A: menu-context authorization GameTests; M: client-side layout and all formation controls; Server rejects formation mutations unless the player has a live formation menu opened from a held pointer |
| Small shipyard | legacy GUI → `GuiSmallShipyard` | NA | — |
| Large shipyard | legacy GUI → `GuiLargeShipyard` | NA | — |
| Desk | legacy GUI → `GuiDesk` | PC | A: protected desk-operation GameTests; M: radar/book client flow; Desk mutations require the matching live menu and held desk item |
| Crane | legacy GUI → `GuiCrane` | PC | A: compile + full GameTest regression; M: visually inspect NOT markers and transfer behavior; Restored persistent per-slot exclusion filters and docked-ship name display |
| Vol Core | legacy GUI → `GuiVolCore` | NA | — |
| Recipe paper | legacy GUI → `GuiRecipePaper` | PC | A: compile + full GameTest regression; M: client click/close/reopen flow; Item use opens its menu, ghost recipe/result persist, and right-click clears a pattern slot |

## Interaction

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Pointer / owner commands | legacy item/input flow → `PointerItem`, input/network handlers | PC | A: pointer menu/authorization GameTests; M: client sprint-hotbar shortcuts and wall occlusion; Server validates held pointer, mode, range, LOS, world bounds and menu context, while synchronizing all six pointer modes |
| Fishing | legacy ship fishing behavior → current `EntityShipFishingHook` flow | NA | — |
| Mount/riding | legacy mount flow → current mount entities/input | PC | A: mount input movement/validation/lease GameTests; M: summon visuals and sustained client steering; Restored eight Hime mount factories and a server-side ridden-movement fallback when vanilla travel makes no progress |

## Audio/Visual

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Emotion / reaction feedback | legacy packets/sounds/render state → current packets/sounds/emotion helpers | NA | — |
