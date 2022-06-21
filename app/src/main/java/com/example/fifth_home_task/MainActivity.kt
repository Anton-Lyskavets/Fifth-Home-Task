package com.example.fifth_home_task

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fifth_home_task.databinding.ActivityMainBinding
import com.example.fifth_home_task.model.Bank
import com.example.fifth_home_task.model.BankATM
import com.example.fifth_home_task.network.BankApi.retrofitService
import com.example.fifth_home_task.model.BankFilial
import com.example.fifth_home_task.model.BankInfobox
import com.example.fifth_home_task.network.BankRenderer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var circle: Circle? = null
    private val defaultMarker = LatLng(52.425163, 31.015039)

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment
        val bounds = LatLngBounds.builder()
        val listDistance = mutableListOf<Double>()
        var sortedListDistance: List<Double>
        val needListMarker = mutableListOf<Bank>()
        Observable.zip(retrofitService.getATM(),
            retrofitService.getInfobox(),
            retrofitService.getFilial(),
            Function3<List<BankATM>, List<BankInfobox>, List<BankFilial>, List<Bank>> { type1, type2, type3 ->
                val listBank = mutableListOf<Bank>()
                val list1: List<Bank> = type1
                val list2: List<Bank> = type2
                val list3: List<Bank> = type3
                for (i in list1) {
                    listBank.add(i)
                }
                for (i in list2) {
                    listBank.add(i)
                }
                for (i in list3) {
                    listBank.add(i)
                }
                listBank
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mapFragment?.getMapAsync { googleMap ->
                    googleMap.setOnMapLoadedCallback {
                        it.forEach {
                            listDistance.add(distanceTwoPoints(defaultMarker, it.position))
                        }
                        sortedListDistance = listDistance.sorted()
                        it.forEach {
                            for (i in 0..9) {
                                if (distanceTwoPoints(defaultMarker,
                                        it.position) == sortedListDistance[i]
                                ) {
//                                    val marker = googleMap.addMarker(
//                                        MarkerOptions()
//                                            .title(it.title)
//                                            .snippet(it.snippet)
//                                            .position(it.position)
//                                    )
                                    needListMarker.add(it)
                                    bounds.include(it.position)
                                }
                            }
                        }
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds.build(),
                                20
                            )
                        )
                        addClusteredMarkers(googleMap, needListMarker)
                    }
                }
            }, { error ->
                error.printStackTrace()
            })
    }

    private fun distanceTwoPoints(pointOne: LatLng, pointTwo: LatLng): Double {
        return sqrt(
            (pointTwo.latitude - pointOne.latitude).pow(2)
                    + (pointTwo.longitude - pointOne.longitude).pow(2)
        )
    }

    private fun addClusteredMarkers(googleMap: GoogleMap, listObj: List<Bank>) {
        val clusterManager = ClusterManager<Bank>(this, googleMap)
        clusterManager.renderer =
            BankRenderer(
                this,
                googleMap,
                clusterManager
            )
        clusterManager.addItems(listObj)
        clusterManager.cluster()
        clusterManager.setOnClusterItemClickListener { item: Bank ->
            addCircle(googleMap, item)
            return@setOnClusterItemClickListener false
        }
        googleMap.setOnCameraMoveStartedListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
        }

        googleMap.setOnCameraIdleListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.onCameraIdle()
        }
    }

    private fun addCircle(googleMap: GoogleMap, item: Bank) {
        circle?.remove()
        circle = googleMap.addCircle(
            CircleOptions()
                .center(LatLng(item.gpsX.toDouble(), item.gpsY.toDouble()))
                .radius(100.0)
                .fillColor(ContextCompat.getColor(this, R.color.green_200))
                .strokeColor(ContextCompat.getColor(this, R.color.red_600))
        )
    }
}