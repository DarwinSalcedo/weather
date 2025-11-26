package com.custom.onboarding.ui

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class OnboardingData(
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
    val icon: ImageVector,
    @StringRes val iconContentDescriptionResId: Int
)

