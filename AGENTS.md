# AGENTS.md — ShinColle-Reforge

## Mission and precedence

Reproduce the player-facing UX/gameplay of legacy ShinColle in `temp_1_10_2` on Minecraft Forge 1.20.1.

Priority when requirements conflict:

1. explicit user instructions
2. legacy player-visible behavior in `temp_1_10_2`
3. correct/stable Forge 1.20.1 behavior
4. maintainability and future work
5. current Reforge architecture
6. minimal diff / style cleanup

The current architecture is not the specification. Redesign it when parity or modern correctness requires it.

Project baseline: Minecraft `1.20.1`, Forge `47.4.0`, Parchment `2023.09.03-1.20.1`, Java `17`.

## Hard rules

- Treat `temp_1_10_2` as read-only unless the user explicitly asks to edit the legacy project.
- Port behavior, timing, state transitions, interactions, audiovisual feedback, and quirks that affect players; do not mechanically port old APIs or class structure.
- Do not reproduce crashes, data corruption, or invisible legacy defects for parity.
- Verify uncertain Minecraft/Forge/Parchment APIs from this repository/dependencies or version-appropriate sources; do not guess from similar method names.
- Keep common/server code dedicated-server safe.
- Before changing registry IDs, NBT/save layout, packets, or world data, assess compatibility and report unavoidable breakage.
- Avoid two independent authorities for the same AI state/action (target, movement intent, flee state, cooldown, etc.).
- Preserve unrelated user changes. Never use destructive Git cleanup/reset without explicit permission.

## Context budget

Token efficiency is a project requirement.

- Start from the user request and the narrowest relevant symbols. Expand only when evidence requires it.
- Do **not** preload all files in `docs/`, bulk-read the repository, or dump large logs.
- Use `docs/INDEX.md` as the document router and read only task-triggered documents/sections.
- Search first; read focused ranges/files second. Reuse evidence already established in the parent thread.
- For parity status, search `docs/parity/` first; open/update only matching rows. Treat `docs/parity/CLOSED.md` as cold history.
- Stop investigation once the behavioral contract, current path, and change surface are sufficiently supported.
- Prefer concise evidence summaries with exact paths/symbols over copied source or long narration.
- Add nested `AGENTS.md`/`AGENTS.override.md` only for durable subtree-specific rules; keep scoped overrides small.

### Long-session continuity

For work likely to cross compaction, interruption, or another session, maintain `.codex/state/active.md` per `.codex/state/README.md`: <=2 KB, rewrite instead of append, main agent only. On a resume/continue request, read an existing cache before broad investigation. If context becomes unclear, recover from `git status`, `git diff --stat`, targeted files/diffs, then the cache; repository evidence wins over chat/state. Stop repeated searches/tests that produce no new evidence. Delete the cache when done.

## Adaptive workflow

Classify the task before expanding context:

- **Legacy parity:** trace the relevant legacy behavior and current Reforge path, define the observable gap, then implement the smallest reliable modern design.
- **New feature:** explicit user requirements are the spec; inspect legacy only where the new feature can alter existing UX.
- **Infrastructure/tooling:** prioritize correctness and avoid unrelated gameplay analysis.

For non-trivial legacy work:

1. identify legacy trigger, visible sequence, timing/state, and stop/reset behavior
2. map the current execution path and state owner(s)
3. state the observable parity gap
4. choose architecture; current code may be replaced
5. implement a bounded change
6. run the narrowest meaningful validation
7. add broader/manual parity checks only when the behavior requires them
8. update the relevant `docs/parity/` row when status materially changes

Compilation is evidence of Java/API correctness only, not UX parity.

## Repository investigation

Follow concrete call/state paths rather than generic checklists. Depending on the task, inspect only the relevant parents/interfaces/callers, selectors or Brain, navigation/MoveControl, persistence, networking, GUI/menu, config, or registries.

For AI/scheduling bugs, check priority/flags, `canUse`, continuation/start/tick/stop, competing behavior, authoritative target/state, navigation, and timers as applicable. For movement, judge player-visible motion rather than class similarity.

## Delegation

The main Sol agent owns architecture, integration, and final judgment. Skip delegation for small/local work. For work that benefits from specialization or parallelism, read `docs/agents/CODEX_WORKFLOW.md`; normally use only 2–3 independent roles at once, pass already-known evidence instead of whole docs, and require compact result summaries rather than raw logs or repeated project background.

## Validation and completion

Use the Gradle Wrapper. Start narrow (`compileJava` or a relevant test); broaden to `build`, GameTests, or manual/in-game checks only when justified by risk or player-visible behavior.

A legacy task is complete when the main agent can explain, concisely:

- legacy player-visible behavior
- pre-change observable gap
- implemented behavior/design
- validation actually run
- remaining manual checks or intentional differences

Do not claim checks that were not run.

## Document routing

Read `docs/INDEX.md` only when task-specific detail beyond this file is needed. In particular, do not treat every document as mandatory startup context.
