import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavTopBar(
    colors: TopAppBarColors,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    if (canNavigateBack) {
        TopAppBar(
            colors = colors,
            title = title,
            actions = { actions() },
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            scrollBehavior = scrollBehavior,
            modifier = modifier
        )
    } else {
        TopAppBar(
            colors = colors,
            title = title,
            scrollBehavior = scrollBehavior,
            actions = { actions() },
            modifier = modifier
        )
    }
}