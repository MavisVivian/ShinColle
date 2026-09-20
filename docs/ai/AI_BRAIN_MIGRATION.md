# AI / Brain migration

Read only for ship-AI architecture/state-ownership work.

`ShipBrain` is transitional: Brain-related state coexists with Goals that still own much movement/weapon execution. Legacy UX parity is the requirement; Brain usage is optional.

## Core rule

Each important action/state needs one authority. Avoid independent Goal/Brain/custom-state control of the same combat target, movement intent, flee/activity state, owner/guard mode, or cooldown.

Before moving a behavior:

1. extract its legacy trigger/timing/competition/stop semantics
2. map current Goal/Brain/navigation state
3. choose the new authoritative state/controller
4. prevent old and new schedulers from issuing the same action
5. validate observable behavior before removing compatibility logic

Adapters may synchronize representations during migration, but document which one is authoritative. Examples that commonly drift are `Mob#getTarget()`, custom attack-target fields, and `MemoryModuleType.ATTACK_TARGET`.

Choose migration slices by parity gap, coupling, state ownership, testability, and risk. A custom controller or Goal is acceptable when it reproduces legacy behavior more reliably than vanilla Brain abstractions.

For acceptance, compare trigger, competition/priority, target/movement semantics, timing/cooldown, stop/reset, owner/team behavior, deterministic checks, and any necessary in-game feel check.
