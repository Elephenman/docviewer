# DocViewer

轻量级 Android 文档阅读编辑器 — 支持 HTML 和 Markdown 文件的阅读与编辑。

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Min SDK: 24](https://img.shields.io/badge/Min%20SDK-24-orange)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple.svg)](https://kotlinlang.org)

## 功能

- **文件浏览** — 内置文件浏览器，过滤 `.html` / `.md` 文件，支持 SAF 打开
- **阅读模式** — WebView 渲染 HTML，Markdown 自动转换为 HTML 后渲染
- **编辑模式** — 原生 Compose 文本编辑器，底部快捷栏辅助输入
- **模式切换** — 底部导航栏一键切换「阅读」/「编辑」
- **自动保存** — 编辑时 debounce 500ms 自动保存，不丢内容

## 技术栈

| 组件 | 技术 |
|------|------|
| 语言 | Kotlin |
| UI | Jetpack Compose + Material3 |
| 依赖注入 | Hilt |
| 导航 | Navigation Compose |
| Markdown 渲染 | commonmark-java |
| 构建 | Gradle 8.11 (Kotlin DSL) |

## 架构

单 Activity + Repository 模式：

```
MainActivity (Hilt)
  └── Navigation (FileBrowser → Document)
        ├── FileBrowserScreen  — SAF 文件选择
        ├── ViewerScreen       — WebView 渲染
        └── EditorScreen       — Compose 编辑器
```

## 下载

前往 [Releases](https://github.com/Elephenman/docviewer/releases) 下载最新的 APK 安装包。

> 当前版本: v1.0.1

## 构建

```bash
# 克隆仓库
git clone git@github.com:Elephenman/docviewer.git
cd docviewer

# macOS / Linux
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

APK 文件生成在 `app/build/outputs/apk/debug/` 目录下。

## 要求

- Android 7.0 (API 24) 及以上
- 存储读取权限

## 开发计划

- [x] 文件浏览 (SAF)
- [x] HTML / Markdown 阅读与渲染
- [x] Compose 编辑器 + 自动保存
- [x] 阅读/编辑模式切换
- [ ] Release 签名 APK
- [ ] 暗色主题
- [ ] 多标签页支持

## License

MIT © 2026 Elephenman
