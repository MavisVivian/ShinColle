# UX parity index

Use this as a router, not as a checklist to preload. Search first, then open only the matching shard/range.

Status codes: `NA` not audited · `IP` in progress · `PC` parity candidate · `OK` verified · `DIFF` accepted known difference.

| Domain | Active shard |
|---|---|
| AI, combat, carrier, aircraft | `ai-combat.md` |
| Navigation, entity state/inventory | `navigation-entity.md` |
| GUI, interaction, audiovisual feedback | `ui-interaction.md` |
| Network and gameplay systems | `systems-gameplay.md` |

Workflow:

1. `rg -n -i "<feature|legacy symbol|current symbol>" docs/parity`
2. Open only the matching row and enough surrounding context to understand it.
3. Update the row only when evidence/status materially changes.
4. When a row becomes `OK`, or an intentional `DIFF` is accepted, move it to `CLOSED.md`; do not duplicate it.
5. Do not read `CLOSED.md` unless search results or the task point there.

The active shards are the development working set. Keeping completed rows cold prevents project history from becoming routine model context. If an active shard grows past roughly 6 KB, split it by subsystem and update only this router; do not make every task read a larger tracker.
