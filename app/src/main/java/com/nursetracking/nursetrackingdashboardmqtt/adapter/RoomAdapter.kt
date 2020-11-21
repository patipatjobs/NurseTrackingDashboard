package com.nursetracking.nursetrackingdashboardmqtt.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nursetracking.nursetrackingdashboardmqtt.R
import com.nursetracking.nursetrackingdashboardmqtt.model.dashboard.RoomList

class RoomAdapter(
    private val items: ArrayList<RoomList>?,
    private val listener: RoomListener
): RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view1 = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false))
        return view1
    }

    override fun getItemCount() = items?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = items?.get(position)
        room?.let { holder.bind(it) }
        holder.itemView.setOnClickListener { listener.onItemClick() }
    }

    class ViewHolder(itemsView: View): RecyclerView.ViewHolder(itemsView) {

        val textRoomTitle: TextView = itemView.findViewById(R.id.textRoomTitle)

        val recyclerView_nurse: RecyclerView = itemView.findViewById(R.id.recyclerView_nurse)

        fun bind(room: RoomList) {
            itemView.apply {
                val room_title = room.room_title
                var nurse = room.nurse_list
                var size_nurse = nurse?.size ?: 0
                Log.d( "Room $room_title ($size_nurse) :" , nurse.toString() )
                if(size_nurse >0)
                {
                    // แสดงรายชื่อพยาบาล
                    setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.color_content_1))
                    var nurseAdapter = room.nurse_list?.let { NurseAdapter(it) }
                    recyclerView_nurse.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                    recyclerView_nurse.isNestedScrollingEnabled = false
                    recyclerView_nurse.adapter = nurseAdapter
                    recyclerView_nurse.onFlingListener = null
                    //

                    // Image
                    val abc = findViewById(R.id.imgMember) as ImageView
                    abc.setImageDrawable(getResources().getDrawable(R.drawable.ic_bed_active));
                    //
                }
                else
                {
                    setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.color_content_2))
                }
                textRoomTitle.text = room_title
            }
            // Picasso.get().load(room.).into(itemView.imgMember)
        }

    }

}