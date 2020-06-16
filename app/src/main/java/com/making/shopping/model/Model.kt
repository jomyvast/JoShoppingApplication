package com.making.shopping.model

import java.io.Serializable

/**
 * @author : Eduardo Medina
 * @since : 11/17/18
 * @see : https://developer.android.com/index.html
 */

data class Product(val id:Int, val name:String, val price:String,val description:String , val  image:String):Serializable