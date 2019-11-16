package ru.sovcombank.iotmerahackathon

import android.os.Bundle
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.new_activity_main.*
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region
import ru.sovcombank.iotmerahackathon.adapters.TableItemAdapter
import ru.sovcombank.iotmerahackathon.entity.MyBeacon
import ru.sovcombank.iotmerahackathon.entity.MyBeaconMapper
import ru.sovcombank.iotmerahackathon.mqqt.MQQTAdapter
import ru.sovcombank.iotmerahackathon.network.NetworkApi
import ru.sovcombank.iotmerahackathon.transformers.PresentationObservableTransformer
import java.util.*


class MainActivity : AppCompatActivity(), BeaconConsumer {

    companion object {
        const val TAG = "MainActivity"
    }

    private val beaconManager = BeaconManager.getInstanceForApplication(this)

    private var tableItemAdapter: TableItemAdapter? = null
    private lateinit var networkApi: NetworkApi
    private lateinit var mqqtAdapter: MQQTAdapter
    private val beaconsMap: MutableMap<Int, MyBeacon> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity_main)

        mqqtAdapter = MQQTAdapter()
        mqqtAdapter.initialize(this)

        networkApi = NetworkApi().getInstance()
        tableItemAdapter = TableItemAdapter(this)

//        val data = mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48")

        val data = mutableListOf<Boolean>()
        for (i in 0..111) data.add(false)

//        data[25] = true

        rv_table.layoutManager = GridLayoutManager(this, 8, GridLayoutManager.VERTICAL, false)
        rv_table.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        rv_table.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_table.adapter = tableItemAdapter
        tableItemAdapter?.list = data

//        showCoordinates(data)

        val timerTask = object : TimerTask() {
            override fun run() {
                mqqtAdapter.publish(beaconsMap)
            }
        }

        Timer().schedule(timerTask, 0, 1000)

        btn_download.setOnClickListener {
            networkApi.getCoordinates().compose(PresentationObservableTransformer()).doOnSubscribe {
                Log.d("REQUEST", "Start")
            }
                .subscribe({ response ->
                    run {
                        if (response.code() == 200) {
                            showCoordinates(response.body()?.x, response.body()?.y)

                        }
                    }
                }, { t ->
                    Toast.makeText(this, "Error Network", Toast.LENGTH_SHORT).show()
                    Log.e("ERROR NETWORK", t.localizedMessage)
                })
        }


    }


    fun showCoordinates(x: Double?, y: Double?) {
        var mutX = 0
        var mutY = 0
        if (x!! >= 0) mutX = x.toInt()
        else if (mutX >= 14) mutX = 14
        if (y!! >= 0) mutY = y.toInt()
        else if (mutY > 8) mutY = 8
        runOnUiThread {
            tv_coordinates.text = "x = $x y = $y"
        }
        val position = ((13 - mutX) * 8) + mutY
        val data = mutableListOf<Boolean>()
        for (i in 0..111) data.add(false)
        data[position] = true
        runOnUiThread {
            showData(data)
        }
    }

    fun showData(data: List<Boolean>) {
        tableItemAdapter?.list = data
    }

    override fun onPause() {
        super.onPause()
        beaconManager.unbind(this)
    }

    override fun onResume() {
        super.onResume()
        beaconManager.bind(this)
    }


    //Beacon
    override fun onBeaconServiceConnect() {
        val rangeNotifier = RangeNotifier { beacons, region ->
            if (beacons.isNotEmpty()) {
                Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size)

//                beacons.sortedBy {
//                    it.rssi
//                }.take(3)
//                    .forEach {
//                    val beacon = MyBeaconMapper.map(it)
//                    beacon.distance
//                    beaconsMap[beacon.id] = beacon
//                }

                beacons.forEach {
                    val beacon = MyBeaconMapper.map(it)
                    beacon.distance
                    beaconsMap[beacon.id] = beacon
                }

                val firstBeacon = beacons.iterator().next()
            }
        }
        try {
            beaconManager.startRangingBeaconsInRegion(
                Region(
                    "myRangingUniqueId",
                    null,
                    null,
                    null
                )
            )
            beaconManager.addRangeNotifier(rangeNotifier)
            beaconManager.startRangingBeaconsInRegion(
                Region(
                    "myRangingUniqueId",
                    null,
                    null,
                    null
                )
            )
            beaconManager.addRangeNotifier(rangeNotifier)
        } catch (e: RemoteException) {
            Log.e(TAG, e.localizedMessage)
        }
    }
}
