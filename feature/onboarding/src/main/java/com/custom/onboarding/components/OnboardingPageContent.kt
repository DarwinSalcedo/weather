package com.custom.onboarding.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.custom.common.components.DefaultBodyContent
import com.custom.onboarding.ui.OnboardingData

@Composable
fun OnboardingPageContent(data: OnboardingData) {
    DefaultBodyContent(
        data.icon,
        stringResource(data.iconContentDescriptionResId),
        stringResource(data.titleResId),
        stringResource(data.descriptionResId)
    )
}
