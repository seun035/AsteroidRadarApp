package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.udacity.asteroidradar.AsteroidItemListener
import com.udacity.asteroidradar.AsteroidType
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val viewModelFactory = MainVewModelFactory(requireNotNull(this.activity).application)
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    lateinit var navController: NavController
    lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this


        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(binding.root)

        binding.viewModel = viewModel
        val adapter = AsteroidAdapter(AsteroidItemListener { asteroid -> navController.navigate(MainFragmentDirections.actionShowDetail(asteroid)) })
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroidList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.show_all_menu -> {
                viewModel.setFilter(AsteroidType.NEXTWEEK)
                true
            }
            R.id.show_rent_menu -> {
                viewModel.setFilter(AsteroidType.TODAY)
                true
            }
            R.id.show_buy_menu -> {
                viewModel.setFilter(AsteroidType.SAVED)
                true
            }
            else -> true
        }
    }
}
