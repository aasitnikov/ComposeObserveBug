package com.example.composeobservebug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.composeobservebug.ui.theme.ComposeObserveBugTheme

internal class ScreenScrollState(private val lazyListState: LazyListState) {

    val scrollPx: Int by derivedStateOf { lazyListState.firstItemOffset() }
}

internal fun LazyListState.firstItemOffset(): Int {
    if (firstVisibleItemIndex != 0) return 0
    return firstVisibleItemScrollOffset
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lazyListState = rememberLazyListState()
            val scrollState = remember(lazyListState) { ScreenScrollState(lazyListState) }
            ComposeObserveBugTheme {
                LazyColumn(Modifier.fillMaxSize(), lazyListState) {
                    item {
                        AlbumHeader(
                            scrollState,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    items((1..100).toList()) {
                        Surface(
                            color = Color.Gray.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colors.onSurface,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(text = "Item $it", Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun AlbumHeader(
    scrollState: ScreenScrollState,
    modifier: Modifier = Modifier,
) {
//    Surface {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.aspectRatio(0.8f)) {
        Spacer(Modifier.height(36.dp))

        // This fixes observe issue
//        LaunchedEffect(scrollState.scrollPx) {
//            snapshotFlow { scrollState.scrollPx }.collect()
//        }

        Box(
            Modifier
                .offset { IntOffset(0, scrollState.scrollPx) }
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(4.dp)),
            propagateMinConstraints = true,
        ) {
            Box(Modifier.background(Color.Magenta))
        }

        Spacer(Modifier.height(200.dp))
    }
}
