package com.example.bookshelf.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.example.bookshelf.presentation.MainScreenViewModel
import com.example.bookshelf.presentation.OfflineBookShelfViewModel
import com.example.bookshelf.presentation.OnlineBookShelfViewModel
import com.example.bookshelf.presentation.ScannerViewModel
import com.example.bookshelf.ui.components.AppAlertDialog
import com.example.bookshelf.ui.components.BookShelfBottomAppBar
import com.example.bookshelf.ui.components.BookShelfTopAppBar
import com.example.bookshelf.ui.screens.BookScreen
import com.example.bookshelf.ui.screens.HomeScreen
import com.example.bookshelf.ui.screens.SavedBooksScreen
import com.example.bookshelf.ui.screens.ScannerScreen
import com.example.bookshelf.ui.screens.Screen
import com.example.bookshelf.ui.screens.ScreenListProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookShelfApp() {

    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val onlineBookShelfViewModel: OnlineBookShelfViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val offlineBookShelfViewModel: OfflineBookShelfViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val bookPageViewModel: BookPageViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val mainScreenViewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val scannerViewModel: ScannerViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val mainScreenUiState by mainScreenViewModel.mainScreenUiState.collectAsState()
    val selectedBookState by bookPageViewModel.selectedBookState.collectAsState()

    var searchInput: String by rememberSaveable {
        mutableStateOf("")
    }

    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val scrollState = rememberScrollState()
    var isAtTop by remember { mutableStateOf(true) }

    LaunchedEffect(scrollState.canScrollBackward) {
        isAtTop = !scrollState.canScrollBackward
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            BookShelfTopAppBar(
                scrollBehavior = topAppBarScrollBehavior,
                title = mainScreenUiState.selectedScreen.title,
                enableNavigateBack = mainScreenViewModel.canNavigateBack(backStackEntry?.destination?.route),
                onNavigateBack = {
                    handleBackPressed(mainScreenViewModel, navController)
                },
                selectedBook = selectedBookState.book,
                mainScreenUiState = mainScreenUiState,
                searchInput = searchInput,
                onInputChange = {
                    searchInput = it
                    onlineBookShelfViewModel.isFirstTry = false
                    onlineBookShelfViewModel.getBooksFromNetwork(searchInput)
                },
                onInputClear = {
                    searchInput = ""
                    onlineBookShelfViewModel.getBooksFromNetwork(searchInput)
                }
            )
        },
        bottomBar = {
            BookShelfBottomAppBar(
                selectedScreen = mainScreenUiState.selectedScreen,
                onScreenSelect = {
                    navController.popBackStack(it.route, false)
                    navController.navigate(it.route)
                    mainScreenViewModel.changeSelectedScreen(it)
                }
            )
        },
        floatingActionButton = {
            val route = backStackEntry?.destination?.route
            AnimatedVisibility (
                visible = route == Screen.BookScreen.route && selectedBookState.book != null,
                enter = fadeIn() + slideInHorizontally { fullWidth -> fullWidth / 2 },
                exit = fadeOut() + slideOutHorizontally { fullWidth -> fullWidth / 2 }
            ) {
                val context = LocalContext.current
                FloatingActionButton(
                    onClick = {
                        if (selectedBookState.isSaved) {
                            selectedBookState.book?.let { bookPageViewModel.deleteBook(it) }
                            scope.launch {
                                showSnackBar(
                                    snackbarHostState = snackbarHostState,
                                    message = context.getString(R.string.book_deleted),
                                    showAction = false,
                                    context = context
                                )
                            }
                        } else {
                            selectedBookState.book?.let { bookPageViewModel.saveBook(it) }
                            scope.launch {
                                showSnackBar(
                                    snackbarHostState = snackbarHostState,
                                    message = context.getString(R.string.book_saved),
                                    showAction = true,
                                    context = context,
                                    navController = navController,
                                    mainScreenViewModel = mainScreenViewModel
                                )
                            }
                        }
                    },
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Row (
                        modifier = Modifier.padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedBookState.isSaved) {
                            Icon(
                                imageVector = Icons.TwoTone.Delete,
                                contentDescription = ""
                            )
                            AnimatedVisibility (visible = isAtTop) {
                                Text(text = stringResource(R.string.delete_book), modifier = Modifier.padding(start = 10.dp))
                            }
                        } else {
                            Icon(
                                imageVector = Icons.TwoTone.Star,
                                contentDescription = ""
                            )
                            AnimatedVisibility (visible = isAtTop) {
                                Text(text = stringResource(R.string.save_book), modifier = Modifier.padding(start = 10.dp))
                            }
                        }
                    }
                }
            }
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
                    HomeScreen(
                        viewModel = onlineBookShelfViewModel,
                        retryAction = { onlineBookShelfViewModel.getBooksFromNetwork(searchInput) },
                        onBookClick = {
                            onBookClick(needsNetwork = true, navController, bookPageViewModel, mainScreenViewModel, scope, scrollState, bookId = it)
                        },
                        onBackClick = {
                            isDialogOpen = !isDialogOpen
                        }
                    )
                }
                composable(route = Screen.SavedScreen.route) {
                    SavedBooksScreen(
                        offlineViewModel = offlineBookShelfViewModel,
                        onBookClick = {
                            onBookClick(needsNetwork = false, navController, bookPageViewModel, mainScreenViewModel, scope, scrollState, bookId = it)
                        },
                        onBackPressed = {
                            mainScreenViewModel.changeSelectedScreen(Screen.HomeScreen)
                            navController.popBackStack(Screen.HomeScreen.route, false)
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
                        scrollState = scrollState,
                        retryAction = { bookPageViewModel.getBookDetailsFromNetwork(bookId) },
                        onBackPressed = {
                            handleBackPressed(mainScreenViewModel, navController)
                        }
                    )
                }
                composable(route = Screen.ScannerScreen.route) {
                    ScannerScreen(
                        viewModel = scannerViewModel,
                        onBackPressed = {
                            mainScreenViewModel.changeSelectedScreen(Screen.HomeScreen)
                            navController.popBackStack(Screen.HomeScreen.route, false)
                        }
                    )
                }
            }
        }
    }
    if(isDialogOpen) {
        val activity = LocalContext.current as Activity
        AppAlertDialog(
            title = stringResource(R.string.close_the_app),
            text = stringResource(R.string.do_you_want_to_exit_the_app),
            onConfirm = {
                activity.finish()
            },
            onDismiss = {
                isDialogOpen = false
            }
        )
    }
}

