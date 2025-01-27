package com.example.bookshelf.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookshelf.R
import com.example.bookshelf.presentation.AppViewModelProvider
import com.example.bookshelf.presentation.BookPageViewModel
import com.example.bookshelf.presentation.BookShelfViewModel
import com.example.bookshelf.presentation.MainScreenViewModel
import com.example.bookshelf.ui.components.BookShelfBottomAppBar
import com.example.bookshelf.ui.components.BookShelfTopAppBar
import com.example.bookshelf.ui.screens.BookScreen
import com.example.bookshelf.ui.screens.HomeScreen
import com.example.bookshelf.ui.screens.Page
import com.example.bookshelf.ui.screens.Pages
import com.example.bookshelf.ui.screens.SavedBooksScreen
import com.example.bookshelf.ui.screens.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookShelfApp() {

    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val bottomAppBarScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    val bookShelfViewModel: BookShelfViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val bookPageViewModel: BookPageViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val mainScreenViewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val mainScreenUiState by mainScreenViewModel.mainScreenUiState.collectAsState()

    var searchInput: String by rememberSaveable {
        mutableStateOf("")
    }

    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            BookShelfTopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = mainScreenUiState.selectedPage.name.replaceFirstChar { it.uppercaseChar() },
                enableNavigateBack = mainScreenViewModel.canNavigateBack(backStackEntry?.destination?.route),
                onNavigateBack = {
                    navController.navigateUp()
                    mainScreenViewModel.changeSelectedPage(Pages.pageList[0])
                },
                enableSearch = mainScreenUiState.isSearchEnabled,
                searchInput = searchInput,
                onInputChange = {
                    searchInput = it
                    bookShelfViewModel.getBooksFromNetwork(searchInput)
                },
                onInputClear = {
                    searchInput = ""
                }
            )
        },
        bottomBar = {
            BookShelfBottomAppBar(
                scrollBehavior = bottomAppBarScrollBehavior,
                selectedPage = mainScreenUiState.selectedPage,
                onPageSelect = {
                    navController.navigate(it.name)
                    mainScreenViewModel.changeSelectedPage(it)
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.route
            ) {
                composable(Screen.HomeScreen.route) {
                    val title = stringResource(R.string.book_page_title)
                    HomeScreen(
                        bookShelfUiState = bookShelfViewModel.bookShelfUiState,
                        retryAction = { bookShelfViewModel.getBooksFromNetwork(searchInput) },
                        onBookClick = {
                            navController.navigate(Screen.BookScreen.passBookId(it))
                            bookPageViewModel.getBookDetailsFromNetwork(it)
                            mainScreenViewModel.changeSelectedPage(Page(name = title))
                        }
                    )
                }
                composable(route = Screen.SavedScreen.route) {
                    SavedBooksScreen(
                        onBackPressed = {
                            handleBackPressed(mainScreenViewModel, navController)
                        }
                    )
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
                    BookScreen(
                        bookPageUiState = bookPageViewModel.bookPageUiState,
                        retryAction = { bookPageViewModel.getBookDetailsFromNetwork(bookId) },
                        onBackPressed = {
                            handleBackPressed(mainScreenViewModel, navController)
                        }
                    )
                }
            }
        }
    }
}

private fun handleBackPressed(mainScreenViewModel: MainScreenViewModel, navController: NavHostController) {
    mainScreenViewModel.changeSelectedPage(Pages.pageList[0])
    navController.popBackStack(Screen.HomeScreen.route, false)
}