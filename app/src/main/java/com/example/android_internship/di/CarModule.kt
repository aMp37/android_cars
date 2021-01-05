package com.example.android_internship.di

import com.example.android_internship.car.CarService
import com.example.android_internship.car.CarServiceImpl
import com.example.android_internship.car.CarUseCase
import com.example.android_internship.car.CarUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class CarInteractorModule {

    @Binds
    abstract fun bindCarService(carServiceImpl: CarServiceImpl): CarService

    @Binds
    abstract fun bindCarInteractor(carInteractorImpl: CarUseCaseImpl): CarUseCase
}