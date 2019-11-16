package ru.sovcombank.iotmerahackathon.entity

import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.Identifier
import kotlin.math.pow

class MyBeaconMapper {

    companion object {
        fun map(beacon: Beacon): MyBeacon {
            return MyBeacon(
                getId(beacon.id2),
                getDistance(beacon),
                getX(beacon.id2),
                getY(beacon.id2)
            )
        }

        private fun getId(id: Identifier): Int = id.toByteArray()[1].toInt()

        private fun getX(id: Identifier): Int {
            return id.toHexString().substring(6,10).toInt(16)
        }
        private fun getY(id: Identifier): Int  {
            return id.toHexString().takeLast(4).toInt(16)
        }

        private fun getDistance(beacon: Beacon): Double {
            val pathLossFactor = 2.0

            beacon.runningAverageRssi
            val calibrateRssi = beacon.id1.toByteArray()[1]
           return 10.0.pow(((calibrateRssi - 41) - beacon.rssi) / (10 * pathLossFactor))
        }
    }
}