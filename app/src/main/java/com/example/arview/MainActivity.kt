package com.example.arview

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arview.data.GameInfo
import com.example.arview.databinding.ActivityMainBinding
import com.example.arview.databinding.DialogLayoutBinding
import com.example.arview.network.isNetworkAvailable
import com.example.arview.viewModel.DbViewModel
import com.example.arview.viewModel.GameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private val viewModel: GameViewModel by viewModel()
    private val dbViewModel: DbViewModel by viewModel()
    private var games = ArrayList<GameInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpAdapter()
        setUpRecyclerView()
        showData()
        setOnClick()
    }

    private fun setUpAdapter() {
        adapter = MainAdapter()
    }

    private fun setUpRecyclerView() {
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
    }

    private fun showData() {
        if (this.isNetworkAvailable()) {
            getDataFromServer()
        } else {
            Log.d("here", "no internet")

            getDataFromDb()
        }
    }

    private fun getDataFromDb() {
        dbViewModel.gameList.observe(this, Observer { game ->
            for (i in game) {
                games.add(
                    GameInfo(
                        name = i.name,
                        channel = i.channel,
                        viewer = i.viewer,
                        image = i.image
                    )
                )
            }
            Log.d("here", "here2 ${game.size}")
            adapter.update(games)
        })
    }

    private fun getDataFromServer() {
        viewModel.getGames()
        viewModel.resourceGame.observe(this, Observer {
            it.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is com.example.arview.network.Resource.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }
                    is com.example.arview.network.Resource.Success -> {
                        binding.loading.visibility = View.GONE
                        var count = 0
                        for (i in resource.data.top) {
                            games.add(
                                GameInfo(
                                    name = i.game.name,
                                    channel = i.channels,
                                    viewer = i.viewers,
                                    image = i.game.box.large
                                )
                            )
                            dbViewModel.insert(
                                GameInfo(
                                    //i come to this solution ( auto increment id by my side)
                                    //because i when did it in entity app crashed and android studio didn't show error
                                    // after spending some time i didn't find solution decide to do it like this
                                    id = count++,
                                    name = i.game.name,
                                    channel = i.channels,
                                    viewer = i.viewers,
                                    image = i.game.box.large
                                )
                            )

                        }
                        adapter.update(games)
                    }
                    is com.example.arview.network.Resource.GenericError -> {
                        Log.d("here", "here3 ${resource.errorResponse.message}")
                        binding.loading.visibility = View.GONE
                    }
                    is com.example.arview.network.Resource.Error -> {
                        binding.loading.visibility = View.GONE

                    }
                }
            }
        })
    }

    private fun setOnClick() {
        binding.ratingBtn.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialogBinding =
            DialogLayoutBinding.inflate(layoutInflater, dialogLayout as ViewGroup, false)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp

        dialog.show()
        dialogBinding.btn.setOnClickListener {
            dialog.dismiss()
        }


    }
}