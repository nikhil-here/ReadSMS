package com.nikhil.mandlik.readsms.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikhil.mandlik.readsms.R
import com.nikhil.mandlik.readsms.databinding.FragmentResultBinding
import com.nikhil.mandlik.readsms.utility.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private var _binding : FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val  adapterSMS by lazy { AdapterSMS() }
    private val searchViewModel: SearchViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        searchViewModel.smsList.observe(viewLifecycleOwner,{
            when(it){
                is Response.Error -> {}
                is Response.Loading -> {}
                is Response.Success -> {
                    adapterSMS.submitList(it.data)
                }
            }
        })
    }


    private fun initRecyclerView() {
        binding.rvSMS.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = adapterSMS
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}