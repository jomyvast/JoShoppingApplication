package com.making.shopping.model

import com.making.shopping.data.OperationCallback

interface ProductDataSource {

    fun retrieveMuseums(callback: OperationCallback<Product>)
    fun cancel()
}