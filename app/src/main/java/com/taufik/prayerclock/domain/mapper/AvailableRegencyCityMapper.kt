package com.taufik.prayerclock.domain.mapper

import com.taufik.prayerclock.data.data_source.local.db.entity.AvailableRegencyCityEntity
import com.taufik.prayerclock.data.dto.AvailableRegencyCityResponseDto
import com.taufik.prayerclock.domain.model.AvailableRegencyCityModel

fun AvailableRegencyCityResponseDto.toAvailableRegencyCityModel() = AvailableRegencyCityModel(
    id = id.orEmpty(),
    name = name.orEmpty()
)

fun AvailableRegencyCityModel.toAvailableRegencyCityEntity() = AvailableRegencyCityEntity(
    id = id,
    name = name
)