package com.nursetracking.nursetrackingdashboardmqtt.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.nursetracking.nursetrackingdashboardmqtt.R
import com.nursetracking.nursetrackingdashboardmqtt.adapter.RoomAdapter
import com.nursetracking.nursetrackingdashboardmqtt.adapter.RoomListener
import com.nursetracking.nursetrackingdashboardmqtt.model.dashboard.DashboardPublish
import com.nursetracking.nursetrackingdashboardmqtt.model.dashboard.Layout
import com.nursetracking.nursetrackingdashboardmqtt.model.dashboard.RoomList
import com.nursetracking.nursetrackingdashboardmqtt.services.mqtt.MQTTHelper
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() , RoomListener {

    private lateinit var mqttHelper: MQTTHelper
    lateinit var mqttAndroidClient: MqttAndroidClient

    private var datetime: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    private var gson = GsonBuilder().setPrettyPrinting().create()

    var room_list:String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MQTTStart()
//        getItemRoom(null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    fun MQTTStart(){
        mqttHelper = MQTTHelper( requireContext())
        mqttHelper.init()
        mqttHelper.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                if( topic=="Phyathai/Ward1/Dashboard" ) {
                    val JsonMessage = JsonParser().parse(message.toString()).getAsJsonObject()
                    getItemRoom(JsonMessage)
                }
            }

            override fun connectionLost(cause: Throwable?) {
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
            }

        })
    }

    fun getItemRoom(JsonMessage: JsonElement) {
//        Log.d("json JsonMessage",JsonMessage.toString())
        val jsonMessage = gson.fromJson(JsonMessage,DashboardPublish::class.java)
        val jsonLayoutX = jsonMessage.layoutX
        val jsonLayoutY = jsonMessage.layoutY
        val json_room_listX = jsonLayoutX.room_list
        val json_room_listY = jsonLayoutY.room_list

        val roomAdapter1 = RoomAdapter( json_room_listY ,this)
        recyclerView_room1.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            adapter = roomAdapter1
            onFlingListener = null
        }
        roomAdapter1.notifyDataSetChanged()

        val roomAdapter2 = RoomAdapter( json_room_listX ,this)
        recyclerView_room2.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            adapter = roomAdapter2
            onFlingListener = null
        }
        roomAdapter2.notifyDataSetChanged()
    }

//    fun getRoom(layout: Int, json: ArrayList<RoomList>?){
//        var recyclerviewRoom:RecyclerView
//        var layoutManager : GridLayoutManager
//        if(layout == 3){
//            for(i in 1..2) {
//                if(i==1){
//                    layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
//                    recyclerviewRoom = recyclerView_room1
//                }else{
//                    layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
//                    recyclerviewRoom = recyclerView_room2
//                }
//                val roomAdapter = RoomAdapter( json ,this)
//                recyclerviewRoom.apply {
//                    layoutManager
//                    isNestedScrollingEnabled = false
//                    adapter = roomAdapter
//                    onFlingListener = null
//                }
//                roomAdapter.notifyDataSetChanged()
//            }
//        }
//
//    }

    override fun onItemClick() {
        TODO("Not yet implemented")
    }

}