package io.github.openflocon.flocondesktop.features.network.body

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sebastianneubauer.jsontree.diff.JsonTreeDiff
import com.sebastianneubauer.jsontree.diff.JsonTreeDiffError
import com.sebastianneubauer.jsontree.diff.defaultDarkDiffColors
import io.github.openflocon.flocondesktop.features.network.body.model.NetworkDiffUi
import io.github.openflocon.flocondesktop.features.network.body.model.previewNetworkDiffUi
import io.github.openflocon.library.designsystem.FloconTheme
import io.github.openflocon.library.designsystem.components.FloconCircularProgressIndicator
import io.github.openflocon.library.designsystem.components.FloconSurface
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NetworkDiffWindow(
    diff: NetworkDiffUi
) {
    NetworkDiffContent(
        diff = diff,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun NetworkDiffContent(
    diff: NetworkDiffUi,
    modifier: Modifier = Modifier,
) {
    var diffError by remember(diff) { mutableStateOf<JsonTreeDiffError?>(null) }

    FloconSurface(
        modifier = modifier,
    ) {
        val error = diffError
        if(error != null) {
            when(error) {
                is JsonTreeDiffError.OriginalJsonError -> {
                    Text(text = "Error in original Json: ${error.throwable.localizedMessage}")
                }
                is JsonTreeDiffError.RevisedJsonError -> {
                    Text(text = "Error in clipboard Json: ${error.throwable.localizedMessage}")
                }
            }
        } else {
            JsonTreeDiff(
                modifier = Modifier.fillMaxSize(),
                originalJson = diff.json,
                revisedJson = diff.clipboardJson,
                colors = defaultDarkDiffColors,
                showInlineDiffs = true,
                onLoading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        FloconCircularProgressIndicator()
                    }
                },
                onError = { diffError = it }
            )
        }
    }
}

@Preview
@Composable
private fun NetworkDiffContentPreview() {
    FloconTheme {
        NetworkDiffContent(
            diff = previewNetworkDiffUi()
        )
    }
}
