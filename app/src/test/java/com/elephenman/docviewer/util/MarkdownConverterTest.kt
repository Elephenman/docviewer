package com.elephenman.docviewer.util

import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownConverterTest {

    @Test
    fun `toHtml converts markdown to html`() {
        val markdown = "# Hello\n\nThis is **bold** and *italic*."
        val html = MarkdownConverter.toHtml(markdown)

        assertTrue(html.contains("<h1>Hello</h1>"))
        assertTrue(html.contains("<strong>bold</strong>"))
        assertTrue(html.contains("<em>italic</em>"))
        assertTrue(html.contains("<!DOCTYPE html>"))
    }

    @Test
    fun `toHtml wraps with proper html structure`() {
        val markdown = "test"
        val html = MarkdownConverter.toHtml(markdown)

        assertTrue(html.startsWith("<!DOCTYPE html>"))
        assertTrue(html.contains("<html>"))
        assertTrue(html.contains("<body>"))
        assertTrue(html.contains("</body>"))
        assertTrue(html.contains("</html>"))
    }
}
