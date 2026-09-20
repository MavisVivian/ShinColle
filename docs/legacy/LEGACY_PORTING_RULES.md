# Legacy porting rules

Port observable behavior, not legacy syntax or structure.

For the requested feature:

1. identify the player-visible contract and relevant timing/state
2. locate the modern subsystem that owns those semantics
3. verify uncertain 1.20.1/Parchment symbols
4. implement the smallest reliable equivalent behavior
5. validate what the compiler cannot prove

Do not preserve old MCP names, inheritance, event/packet patterns, Goals, navigation classes, or compatibility wrappers merely because they existed. Current Reforge abstractions may also be replaced when they block parity.

Take extra care with registry IDs, NBT keys, capabilities, world data, resource IDs, and network contracts. Prefer migration/compatibility when practical and report unavoidable breakage.

Common traps: method-name substitution without semantic checking; removed lifecycle hooks whose behavior moved elsewhere; old integrated-client assumptions; different GoalSelector scheduling semantics; and equating successful compilation with gameplay parity.
