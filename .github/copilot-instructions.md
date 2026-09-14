# ShinColle-Reforge Copilot instructions

Target Minecraft Forge 1.20.1 / Java 17. The behavioral reference is the read-only legacy source under `temp_1_10_2`.

Priority: explicit user request > legacy player-visible UX > correct Forge 1.20.1 behavior > maintainability > current architecture/minimal diff.

Port semantics and player-visible behavior, not old MCP names or source structure. Verify uncertain Forge/Parchment APIs instead of guessing. Keep common code dedicated-server safe and avoid casual registry/NBT/save/network breakage. Current AI/navigation/Brain design may be replaced when needed for parity.

For repository-specific detail, use `AGENTS.md` and read only the task-relevant document routed by `docs/INDEX.md`.
