# Documentation router

`AGENTS.md` contains the always-on rules. Read documents below only when the task trigger applies.

| Trigger | Read |
|---|---|
| Need current project versions/entry points | `PROJECT_CONTEXT.md` |
| Trace legacy source/behavior | `LEGACY_SOURCE_GUIDE.md` |
| Port a legacy subsystem/API | `LEGACY_PORTING_RULES.md` |
| Forge/Parchment lifecycle, mapping, registration, networking, persistence | `FORGE_1_20_1_RULES.md` |
| Ship AI, Brain state ownership, Goal migration | `AI_BRAIN_MIGRATION.md` |
| Goal scheduling/navigation debugging | `ENTITY_AI_AND_NAVIGATION.md` |
| Find legacy ↔ current AI class correspondence | `LEGACY_CURRENT_AI_MAP.md` |
| Need detailed task orchestration/delegation | `CODEX_WORKFLOW.md` |
| Long task may cross compaction/interruption/session boundary | `.codex/state/README.md` |
| Compile/test/manual parity validation | `BUILD_AND_VALIDATION.md` |
| Check/update project parity status | `parity/INDEX.md`, then only matching row(s) |
| User wants a reusable task request | `TASK_REQUEST_TEMPLATE.md` |
| A consequential architecture choice needs a durable record | `DECISION_RECORD_TEMPLATE.md` |

Rules:

- For parity, search first and open only the matching shard/range; `parity/CLOSED.md` is cold history.
- Do not read AI documents for unrelated GUI/item/build tasks.
- Do not read templates unless creating a request/record.
- Prefer repository code over snapshot prose when they disagree; update stale docs if the task materially changes documented architecture.

## Documentation budget

- One fact/rule should have one owner; link to it instead of copying it into multiple docs.
- New durable docs must have a task trigger in this router; unlisted docs are not startup context.
- Soft limits: root `AGENTS.md` ~6 KB, ordinary routed docs ~4 KB, active parity shard ~6 KB, `.codex/state/active.md` 2 KB. These are prompts to prune/split, not hard truncation targets.
- When a file grows past its useful working-set size, split by task trigger or move completed/history material to cold storage rather than raising every task's context cost.
