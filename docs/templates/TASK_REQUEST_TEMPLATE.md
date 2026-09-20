# Codex task request template

`AGENTS.md` already contains project rules, versions, delegation policy, and validation defaults. Do not repeat them in each task.

```text
Task:
<what to implement/fix>

Expected player-visible result:
<desired behavior, or "match temp_1_10_2" if appropriate>

Known legacy/current references (optional):
<class, file, symptom, reproduction steps, logs>

Extra constraints (optional):
<only constraints specific to this task>
```

Codex should infer repository-resolvable details, use only the subagents/docs that materially help, implement when implementation was requested, and report actual validation plus remaining manual checks.
