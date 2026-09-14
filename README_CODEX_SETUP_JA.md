# ShinColle-Reforge Codex設定

このリポジトリは、メインを `gpt-5.6-sol`、必要な作業だけ `gpt-5.6-luna` サブエージェントへ委譲する構成です。常時コンテキストを小さく保つことを優先しています。

## 重要ファイル

- `AGENTS.md`: 常時必要な最小ルール
- `docs/INDEX.md`: 必要時だけ読む文書のルーター
- `.codex/config.toml`: モデル・サブエージェント設定
- `.codex/agents/`: 7つの限定ロール
- `FIRST_PROMPT.txt`: 最小の開始プロンプト

サブエージェントは `legacy_ux_analyst`, `reforge_mapper`, `forge_api_researcher`, `implementation_worker`, `build_verifier`, `parity_reviewer`, `parity_test_designer` のみです。

## 使い方

新しいCodexセッションをリポジトリルートで開始し、通常はタスクだけ伝えます。`AGENTS.md` にプロジェクト方針があるため、毎回バージョン・UX優先順位・全エージェント手順をプロンプトへ再掲する必要はありません。

複雑なタスクでも全ロールを固定で起動せず、旧版調査・現行調査・API確認など独立性のあるものだけ並列化します。詳細資料は `docs/INDEX.md` のトリガーに従って必要なときだけ読みます。

`project_doc_max_bytes = 8192` は、`AGENTS.md` とスコープ別overrideの肥大化を防ぐためのガードです。現状の常時指示はこの範囲に収まります。

## 長期セッション

コンテキスト圧縮・中断・別セッションをまたぐ可能性がある作業では、メインエージェントが Git 管理外の `.codex/state/active.md` を現在地キャッシュとして使用します。2 KB以内を目安に追記ではなく上書き圧縮し、詳細は `.codex/state/README.md` を参照します。
