# Navigation and entity parity

Search/open only when the task touches this domain. Status codes: see `INDEX.md`.

## Navigation

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Ship path navigation | `ShipPathNavigate` → `ShipNavigation` | NA | Path result and motion feel both matter |
| Path finder | `ShipPathFinder` → `ShipPathFinderCore` / vanilla `PathFinder` integration | NA | — |
| Move helper/control | `ShipMoveHelper` → `ShipMoveControl` | PC | A: representative friendly/hostile forward-intent/speed, hostile water displacement, and dynamic Levitation ascent GameTests; M: owner pursuit on land/water/air and formation speed across hull archetypes; Restored normalized forward input, hostile shared water travel, and runtime flight-state checks |

## Entity

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Owner/taming interaction | legacy `BasicEntityShip` and helpers → current `BasicEntityShip` / `TamableAnimal` | NA | — |
| Ship persistent state | legacy ship NBT/capability state → `CapaShipSavedValues` / current entity state | NA | — |
| Inventory | legacy ship inventory capability/container → current `CapaShipInventory` / `ContainerShipInventory` | NA | — |
