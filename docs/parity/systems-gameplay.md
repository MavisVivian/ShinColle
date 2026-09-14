# Systems and gameplay parity

Search/open only when the task touches this domain. Status codes: see `INDEX.md`.

## Network

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Entity sync | legacy S2C sync → `S2CEntitySyncPacket` | NA | Judge resulting UX, not packet similarity |
| GUI sync/input | legacy GUI packets → current GUI packets | NA | — |

## Gameplay

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Config-driven behavior | legacy configs → `ConfigHandler` / current config | NA | — |
| Team/faction targeting | legacy Team/Target helpers → current `TeamData`, `TargetHelper` | NA | — |
| World/resource progression | legacy worldgen/loot → current worldgen/loot/datagen | NA | Player progression parity |
