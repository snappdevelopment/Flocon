package io.github.openflocon.library.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sebastianneubauer.jsontree.diff.JsonTreeDiff
import com.sebastianneubauer.jsontree.diff.JsonTreeDiffError
import com.sebastianneubauer.jsontree.diff.defaultDarkDiffColors
import io.github.openflocon.library.designsystem.FloconTheme

@Composable
fun FloconJsonTreeDiff(
    originalJson: String,
    revisedJson: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(8.dp),
    showInlineDiffs: Boolean = false,
    textStyle: TextStyle = FloconTheme.typography.bodyMedium,
    onError: (JsonTreeDiffError) -> Unit = {},
) {
    SelectionContainer(modifier = modifier) {
        JsonTreeDiff(
            modifier = modifier,
            originalJson = originalJson,
            revisedJson = revisedJson,
            contentPadding = paddingValues,
            showInlineDiffs = showInlineDiffs,
            colors = defaultDarkDiffColors,
            textStyle = textStyle,
            onError = onError,
            onLoading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    FloconCircularProgressIndicator()
                }
            }
        )
    }
}
