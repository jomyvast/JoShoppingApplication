package com.making.shopping.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.making.shopping.R
import com.making.shopping.model.CartItem
import com.making.shopping.model.Product
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.row_product.view.*

class ProductListAdapter(private var museums:List<Product>,private val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<ProductListAdapter.MViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_product, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        //render
        vh.bind(museums[position],onItemClickListener)

    }
  
    override fun getItemCount(): Int {
        return museums.size
    }

    fun update(data:List<Product>){
        museums= data
        notifyDataSetChanged()
    }

    class MViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val textViewName:TextView = view.textViewName
        private val textViewPrice:TextView = view.textViewPrice
        private val textViewLink:TextView = view.textViewLink
        private val imageView:ImageView = view.imageView
        @SuppressLint("CheckResult")
        fun bind(museum: Product, clickListener: OnItemClickListener){
            textViewName.text = museum.name
            textViewPrice.text =museum.price

            Glide.with(imageView.context).load(museum.image).into(imageView)

            textViewLink.setOnClickListener {
                clickListener.onItemClicked(museum)
            }


            Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {

                itemView.addToCart.setOnClickListener { view ->

                    val item = CartItem(museum)

                    ShoppingCart.addItem(item)
                    //notify users
                    Snackbar.make(
                        (itemView.context as ProductListActivity).coordinator,
                        "${museum.name} added to your cart",
                        Snackbar.LENGTH_LONG
                    ).show()


                    it.onNext(ShoppingCart.getCart())

                }

                itemView.removeItem.setOnClickListener { view ->

                    val item = CartItem(museum)

                    ShoppingCart.removeItem(item, itemView.context)

                    Snackbar.make(
                        (itemView.context as ProductListActivity).coordinator,
                        "${museum.name} removed from your cart",
                        Snackbar.LENGTH_LONG
                    ).show()

                    it.onNext(ShoppingCart.getCart())

                }


            }).subscribe { cart ->

                var quantity = 0

                cart.forEach { cartItem ->

                    quantity += cartItem.quantity
                }


                (itemView.context as ProductListActivity).cart_size.text = quantity.toString()
                Toast.makeText(itemView.context, "Cart size $quantity", Toast.LENGTH_SHORT).show()

            }

            }
        }
    }
interface OnItemClickListener{
    fun onItemClicked(museum: Product)
}