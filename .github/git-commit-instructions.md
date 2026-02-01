# Git Commit Instructions

## Language Requirement
Commit message subject/body must be written in Japanese, and type must be written in English.
(Type must always be in English, subject/body must always be in Japanese.)

## Commit Message Format

### Structure
```
<type>: <subject>

<body> (optional)

<footer> (optional)
```

### Type (種類)
Use one of the following types in English, but write subject and body in Japanese:
- **feat**: 新機能の追加
- **fix**: バグ修正
- **docs**: ドキュメントのみの変更
- **style**: コードの意味に影響を与えない変更（空白、フォーマット、セミコロンなど）
- **refactor**: バグ修正や機能追加を伴わないコードの変更
- **perf**: パフォーマンス向上のための変更
- **test**: テストの追加や修正
- **chore**: ビルドプロセスやツールの変更、ライブラリの更新など

### Subject (件名) - Japanese
- Write in Japanese using polite form or declarative form
- Keep it concise (50 characters or less recommended)
- Do not end with period (。)
- Start with verb when possible
- Describe WHAT changed, not HOW

### Body (本文) - Japanese (Optional)
- Write in Japanese
- Explain WHY the change was made
- Provide context and reasoning
- Separate from subject with blank line
- Wrap at 72 characters per line

### Footer (フッター) - (Optional)
- Reference issue numbers: `Refs #123`
- Breaking changes: `BREAKING CHANGE: 説明`
- Close issues: `Closes #123`

## Examples

### Example 1: Feature Addition
```
feat: ファイルアップロード機能を追加

ユーザーがファイルをアップロードして、
データを自動的に処理できるようにした。

Refs #45
```

### Example 2: Bug Fix
```
fix: 日付パース時のタイムゾーンエラーを修正

日付を読み取る際に、
タイムゾーンの違いにより日付がずれる問題を修正。
UTC基準で処理するよう変更。

Closes #78
```

### Example 3: Refactoring
```
refactor: データ処理サービスのメソッドを分割

可読性向上のため、大きなメソッドを
複数の小さなメソッドに分割した。
機能的な変更はなし。
```

### Example 4: Documentation
```
docs: README.mdにデータ処理の説明を追加
```

### Example 5: Test
```
test: データ処理サービスの単体テストを追加

正常系、異常系、エッジケースのテストケースを追加。
サンプルファイルもtest/resourcesに配置。
```

### Example 6: Chore
```
chore: ライブラリのバージョンを更新

セキュリティ脆弱性対応のためライブラリをアップデート。
```

## Best Practices

### Do's ✅
- Use Japanese for subject and body
- Keep subject concise and clear
- Use present tense verbs (追加、修正、削除、変更)
- Reference issue numbers when applicable
- Write meaningful commit messages that explain WHY
- Make atomic commits (one logical change per commit)

### Don'ts ❌
- Don't mix languages (English and Japanese) in subject
- Don't write vague messages like "修正" or "更新" alone
- Don't commit multiple unrelated changes together
- Don't exceed 50 characters for subject (as guideline)
- Don't use casual Japanese (use polite or neutral form)

## Common Verbs in Japanese
- 追加 (add)
- 修正 (fix)
- 削除 (delete)
- 変更 (change)
- 更新 (update)
- 実装 (implement)
- 改善 (improve)
- リファクタリング (refactor)
- 最適化 (optimize)
- 統合 (integrate)
- 分離 (separate)
- 移動 (move)
- 名前変更 (rename)

## Commit Frequency
- Commit often, but keep each commit meaningful
- Commit after completing a logical unit of work
- Commit before switching tasks or branches
- Don't commit broken code to main/develop branches

## Before Committing
- [ ] Run tests: `./gradlew test`
- [ ] Check code style
- [ ] Review your changes
- [ ] Write meaningful commit message in Japanese
- [ ] Reference related issues if any
