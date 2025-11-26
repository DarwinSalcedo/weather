package com.custom.home.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.custom.common.components.DefaultBodyContent
import com.custom.home.R


@Composable
fun LocationPermissionScreen(
    onPermissionGranted: () -> Unit,
    onPermissionSkipped: () -> Unit,
) {
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionSkipped()
        }
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    },
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(stringResource(R.string.allow_location))
                }


                Text(
                    text = stringResource(R.string.continue_without_location),
                    modifier = Modifier.clickable { onPermissionSkipped() },
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

            }
        }
    ) { paddingValues ->
        Spacer(Modifier.height(paddingValues.calculateTopPadding()))
        DefaultBodyContent(
            Icons.Default.AddLocationAlt,
            stringResource(R.string.location_authorize),
            stringResource(R.string.location_permission_is_required),
            stringResource(R.string.to_get_the_local_weather_we_need_to_access_your_location)
        )
    }
}