package com.custom.onboarding.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import com.custom.onboarding.R

val onboardingPages = listOf(
    OnboardingData(
        titleResId = R.string.onboarding_title_1,
        descriptionResId = R.string.onboarding_description_1,
        icon = Icons.Default.Info,
        iconContentDescriptionResId = R.string.onboarding_icon_info_desc
    ),
    OnboardingData(
        titleResId = R.string.onboarding_title_2,
        descriptionResId = R.string.onboarding_description_2,
        icon = Icons.Default.Search,
        iconContentDescriptionResId = R.string.onboarding_icon_search_desc
    )
)