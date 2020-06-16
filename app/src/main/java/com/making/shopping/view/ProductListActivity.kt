package com.making.shopping.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.making.shopping.R
import com.making.shopping.di.Injection
import com.making.shopping.model.Product
import com.making.shopping.viewmodel.ProductListViewModel
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.layout_error.*


class ProductListActivity : AppCompatActivity(),OnItemClickListener {

    private lateinit var viewModel: ProductListViewModel
    private lateinit var adapter: ProductListAdapter

    companion object {
        const val TAG= "CONSOLE"
    }

    override fun onItemClicked(museums: Product) {

        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("name",museums.name)
        intent.putExtra("price",museums.price)
        intent.putExtra("description",museums.description)
        intent.putExtra("image",museums.image)
        startActivity(intent)
//        Toast.makeText(this,"User name ${museums.id} \n Phone:${museums.name}", Toast.LENGTH_LONG)
//            .show()
//        Log.i("USER_",museums.name)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        setupViewModel()
        setupUI()
        Paper.init(this)
        cart_size.text = ShoppingCart.getShoppingCartSize().toString()
        showCart.setOnClickListener {
            startActivity(Intent(this, ShoppingCartActivity::class.java))
        }

    }

    //ui
    private fun setupUI(){
       adapter = ProductListAdapter(viewModel.museums.value?: emptyList(),this)
        val gridLayoutManager = GridLayoutManager(applicationContext, 2)
        recyclerView.setLayoutManager(gridLayoutManager);
       // recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter= adapter
    }
    private fun setupViewModel(){
        viewModel = ViewModelProvider(this,Injection.provideViewModelFactory()).get(ProductListViewModel::class.java)
            viewModel.museums.observe(this,renderMuseums)

        viewModel.isViewLoading.observe(this,isViewLoadingObserver)
        viewModel.onMessageError.observe(this,onMessageErrorObserver)
        viewModel.isEmptyList.observe(this,emptyListObserver)
    }

    //observers
    private val renderMuseums= Observer<List<Product>> {
        Log.v(TAG, "products updated $it")
        layoutError.visibility=View.GONE
        layoutEmpty.visibility=View.GONE
        adapter.update(it)
    }

    private val isViewLoadingObserver= Observer<Boolean> {
        Log.v(TAG, "isViewLoading $it")
        val visibility=if(it)View.VISIBLE else View.GONE
        progressBar.visibility= visibility
    }

    private val onMessageErrorObserver= Observer<Any> {
        Log.v(TAG, "onMessageError $it")
        layoutError.visibility=View.VISIBLE
        layoutEmpty.visibility=View.GONE
        textViewError.text= "Error $it"
    }

    private val emptyListObserver= Observer<Boolean> {
        Log.v(TAG, "emptyListObserver $it")
        layoutEmpty.visibility=View.VISIBLE
        layoutError.visibility=View.GONE
    }

     //If you require updated data, you can call the method "loadMuseum" here
     override fun onResume() {
        super.onResume()
        viewModel.loadMuseums()
     }

}
