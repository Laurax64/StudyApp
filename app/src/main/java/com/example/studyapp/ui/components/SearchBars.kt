package com.example.studyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    modifier: Modifier = Modifier,
    openSearchView: () -> Unit,
    placeholderText: String,
) {
    val query by rememberSaveable { mutableStateOf("") }
    Row(
        modifier = modifier
            .windowInsetsPadding(insets = TopAppBarDefaults.windowInsets)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.close_search),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(90.dp))
                .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(90.dp)
                )
                .size(size = 48.dp)
        )

        SearchBarDefaults.InputField(
            query = query,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(0.5f)
                .wrapContentWidth()
                .height(64.dp)
                .clickable { openSearchView() },
            onQueryChange = { openSearchView() },
            onSearch = { openSearchView() },
            expanded = false,
            enabled = false,
            onExpandedChange = {},
            placeholder = {
                Text(
                    text = placeholderText,
                    modifier = Modifier.padding(start = 12.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        Icon(
            painter = painterResource(R.drawable.baseline_account_circle_24),
            contentDescription = stringResource(R.string.open_login),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clickable {/*TODO add login functionality and avatar display. */ }
                .size(size = 48.dp)

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DockedSearchBar(
    modifier: Modifier = Modifier,
    items: List<T>,
    closeSearchBar: () -> Unit,
    itemLabel: (T) -> String,
    placeholderText: String,
    content: @Composable (List<T>) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var filteredItems by rememberSaveable { mutableStateOf(items) }
    var expanded by rememberSaveable { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Request focus and show keyboard when composable enters composition
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
                onQueryChange = {
                    query = it
                    filteredItems = items.filter { item ->
                        itemLabel(item).contains(it, ignoreCase = true)
                    }
                },
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text(text = placeholderText) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.close_search),
                        modifier = Modifier.clickable {
                            closeSearchBar()
                            focusManager.clearFocus()
                        }
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_close_24),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentDescription = stringResource(R.string.close_search),
                            modifier = Modifier.clickable {
                                query = ""
                                filteredItems = items
                            }
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        },
        modifier = modifier,
        expanded = true,
        onExpandedChange = { expanded = it },
        colors = SearchBarDefaults.colors(containerColor = Color.Transparent),
        content = { content(filteredItems) }
    )
}

@Preview
@Composable
private fun SearchAppBarPreview() {
    SearchAppBar(
        openSearchView = {},
        placeholderText = "Search",
    )
}