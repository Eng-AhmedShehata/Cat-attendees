package com.ashehata.catattendees.meeting

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

data class MeetingDay(
    val id: String = "",
    val topic: String = "",
    val dateMilliSecond: Long = 0,
    val type: String = MeetingType.Unknown.toString(),
    val membersAttendees: Int = 0,
    val period: Double = 0.0,
    val place: String = ""
) {
    fun getRealDate(): String {
        var date = ""

        try {
            val formatter = SimpleDateFormat("EEE, d-MM-yyyy, h:mm aaa", Locale.getDefault());
            date = formatter.format(Date(dateMilliSecond))

        } catch (e: Exception) {
            date = "Time badly formatted"
        }
        return date
    }
}

enum class MeetingType {
    Online,
    Offline,
    Unknown
}

enum class MeetingPlace {
    EngineeringLap,
    Sky,
    Other
}

fun setDayInfo(meetingDay: MeetingDay): Map<String, Any> {
    val mutableMap = mutableMapOf<String, Any>()
    mutableMap["topic"] = meetingDay.topic
    mutableMap["type"] = meetingDay.type
    mutableMap["dateMilliSecond"] = meetingDay.dateMilliSecond
    mutableMap["period"] = meetingDay.period
    mutableMap["place"] = meetingDay.place

    return mutableMap
}

fun getDayInfo(map: MutableMap<String, Any>, id: String): MeetingDay {

    return MeetingDay(
        id = id,
        topic = map["topic"].toString(),
        type = map["type"].toString(),
        dateMilliSecond = map["dateMilliSecond"].toString().toLong(),
        period = map["period"].toString().toDouble(),
        place = map["place"].toString()
    )
}