package com.example.studyapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> StudyAppSearchBar(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemClick: (T) -> Unit,
    closeSearchBar: () -> Unit,
    itemLabel: (T) -> String,
    placeholderText: String,
    itemContent: @Composable (T) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var filteredItems by rememberSaveable { mutableStateOf(items) }
    var expanded by rememberSaveable { mutableStateOf(true) }

    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
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
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.close_search),
                        modifier = Modifier.clickable { closeSearchBar() }
                    )
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
        content = {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                items(items = filteredItems) { item ->
                    Box(modifier = Modifier.clickable { onItemClick(item) }) {
                        itemContent(item)
                    }
                }
            }
        }
    )
}
