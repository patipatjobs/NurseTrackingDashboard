package com.nursetracking.nursetrackingdashboardmqtt.model.dashboard

data class androidbox (
        val device_id: String,
        val datetime: String,
        val message: String? = null
)