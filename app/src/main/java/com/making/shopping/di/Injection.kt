package com.making.shopping.di

import androidx.lifecycle.ViewModelProvider
import com.making.shopping.model.ProductDataSource
import com.making.shopping.model.MuseumRepository
import com.making.shopping.viewmodel.ViewModelFactory

object Injection {

    private val museumDataSource:ProductDataSource = MuseumRepository()
    private val museumViewModelFactory = ViewModelFactory(museumDataSource)

    fun providerRepository():ProductDataSource{
        return museumDataSource
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory{
        return museumViewModelFactory
    }
}