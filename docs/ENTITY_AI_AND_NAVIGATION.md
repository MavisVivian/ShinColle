# Entity AI and navigation

Read for Goal scheduling, target ownership, or movement/pathing bugs.

## Goal scheduling

When a Goal never starts, runs constantly, or blocks another, inspect only the relevant chain: selector registration, priority, flags, competing running Goals, `canUse`, `canContinueToUse`, `start/tick/stop`, target/state owner, navigation state, and timer gates.

Flags are scheduler resources: a higher-priority usable/running Goal can prevent another Goal from being evaluated or activated. For modulo/tick gates, verify both the tick source and whether the selector actually evaluates the Goal on those ticks.

## Targets and authority

Distinguish mob target, custom attack target, owner/owner target, navigation destination, and temporary Goal targets. Prefer one authoritative combat target; keep wrappers/synchronization thin.

## Navigation

Reason from semantics, not old method names:

```text
behavior intent -> PathNavigation -> MoveControl -> entity motion
```

For legacy migration, determine what the old navigation call meant, then verify the modern operation's completion/stop/repath behavior. For flying ships, also inspect vertical movement, speed, collision/path assumptions, floating/water behavior, and owner-follow teleport rules.

Attack behaviors additionally need range/LOS when relevant, cooldown/cadence, navigation-to-target, stop conditions, target removal/death, faction/owner checks, and scheduling conflicts.

Use temporary logs for state transitions and competing behavior, not every-tick dumps; remove noisy diagnostics after diagnosis.
