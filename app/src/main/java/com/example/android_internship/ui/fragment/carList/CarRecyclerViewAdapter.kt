package com.example.android_internship.ui.fragment.carList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.android_internship.R
import com.example.android_internship.car.Car
import com.example.android_internship.util.BaseRecyclerViewAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class CarRecyclerViewAdapter(context: Context): BaseRecyclerViewAdapter<Car, CarViewHolder>(context) {

    private val _clickPublisher = PublishSubject.create<Car>()

    fun itemClicks(): Observable<Car>  = _clickPublisher

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.car_view_holder,parent,false).also {
            return CarViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.carTextView.text = mDataSet[position].name
        holder.cardClicks.map { mDataSet[position] }.subscribe(_clickPublisher)
    }
}