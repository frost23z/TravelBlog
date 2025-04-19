package me.zayedbinhasan.travelblog.ui.screen.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import me.zayedbinhasan.travelblog.data.remote.BASE_URL
import me.zayedbinhasan.travelblog.data.remote.PATH

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    state: ListState,
    onIntent: (ListIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    if (state.showSearchBar) {
                        TextField(
                            value = state.searchQuery,
                            onValueChange = { onIntent(ListIntent.Search(it)) },
                            placeholder = { Text("Search blogs...") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Travel Blog")
                    }
                },
                actions = {
                    IconButton(onClick = { onIntent(ListIntent.ToggleSearchBarVisibility) }) {
                        Icon(
                            imageVector = if (state.showSearchBar) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = if (state.showSearchBar) "Close search" else "Search"
                        )
                    }
                    IconButton(onClick = { onIntent(ListIntent.ToggleSortDialog) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Sort"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onIntent(ListIntent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${state.errorMessage}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        items = state.blogs,
                        key = { it.id }
                    ) { blog ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = blog.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = blog.date,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            trailingContent = {
                                AsyncImage(
                                    model = BASE_URL + PATH + blog.author.avatar,
                                    contentDescription = "Author Avatar",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .clickable { onIntent(ListIntent.BlogClicked(blog.id)) }
                        )
                    }
                }
            }
        }

        if (state.showSortDialog) {
            AlertDialog(
                onDismissRequest = { onIntent(ListIntent.ToggleSortDialog) },
                title = { Text("Sort") },
                text = {
                    LazyColumn {
                        items(Sort.entries.toTypedArray()) { sort ->
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = sort.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (state.sort == sort) {
                                            onIntent(ListIntent.SwitchSortOrder)
                                        } else {
                                            onIntent(ListIntent.SortBlogs(sort))
                                        }
                                    }
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { onIntent(ListIntent.ToggleSortDialog) }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}