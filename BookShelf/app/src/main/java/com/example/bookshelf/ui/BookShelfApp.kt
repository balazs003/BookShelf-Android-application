package com.example.bookshelf.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookshelf.R
import com.example.bookshelf.presentation.BookShelfViewModel
import com.example.bookshelf.ui.components.BookPageTopAppBar
import com.example.bookshelf.ui.components.BookShelfBottomAppBar
import com.example.bookshelf.ui.components.BookShelfTopAppBar
import com.example.bookshelf.ui.screens.BookScreen
import com.example.bookshelf.ui.screens.HomeScreen
import com.example.bookshelf.ui.screens.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookShelfApp() {
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val bottomAppBarScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    val bookShelfViewModel: BookShelfViewModel = viewModel(factory = BookShelfViewModel.Factory)

    var searchInput: String by remember {
        mutableStateOf("")
    }

    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            Scaffold(
                modifier = Modifier
                    .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                    .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
                topBar = {
                    BookShelfTopAppBar(
                        scrollBehavior = topAppBarScrollBehavior
                    )
                },
                bottomBar = {
                    BookShelfBottomAppBar(
                        searchInput = searchInput,
                        scrollBehavior = bottomAppBarScrollBehavior,
                        onInputChange = {
                            searchInput = it
                            bookShelfViewModel.getBooks(searchInput)
                        },
                        onInputClear = {
                            searchInput = ""
                        }
                    )
                }
            ) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    HomeScreen(
                        bookShelfUiState = bookShelfViewModel.bookShelfUiState,
                        retryAction = { bookShelfViewModel.getBooks(searchInput) },
                        onBookClick = {
                            navController.navigate(Screen.BookScreen.passBookId(it))
                            bookShelfViewModel.getBookDetails(it)
                        }
                    )
                }
            }
        }
        composable(
            route = Screen.BookScreen.route,
            arguments = listOf(
                navArgument(Screen.BookScreen.Args.id) {
                    type = NavType.StringType
                    defaultValue = "1"
                }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString(Screen.BookScreen.Args.id)!!
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    BookPageTopAppBar(navController = navController)
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    BookScreen(
                        bookPageUiState = bookShelfViewModel.bookPageUiState,
                        retryAction = { bookShelfViewModel.getBookDetails(bookId) }
                    )
                }
            }
        }
    }
}