private fun handleBackPressed(
    mainScreenViewModel: MainScreenViewModel,
    navController: NavHostController
) {
    val route = navController.previousBackStackEntry?.destination?.route
    val screen = ScreenListProvider.allScreens.find { it.route == route }
    navController.navigateUp()
    screen?.let { mainScreenViewModel.changeSelectedScreen(screen) }
}

private fun onBookClick(
    needsNetwork: Boolean,
    navController: NavHostController,
    bookPageViewModel: BookPageViewModel,
    mainScreenViewModel: MainScreenViewModel,
    scope: CoroutineScope,
    scrollState: ScrollState,
    bookId: String
) {
    navController.navigate(Screen.BookScreen.passBookId(bookId))
    if (needsNetwork) {
        bookPageViewModel.getBookDetailsFromNetwork(bookId)
    } else {
        bookPageViewModel.getBookDetailsFromStorage(bookId)
    }
    mainScreenViewModel.changeSelectedScreen(Screen.BookScreen)
    scope.launch {
        scrollState.scrollTo(0)
    }
}

private suspend fun showSnackBar(
    snackbarHostState: SnackbarHostState,
    message: String,
    showAction: Boolean,
    context: Context,
    navController: NavHostController? = null,
    mainScreenViewModel: MainScreenViewModel? = null
) {
    val result = snackbarHostState
        .showSnackbar(
            message = message,
            actionLabel = if (showAction) context.getString(R.string.view) else "",
            duration = SnackbarDuration.Short
        )
    when (result) {
        SnackbarResult.ActionPerformed -> {
            if (showAction) {
                mainScreenViewModel?.changeSelectedScreen(Screen.SavedScreen)
                navController?.navigate(Screen.SavedScreen.route)
            }
        }
        SnackbarResult.Dismissed -> {}
    }
}