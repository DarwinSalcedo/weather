package com.custom.language.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.custom.language.R
import com.custom.language.components.LanguageListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionScreen(
    viewModel: LanguageViewModel = hiltViewModel(),
    onLanguageSelected: () -> Unit
) {
    val selectedCode by viewModel.selectedLanguageCode.collectAsState()
    val availableLanguages by viewModel.availableLanguages.collectAsState()
    val isSelectionValid = selectedCode != null

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,


                ) {
                Button(
                    enabled = isSelectionValid,
                    onClick = { viewModel.saveLanguageAndComplete(onLanguageSelected) },
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    AnimatedContent(
                        targetState = true,
                        transitionSpec = {
                            (slideInHorizontally { it } + fadeIn()).togetherWith(
                                slideOutHorizontally { -it } + fadeOut())
                        }, label = "NextButtonTransition"
                    ) { targetIsLastPage ->
                        println(targetIsLastPage)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(R.string.next), fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                Icons.Default.Done,
                                contentDescription = stringResource(R.string.next)
                            )
                        }

                    }
                }

            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = stringResource(R.string.select_the_language_of_the_app),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(availableLanguages) { locale ->
                    LanguageListItem(
                        name = locale.name,
                        isSelected = locale.code == selectedCode,
                        onClick = { viewModel.selectLanguage(locale.code) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
