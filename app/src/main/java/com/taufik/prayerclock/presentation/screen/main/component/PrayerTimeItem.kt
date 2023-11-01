package com.taufik.prayerclock.presentation.screen.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.taufik.prayerclock.R
import com.taufik.prayerclock.domain.model.PrayerTime
import com.taufik.prayerclock.presentation.theme.PrayerClockTheme

@Composable
fun PrayerTimeItem(
    prayerTimePair: Pair<PrayerTime, String>,
    onClickChangeColor: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = prayerTimePair.first.toString())
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = prayerTimePair.second)
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { onClickChangeColor() }
            ) {
                Icon(
                    painterResource(id = R.drawable.color_palette),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Preview
@Composable
fun PrayerTimeItemPreview() {
    PrayerClockTheme {
        PrayerTimeItem(prayerTimePair = Pair(PrayerTime.SUBUH, "04:38"), onClickChangeColor = {})
    }
}