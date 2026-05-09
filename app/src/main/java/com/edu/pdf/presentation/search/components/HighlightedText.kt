package com.edu.pdf.presentation.search.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun HighlightedText(
    text: String,
    query: String,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    // 🌟 remember logic prevents UI stuttering while typing rapidly
    val annotatedString = remember(text, query, primaryColor) {
        val cleanQuery = query.trim()
        if (cleanQuery.isBlank()) return@remember null

        // 1. Break query into independent tokens
        val tokens = cleanQuery.split(Regex("\\s+")).filter { it.isNotEmpty() }

        // 2. O(1) Memory BooleanArray to map which exact letters should be highlighted
        val matchMap = BooleanArray(text.length)

        // 3. Multi-Token Infix Scanner
        for (token in tokens) {
            var startIndex = 0
            while (startIndex < text.length) {
                val foundIndex = text.indexOf(token, startIndex, ignoreCase = true)
                if (foundIndex == -1) break

                // Mark this specific token's letters as 'true' (highlighted)
                for (i in foundIndex until (foundIndex + token.length)) {
                    matchMap[i] = true
                }
                // Jump ahead to avoid overlapping self-matches
                startIndex = foundIndex + token.length
            }
        }

        // Check if ANY highlights were made
        var hasHighlights = false
        for (isHighlighted in matchMap) {
            if (isHighlighted) {
                hasHighlights = true
                break
            }
        }

        if (!hasHighlights) return@remember null

        // 4. Render the string seamlessly
        buildAnnotatedString {
            var i = 0
            while (i < text.length) {
                if (matchMap[i]) {
                    val start = i
                    while (i < text.length && matchMap[i]) { i++ }
                    withStyle(
                        style = SpanStyle(
                            color = primaryColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    ) {
                        append(text.substring(start, i))
                    }
                } else {
                    val start = i
                    while (i < text.length && !matchMap[i]) { i++ }
                    append(text.substring(start, i))
                }
            }
        }
    }

    Text(
        text = annotatedString ?: buildAnnotatedString { append(text) },
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = onBackgroundColor,
        modifier = modifier
    )
}