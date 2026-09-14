# ShinColle-Reforge Codex setup

This repository keeps always-on Codex context intentionally small.

- `AGENTS.md`: minimal project policy loaded for normal work
- `docs/INDEX.md`: route to task-specific docs only when needed
- `.codex/config.toml`: Sol root + bounded Luna subagents
- `.codex/agents/`: seven specialized roles
- `FIRST_PROMPT.txt`: minimal session starter

The canonical roles are `legacy_ux_analyst`, `reforge_mapper`, `forge_api_researcher`, `implementation_worker`, `build_verifier`, `parity_reviewer`, and `parity_test_designer`.

Start a new Codex session from the repository root after changing instruction/config files. Normal task prompts do not need to repeat project versions, parity policy, or a fixed multi-agent workflow; `AGENTS.md` already contains them.

Use `docs/INDEX.md` to load only the detail relevant to the current task. Delegate only when specialization or parallelism is useful rather than spawning all roles by default.

## Long sessions

For tasks likely to survive context compaction, interruption, or another session, the main agent uses the git-ignored `.codex/state/active.md` resume cache. It is capped at 2 KB and rewritten rather than accumulated. See `.codex/state/README.md`.
