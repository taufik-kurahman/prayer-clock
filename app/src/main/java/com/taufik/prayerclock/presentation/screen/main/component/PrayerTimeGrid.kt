package com.taufik.prayerclock.presentation.screen.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.taufik.prayerclock.domain.model.PrayerTime

@Composable
fun PrayerTimeGrid(
    modifier: Modifier,
    columnsCount: Int,
    contentPadding: Dp = 16.dp,
    verticalArrangementSpace: Dp = 16.dp,
    horizontalArrangementSpace: Dp = 16.dp,
    prayerTimes: List<Pair<PrayerTime, String>>,
    onClickChangeColor: (PrayerTime) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(columnsCount),
        contentPadding = PaddingValues(contentPadding),
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpace),
        horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpace)
    ) {
        items(prayerTimes) { prayerTime ->
            PrayerTimeItem(prayerTimePair = prayerTime, onClickChangeColor = { onClickChangeColor(prayerTime.first) })
        }
    }
}