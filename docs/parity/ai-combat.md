# AI and combat parity

Search/open only when the task touches this domain. Status codes: see `INDEX.md`.

## AI

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Follow owner | `EntityAIShipFollowOwner` → `ShipFollowOwnerGoal` | PC | A: core guards, leash block, legacy formation pickup distance; M: same-/cross-dimension lifecycle and safe teleport; Owner resolution is now same-level only and teleport uses the shared safety gate |
| Sit / wait | `EntityAIShipSit` → `ShipSitGoal` | NA | — |
| Flee | `EntityAIShipFlee` → `ShipFleeGoal` / `ShipBrain` PANIC mirror | PC | A: health/owner-distance/sit/leash/grudge activation guards; M: failed-path teleport with config on/off and mounted ship; Restored legacy cooldown, distance, same-level and teleport gates |
| Guarding | `EntityAIShipGuarding` → `ShipGuardingGoal` | NA | — |
| Wander | `EntityAIShipWander` → `ShipWanderGoal` | PC | M: random movement while idle and suppression during crane/fishing; Uses `DefaultRandomPos` and restores crane/fishing guards |
| Floating | `EntityAIShipFloating` → `ShipFloatingGoal` | NA | Movement feel matters |
| Pick item | `EntityAIShipPickItem` → `ShipPickItemGoal` | NA | Check Goal blocking interactions |
| Revenge target | `EntityAIShipRevengeTarget` → `ShipRevengeTargetGoal` | NA | — |
| Range target | `EntityAIShipRangeTarget` → `ShipRangeTargetGoal` | NA | — |
| Fuel-driven selector lifecycle | legacy `BasicEntityShip` fuel/task refresh → deferred refresh in `BasicEntityShip.aiStep` | PC | A: Selector refresh GameTest passes; M: exhaust/refuel during active combat; Fuel changes apply at the next safe server AI boundary; running Goals are stopped before replacement |
| Watch closest | `EntityAIShipWatchClosest` → vanilla/custom look Goal in current ship setup | NA | Verify exact modern owner |
| Look idle | `EntityAIShipLookIdle` → current look-around behavior | NA | Verify exact modern owner |
| Open door | `EntityAIShipOpenDoor` → `ShipOpenDoorGoal` | NA | — |

## Combat

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Ranged attack | `EntityAIShipRangeAttack` → `ShipRangeAttackGoal` | IP | A: Compile/build + regression suite; M: UseMelee chase + cannon animation/particles; Restored legacy strict range boundary, 32-tick pursuit refresh, 64-tick combat-parameter refresh, and attacker/target feedback; movement feel and LOS timing still need in-game verification |
| Melee/collide attack | `EntityAIShipAttackOnCollide` → `ShipAttackOnCollideGoal` | NA | — |
| Skill attack | `EntityAIShipSkillAttack` → `ShipSkillAttackGoal` | IP | A: compile + full GameTest regression; M: visually verify Tatsuta charge/spin/beam timing and damage; Restored Tatsuta's legacy multi-phase heavy attack and final beam lifecycle |
| Attack result text | `ParticleHelper.spawnAttackTextParticle` / `ParticleTexts` → same symbols | PC | A: Compile + texture hash comparison; M: inspect Miss/Critical/Double/Triple/Dodge at normal and near-camera distances; Restored the legacy server-side packet condition; texture and world-space quad dimensions match legacy, so close-camera scaling was not altered |

## Carrier

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Carrier attack | `EntityAIShipCarrierAttack` → `ShipCarrierAttackGoal` | IP | A: Hostile carrier GoalSelector launch GameTest passes; M: visually confirm Akagi/Kaga launch timing and positioning; Restored legacy strict range boundary, 32-tick pursuit refresh, 64-tick combat-parameter refresh, hostile 10/10 effective aircraft stock, launch height, and Akagi/Kaga carrier Goal |

## Aircraft

| Feature | Legacy → current | St | Evidence / remaining |
|---|---|---|---|
| Aircraft attack | `EntityAIShipAircraftAttack` → `ShipAircraftAttackGoal` | IP | A: Compile + hostile carrier launch GameTest; M: launch target retention, 16-tick retarget cadence, return-to-host behavior; Preserves the launch target across selector rebuild, scans every 16 ticks, and falls back to the live host target |
