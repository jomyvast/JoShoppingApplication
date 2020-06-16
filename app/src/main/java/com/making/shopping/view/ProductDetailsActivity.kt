package com.making.shopping.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.making.shopping.R
import com.making.shopping.model.Product
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.cart_list_item.*

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        intent.extras?.let { bundle ->
            product_title?.text = bundle.getString("name")
            product_price_tv?.text = bundle.getString("price")
            product_discription?.text = bundle.getString("description")
          val product_image = bundle.getString("image")

            Glide.with(imageView.context).load(product_image).into(imageView)

        }
        Paper.init(this)
        cart_size?.text = ShoppingCart.getShoppingCartSize().toString()
        textViewCart.setOnClickListener {
            startActivity(Intent(this, ShoppingCartActivity::class.java))
    }
    }
}