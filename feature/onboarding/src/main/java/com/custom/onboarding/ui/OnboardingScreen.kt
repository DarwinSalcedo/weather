package com.custom.onboarding.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.custom.onboarding.components.BottomBar
import com.custom.onboarding.components.OnboardingPageContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onOnboardingComplete: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val isLastPage by remember {
        derivedStateOf { pagerState.currentPage == onboardingPages.size - 1 }
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomBar(
                pagerState = pagerState,
                isLastPage = isLastPage,
                onNextClicked = {
                    if (isLastPage) {
                        viewModel.completeOnboarding(onOnboardingComplete)
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues)
        ) { pageIndex ->
            OnboardingPageContent(data = onboardingPages[pageIndex])
        }
    }
}