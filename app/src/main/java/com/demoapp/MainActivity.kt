package com.demoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import com.google.accompanist.pager.ExperimentalPagerApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch

/**
 * Main entry point for the app.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageCarouselApp()
        }
    }
}

/**
 * Composable function that creates the main app layout with image carousel, search functionality,
 * and a bottom sheet.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun ImageCarouselApp() {
    val images = listOf(
        R.drawable.scene, R.drawable.scene, R.drawable.scene, R.drawable.scene, R.drawable.scene
    )

    val pagerState = rememberPagerState()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var currentPage by remember { mutableStateOf(0) }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    // Update current page when the pager state changes
    LaunchedEffect(pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }

    // List of items based on the current page
    val allItems = when (currentPage) {
        0 -> listOf("apple", "banana", "orange", "plum", "orange", "watermelon")
        1 -> listOf("grapes", "pineapple")
        2 -> listOf("black mango", "papaya", "abc")
        3 -> listOf("oran", "pineapple")
        else -> listOf("watermelon", "peach", "plum")
    }

    // Filter items based on search query
    val filteredItems = allItems.filter { it.contains(searchQuery.text, ignoreCase = true) }
    val topCharacters = calculateTopCharacters(allItems)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { BottomSheetContent(allItems, topCharacters) }
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    scope.launch { sheetState.show() }
                }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Show Stats")
                }
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF5FBF8))) {
                item {
                    ImageCarousel(images = images, pagerState = pagerState)
                    Spacer(modifier = Modifier.height(16.dp))
                    PagerIndicator(pagerState = pagerState)
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchBox(query = searchQuery, onQueryChanged = { searchQuery = it })
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(filteredItems) { item ->
                    ListItem(item = item)
                }
            }
        }
    }
}

/**
 * Composable function to display the image carousel.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(images: List<Int>, pagerState: PagerState) {
    HorizontalPager(
        count = images.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(25.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .clip(RoundedCornerShape(25.dp))
        ) {
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Carousel Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
            )
        }
    }
}

/**
 * Composable function for the pager indicator that shows the current page.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerIndicator(pagerState: PagerState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(10.dp)
                    .background(
                        color = if (pagerState.currentPage == index) Color(0xFF2196F3) else Color.Gray,
                        shape = CircleShape
                    )
            )
        }
    }
}

/**
 * Composable function for the search box.
 */
@Composable
fun SearchBox(query: TextFieldValue, onQueryChanged: (TextFieldValue) -> Unit) {
    var offset by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .offset { IntOffset(0, offset) }
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            label = { Text("Search") },
            placeholder = { Text("Search items") },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon")
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
        )
    }

    LaunchedEffect(scrollState.value) {
        offset = scrollState.value
    }
}

/**
 * Composable function to display each list item.
 */
@Composable
fun ListItem(item: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = 14.dp,
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0xFFCDE8E1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.flower),
                contentDescription = "Item Image",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = item, style = MaterialTheme.typography.h6, color = Color.Black)
                Text(text = "Description of $item", style = MaterialTheme.typography.body2, color = Color.Black)
            }
        }
    }
}

/**
 * Composable function for the bottom sheet content.
 */
@Composable
fun BottomSheetContent(items: List<String>, topCharacters: List<Pair<Char, Int>>) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        val totalCharacters = items.sumOf { it.length }

        Text(text = "Item Count: $totalCharacters", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Top 3 Characters Frequency:", style = MaterialTheme.typography.h6)

        topCharacters.forEach { (char, count) ->
            Text(text = "$char = $count")
        }
    }
}

/**
 * Function to calculate the top 3 most frequent characters in the list of items.
 */
fun calculateTopCharacters(items: List<String>): List<Pair<Char, Int>> {
    val characterCounts = mutableMapOf<Char, Int>()
    items.forEach { item ->
        item.forEach { char ->
            if (char.isLetter()) {
                characterCounts[char] = characterCounts.getOrDefault(char, 0) + 1
            }
        }
    }

    return characterCounts.entries
        .sortedByDescending { it.value }
        .take(3)
        .map { it.toPair() }
}

/**
 * Preview of the app for UI testing.
 */
@Composable
@Preview(showBackground = true)
fun PreviewImageCarouselApp() {
    ImageCarouselApp()
}
