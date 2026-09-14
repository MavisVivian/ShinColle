# Long-session state

Use `.codex/state/active.md` only for work likely to span many tool calls, compaction, interruption, or another session. Small/local tasks do not need it.

`active.md` is a resume cache, not documentation or a journal. Keep it under **2 KB** and rewrite it in place; never append history.

Recommended shape:

```text
Goal: <one sentence>
Acceptance: <observable done condition>
Scope: <current files/subsystem>

Established evidence:
- <durable fact + path/symbol; omit re-derivable trivia>

Decisions:
- <decision + short reason>

Completed:
- <durable result>

Next:
1. <single next action>
2. <optional second action>

Blockers/unknowns:
- <only unresolved items>

Validation:
- <check -> result; scope=<what invalidates it>>
```

Update only after a meaningful milestone, scope/architecture decision, validation result, or before ending/interruption. Delete stale detail instead of preserving chronology. Prefer references/conclusions over logs, diffs, raw subagent output, or facts trivially recoverable from code.

When resuming, reconcile the cache with `git status --short`, `git diff --stat`, then targeted diffs/files. Repository state wins. Reuse established evidence whose referenced scope is unchanged; invalidate only the affected entries instead of restarting the whole investigation.

Only the main agent maintains this file. Subagents must not edit it. Delete it when the task is complete.
