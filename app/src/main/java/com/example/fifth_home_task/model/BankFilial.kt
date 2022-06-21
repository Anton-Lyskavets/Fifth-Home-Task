package com.example.fifth_home_task.model

import com.squareup.moshi.Json

data class BankFilial(
    @Json(name = "filial_name") override val installPlace: String,
    @Json(name = "street_type") override val addressType: String,
    @Json(name = "street") override val address: String,
    @Json(name = "home_number") override val house: String,
    @Json(name = "GPS_X") override val gpsX: String,
    @Json(name = "GPS_Y") override val gpsY: String,
    @Json(name = "info_worktime") override val workTime: String,
) : Bank() {
    override fun getTitle(): String {
        return "Filial $installPlace"
    }
}