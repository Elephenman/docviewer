# DocViewer 设计文档

> 轻量级 Android 文档阅读编辑器

## 项目信息

- **包名**: `com.elephenman.docviewer`
- **最低 SDK**: 24 (Android 7.0)
- **目标 SDK**: 35 (Android 15)
- **技术栈**: Kotlin + Jetpack Compose + WebView

---

## 功能范围

### 包含 (MVP)

1. **文件浏览**
   - 内置文件浏览器浏览手机存储
   - 通过系统 SAF (Storage Access Framework) 打开文件
   - 支持 `.html` 和 `.md` 文件过滤

2. **阅读模式**
   - WebView 渲染 HTML 文件
   - Markdown 文件通过 `commonmark-java` 转为 HTML 后渲染
   - 支持缩放、滚动

3. **编辑模式**
   - Compose `TextField` 编辑源码
   - 代码高亮 (HTML/MD 语法)
   - 自动补全基础标签

4. **模式切换**
   - 底部导航栏: 「阅读」/「编辑」
   - 切换时保持内容状态

5. **自动保存**
   - 编辑时 debounce 500ms 自动写入文件
   - 退出编辑模式时确保保存

### 不包含 (YAGNI)

- 云同步
- 多标签页
- 搜索功能
- 书签/历史
- 主题切换
- 插件系统

---

## 架构设计

### 模块结构

```
app/src/main/java/com/elephenman/docviewer/
├── DocViewerApp.kt                 # Application 类 (Hilt)
├── data/
│   ├── repository/
│   │   └── DocumentRepository.kt   # 文件读写、自动保存
│   └── model/
│       └── Document.kt             # 数据模型
├── ui/
│   ├── MainActivity.kt             # 主 Activity
│   ├── theme/
│   │   └── Theme.kt                # 主题配置
│   ├── filebrowser/
│   │   ├── FileBrowserScreen.kt    # 文件浏览界面
│   │   └── FileBrowserViewModel.kt # 文件浏览逻辑
│   ├── reader/
│   │   ├── ReaderScreen.kt         # 阅读模式界面
│   │   └── ReaderViewModel.kt      # 阅读逻辑
│   └── editor/
│       ├── EditorScreen.kt         # 编辑模式界面
│       └── EditorViewModel.kt      # 编辑逻辑
└── util/
    ├── MarkdownConverter.kt        # MD → HTML 转换
    └── FileUtils.kt                # 文件工具函数
```

### 数据模型

```kotlin
data class Document(
    val uri: Uri,                    // 文件 URI
    val name: String,                // 文件名
    val content: String = "",        // 文件内容
    val isModified: Boolean = false, // 是否修改未保存
    val type: DocumentType           // HTML or MARKDOWN
)

enum class DocumentType {
    HTML,
    MARKDOWN
}
```

### 数据流

```
用户选择文件
    ↓
DocumentRepository.loadDocument(uri)
    ↓
DocumentViewModel (共享状态)
    ↓
阅读模式 ←→ 编辑模式 (切换时共享 Document 状态)
    ↓
自动保存 → DocumentRepository.saveDocument()
```

---

## UI 设计

### 主界面结构

```
┌─────────────────────────────┐
│         TopAppBar            │
│      (文件名 + 保存状态)       │
├─────────────────────────────┤
│                             │
│                             │
│         Content              │
│      (WebView / Editor)     │
│                             │
│                             │
├─────────────────────────────┤
│  ┌─────────┐  ┌─────────┐  │
│  │  阅读   │  │  编辑   │  │
│  └─────────┘  └─────────┘  │
│       BottomNavigation      │
└─────────────────────────────┘
```

### 文件浏览器界面

```
┌─────────────────────────────┐
│  当前路径: /sdcard/documents │
├─────────────────────────────┤
│  📁 folder1                 │
│  📁 folder2                 │
│  📄 readme.md               │
│  📄 index.html              │
│  ...                        │
├─────────────────────────────┤
│  [从其他应用打开] [返回上级]  │
└─────────────────────────────┘
```

---

## 技术细节

### Markdown 渲染

使用 `commonmark-java` 库将 Markdown 转为 HTML:

```kotlin
val parser = Parser.builder().build()
val document = parser.parse(markdown)
val renderer = HtmlRenderer.builder().build()
val html = renderer.render(document)
```

渲染时注入 CSS 样式:

```html
<style>
  body { font-family: sans-serif; padding: 16px; line-height: 1.6; }
  h1, h2, h3 { color: #333; }
  code { background: #f5f5f5; padding: 2px 4px; border-radius: 3px; }
  pre { background: #f5f5f5; padding: 12px; overflow-x: auto; }
</style>
```

### 自动保存策略

```
用户输入
    ↓
Debounce 500ms
    ↓
写入文件
    ↓
更新保存状态 (已保存/未保存)
```

### 权限需求

| 权限 | 用途 | 申请时机 |
|------|------|---------|
| `READ_EXTERNAL_STORAGE` | 读取文件列表 | 首次打开文件浏览器 |
| `WRITE_EXTERNAL_STORAGE` | 保存编辑内容 | 首次编辑文件 |
| `MANAGE_EXTERNAL_STORAGE` (Android 11+) | 管理所有文件 | 首次打开文件浏览器 |

---

## 依赖库

```kotlin
// Compose BOM
implementation(platform("androidx.compose:compose-bom:2025.01.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui-tooling-preview")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.5")

// Markdown
implementation("org.commonmark:commonmark:0.24.0")

// Hilt DI
implementation("com.google.dagger:hilt-android:2.53.1")
ksp("com.google.dagger:hilt-android-compiler:2.53.1")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
```

---

## 测试策略

| 层级 | 测试内容 | 工具 |
|------|---------|------|
| 单元测试 | MarkdownConverter, FileUtils | JUnit + MockK |
| 集成测试 | DocumentRepository | Android Test + Room |
| UI 测试 | 文件浏览、模式切换 | Compose Test |

---

## 风险与应对

| 风险 | 影响 | 应对 |
|------|------|------|
| Android 11+ 文件权限限制 | 高 | 使用 SAF + `MANAGE_EXTERNAL_STORAGE` |
| 大文件加载性能 | 中 | 分页加载 / 延迟加载 |
| WebView 内存占用 | 中 | 及时释放不用的 WebView |
| 自动保存冲突 | 低 | 文件锁 + 冲突提示 |

---

## 后续扩展方向

1. 搜索功能 (文件内容搜索)
2. 书签/历史记录
3. 主题切换 (暗色模式)
4. 多标签页
5. 云同步

---

*文档版本: v1.0*
*创建日期: 2026-06-01*
