package com.example.fifth_home_task.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

open class Bank : ClusterItem {
    open val installPlace: String = ""
    open val addressType: String = ""
    open val address: String = ""
    open val house: String = ""
    open val gpsX: String = ""
    open val gpsY: String = ""
    open val workTime: String = ""

    override fun getPosition(): LatLng =
        LatLng(gpsX.toDouble(), gpsY.toDouble())

    override fun getTitle(): String =
        installPlace

    override fun getSnippet(): String =
        "$addressType $address, $house."
}