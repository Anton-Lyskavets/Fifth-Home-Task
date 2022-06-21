package com.example.fifth_home_task.network

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.fifth_home_task.BitmapHelper
import com.example.fifth_home_task.R
import com.example.fifth_home_task.model.Bank
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class BankRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<Bank>
) : DefaultClusterRenderer<Bank>(context, map, clusterManager) {
    private val bankIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(
            context,
            R.color.red_600
        )
        BitmapHelper.vectorToBitmap(
            context,
            R.drawable.baseline_close_36,
            color
        )
    }

    override fun onBeforeClusterItemRendered(
        item: Bank,
        markerOptions: MarkerOptions
    ) {
        markerOptions.title(item.installPlace)
            .position(LatLng(item.gpsX.toDouble(), item.gpsY.toDouble()))
            .icon(bankIcon)
    }

    override fun onClusterItemRendered(clusterItem: Bank, marker: Marker) {
        marker.tag = clusterItem
    }
}