# Build and validation

Use the repository Gradle Wrapper and the cheapest check that can fail meaningfully.

```text
Windows: .\gradlew.bat compileJava
Unix:    ./gradlew compileJava
Broader: ./gradlew build
```

Use relevant existing GameTests under `com.lulan.shincolle.gametest` for deterministic behavior. Broaden only when the edited surface/risk requires it.

## Validation ladder

1. compile affected code
2. focused deterministic test/GameTest when useful
3. broader build only for integration risk
4. manual/in-game scenario for perceptual behavior

Deterministic checks suit state transitions, targeting, inventories, cooldown boundaries, damage, ownership/team logic, spawn/despawn, and save round trips. Manual checks suit movement/pathing feel, responsiveness, GUI layout/usability, sound, particles, animation, and combat pacing.

A successful compile proves Java/API plausibility, not UX parity.

## Reuse and invalidation

Before rerunning a successful check, ask what changed since it passed.

- `compileJava`: reuse if no relevant Java/source-generation/build configuration changed afterward.
- focused test/GameTest: reuse if neither the test nor code/config that can affect its behavior changed.
- API/dependency verification: reuse while dependency versions/mappings and the exact assumption are unchanged.
- manual/in-game result: reuse only for the same relevant build/behavior; invalidate when player-visible behavior in its path changes.

For long tasks, record compact evidence in `.codex/state/active.md`, for example:

```text
Validation:
- compileJava OK; scope=AI files + build config; valid while unchanged
- ShipTarget GameTest OK; scope=target selection/ownership
```

Do not create hashes or elaborate cache machinery unless ambiguity actually requires it. A targeted `git diff --name-only -- <scope>` is usually enough to decide whether evidence is stale.

## Output and failure handling

Start without `--stacktrace` or debug logging. Surface the first relevant diagnostics plus a short tail; escalate verbosity only when current output cannot identify the cause. Never paste full build logs into agent context.

Classify failures as change-caused, pre-existing, test, dependency/network, toolchain, environment, or unrelated. Do not hide failures or claim unrun validation. Do not rerun an unchanged expensive check merely to reconfirm it.

For manual parity, record only: setup/action, expected visible sequence, timing/range tolerance when meaningful, failure signs, and result.
