package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidsAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
    }

    lateinit var adapter: AsteroidsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        adapter = AsteroidsAdapter(AsteroidsAdapter.OnClickListener {
            viewModel.displayAsteroidDetails(it)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.status.observe(viewLifecycleOwner, {
            when (it) {
                NasaApiStatus.LOADING -> binding.statusLoadingWheel.visibility = View.VISIBLE
                NasaApiStatus.DONE -> binding.statusLoadingWheel.visibility = View.GONE
                NasaApiStatus.ERROR -> View.GONE
            }
        })

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_all_menu -> {
                viewModel.getAsteroids()
                adapter.notifyDataSetChanged()
                true
            }
            R.id.show_today_menu -> {
                viewModel.getTodayAsteroids()
                adapter.notifyDataSetChanged()
                true
            }
            R.id.show_saved_menu -> {
                viewModel.getAllAsteroids()
                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
