# Codex workflow

Use only when a task needs explicit orchestration; `AGENTS.md` is the default path.

## Execution gates

Do not run a fixed pipeline. Run a gate only when its condition is true.

| Gate | Run when | Skip/reuse when |
|---|---|---|
| Legacy trace | parity depends on legacy UX and exact behavior is unknown | same contract is established from unchanged legacy symbols |
| Current map | ownership/execution path is unclear | relevant current path is established and unchanged |
| Forge/API research | uncertain 1.20.1/Forge/Parchment semantics affect correctness | repository/dependency evidence or compilation resolves it |
| Subagent | specialization/parallelism saves more parent context/time than handoff cost | task is local, coupled, or already understood |
| Test design | behavioral acceptance exists but the smallest useful check is unclear | obvious focused check already covers it |
| Independent review | change is consequential/cross-cutting | low-risk local/mechanical change has focused evidence |
| Broad build | integration risk extends beyond the edited slice | narrow validation proves the relevant property |

Stop when behavioral contract, change surface, and validation path are supported. New checks should answer a remaining question, not repeat confidence.

## Default routes

- **Local/mechanical:** Sol edits + narrow validation; no subagent by default.
- **Bounded parity:** fill only missing legacy/current evidence, normally with <=2 read-only agents; implement; validate narrowly.
- **Cross-subsystem:** Sol fixes contract/architecture first; delegate non-overlapping slices; integrate once; one focused review.
- **Compatibility-sensitive** (save/NBT, registry, packet, world data): verify assumptions before editing; broaden validation by risk.

## Minimal handoff

```text
Question: <one output/decision needed>
Known: <established facts; do not re-derive>
Scope: <files/symbols/boundary>
Stop when: <enough evidence>
Return: <compact result shape>
```

Do not attach whole docs, project recaps, or unrelated logs. Treat `Known` as established unless repository evidence contradicts it; report contradictions instead of silently restarting broad investigation.

## Roles and parallelism

Roles: `legacy_ux_analyst`, `reforge_mapper`, `forge_api_researcher`, `implementation_worker`, `build_verifier`, `parity_reviewer`, `parity_test_designer`.

Parallelize independent read-only questions. Prefer 1 worker; use 2–3 only when outputs are independent and useful. Avoid concurrent edits to coupled files. Perform one integration/review pass after workers finish, not one review per worker.

Escalate to Sol before widening scope for persistent-format/registry/network breakage, cross-subsystem hierarchy redesign, ambiguous legacy UX, or parity vs safety/stability conflicts.

## Evidence reuse

Do not pay twice for unchanged evidence.

- Reuse legacy contracts while referenced legacy symbols are unchanged.
- Reuse current maps while relevant current symbols are unchanged.
- Reuse API findings for the same dependency/version and assumption.
- Reuse validation only while its declared scope/prerequisites are unchanged; see `docs/project/BUILD_AND_VALIDATION.md`.
- On long work, keep durable conclusions only in `.codex/state/active.md`, not worker narratives.

Re-delegate/re-run only after relevant state changed, material uncertainty remains, or new failure contradicts prior evidence.

## Output and loop discipline

Prefer `git status --short`, `git diff --stat`, path-scoped diffs, `rg -n`, focused ranges, and narrow Gradle tasks. Capture noisy commands and return relevant diagnostics only; avoid full trees/diffs/logs.

For long work, rewrite `.codex/state/active.md` at meaningful milestones, not every tool call. Do not repeat the same search, delegation, review, or validation a third time unless the hypothesis or repository state changed. When stuck, choose one discriminating check, bounded fix, or explicit blocker instead of another broad pass.
