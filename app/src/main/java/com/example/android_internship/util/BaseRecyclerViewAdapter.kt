package com.example.android_internship.util
import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder>(protected val context: Context): RecyclerView.Adapter<VH>() {
    protected var mDataSet = ArrayList<T>()

    override fun getItemCount() = mDataSet.size

    fun updateDataSet(dataSet: Collection<T>){
        mDataSet = ArrayList<T>(dataSet)
        notifyDataSetChanged()
    }
}