package com.elephenman.docviewer.util

import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

object MarkdownConverter {

    private val parser: Parser = Parser.builder().build()
    private val renderer: HtmlRenderer = HtmlRenderer.builder().build()

    fun toHtml(markdown: String): String {
        val document: Node = parser.parse(markdown)
        val bodyHtml = renderer.render(document)
        return wrapHtml(bodyHtml)
    }

    private fun wrapHtml(body: String): String {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; padding: 16px; line-height: 1.6; color: #333; }
                h1, h2, h3, h4, h5, h6 { color: #222; margin-top: 24px; margin-bottom: 16px; }
                code { background: #f5f5f5; padding: 2px 6px; border-radius: 3px; font-family: monospace; }
                pre { background: #f5f5f5; padding: 12px; border-radius: 6px; overflow-x: auto; }
                pre code { background: none; padding: 0; }
                blockquote { border-left: 4px solid #ddd; margin: 0; padding-left: 16px; color: #666; }
                a { color: #0366d6; }
                img { max-width: 100%; height: auto; }
                table { border-collapse: collapse; width: 100%; }
                th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                th { background: #f5f5f5; }
            </style>
        </head>
        <body>
            $body
        </body>
        </html>
        """.trimIndent()
    }
}
