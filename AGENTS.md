# Repository Agent Instructions

## Source Lookup

Choose your lookup method strictly based on whether the code is local or an external dependency:

- **Project Source Code (Local Files):** You may freely use fast command-line utilities (`rg`, `grep`, `find`, `sed`) or IntelliJ IDEA MCP tools to search and inspect local project files.
- **Library & Dependency Code (External JARs):**
  1. **MUST Try First:** ALWAYS use the IntelliJ IDEA MCP tools together with the `workspace-agent-bridge` skill to inspect third-party or library code. Do NOT bypass this step for speed.
  2. **Last-Resort Fallback:** ONLY if the IDEA MCP tool and `workspace-agent-bridge` explicitly fail or return empty results, you may fall back to searching source JARs under `~/.gradle/caches/`.

## Scope and Existing Work

- Make the smallest change that fully satisfies the request.
- Follow nearby project patterns and existing APIs before introducing new abstractions.
- Preserve unrelated working-tree changes. Do not revert, clean up, or reformat files outside the requested scope.
- Do not broaden a file-specific request into a repository-wide refactor unless explicitly asked.

## Code Style

- Do not add a Checkstyle suppression merely to bypass a violation. Fix the code or document a genuinely required
  exception within the requested scope.
- Prefer self-explanatory code and avoid comments that merely restate what the code already makes clear.
- Keep text files UTF-8 encoded and preserve the existing line-ending style of files being edited.
- FQCN is strictly prohibited, use import if possible.

## Nullness Annotations

- Do not use nullness helper annotations from any framework other than JSpecify.
- Use only annotations from `org.jspecify.annotations` when nullness annotations are needed.
- Do not introduce legacy nullness annotations or defaults such as `javax.annotation.Nullable`,
  `javax.annotation.ParametersAreNonnullByDefault`, `net.minecraft.MethodsReturnNonnullByDefault`, or JetBrains
  nullness annotations.
- Existing legacy annotations are not precedent for new code. Do not migrate unrelated existing code unless the task
  explicitly requests it.

## Test Authoring

- Do not add or expand automated tests unless the user explicitly requests them or a regression cannot be verified otherwise.
  Prefer compilation and focused manual validation.

## Verification

- Run focused checks that cover the changed behavior before broader validation.
- For Java source changes, run `./gradlew.bat compileJava --console=plain` on Windows.
- When generated data or assets change, run `./gradlew.bat runData --console=plain` when applicable and inspect the
  generated diff.
- Run `git diff --check` for tracked changes and inspect `git status --short` so untracked artifacts are not missed.
- Report which verification commands were run and identify any checks that could not be completed.
