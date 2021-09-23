package com.nikhil.mandlik.readsms.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nikhil.mandlik.readsms.R
import com.nikhil.mandlik.readsms.databinding.FragmentSearchBinding
import com.nikhil.mandlik.readsms.entity.SMS
import com.nikhil.mandlik.readsms.utility.PermissionHelper
import com.nikhil.mandlik.readsms.utility.Response
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

private const val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var mPhoneNumber: String = ""
    private var mNoOfDays: Int = 0
    private var mSMSList = emptyList<SMS>()

    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        searchViewModel.smsList.observe(viewLifecycleOwner, {
            when (it) {
                is Response.Error -> {
                    showError(msg = it.msg ?: "Something Went Wrong")
                }
                is Response.Loading -> {
                    showLoading()
                }
                is Response.Success -> {
                    showResult(it.data!!)
                    mSMSList = it.data
                }
            }
        })
    }


    private fun initListeners() {
        binding.btnSearch.setOnClickListener {
            if (validate()) {
                if (PermissionHelper.hasReadSmsPermission(requireContext())) {
                    searchSms()
                } else {
                    PermissionHelper.requestPermission(this)
                }
            }
        }

        binding.cvResult.setOnClickListener {
            if (mSMSList.size > 0) {
                findNavController().navigate(R.id.action_searchFragment_to_resultFragment)
            }
        }

    }


    private fun searchSms() {
        searchViewModel.searchSms(
            activity?.contentResolver,
            phoneNumber = "+91$mPhoneNumber",
            noOfDays = mNoOfDays
        )
    }

    private fun showResult(smsList: List<SMS>) {
        binding.groupLoading.visibility = View.GONE
        binding.groupResult.visibility = View.VISIBLE
        if (smsList.isNotEmpty()) {
            binding.tvSMSCount.text = smsList.size.toString()
            binding.tvSMSCountHelper.text = "Tap to see all messages"
            binding.cvResult.isClickable = true
        } else {
            binding.tvSMSCount.text = "0"
            binding.tvSMSCountHelper.text = "No Record Found"
        }
    }

    private fun showError(msg: String) {
        binding.groupLoading.visibility = View.GONE
        binding.groupResult.visibility = View.VISIBLE
        binding.tvSMSCount.text = "0"
        binding.tvSMSCountHelper.text = "Something Went Wrong : $msg"
    }

    private fun showLoading() {
        binding.groupResult.visibility = View.GONE
        binding.groupLoading.visibility = View.VISIBLE
        binding.pbLoading.visibility = View.VISIBLE
        binding.tvLoading.visibility = View.VISIBLE
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        searchSms()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
        showMessage("Permission Denied")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun validate(): Boolean {
        mPhoneNumber = binding.tilPhoneNumber.editText?.text.toString()
        mNoOfDays = binding.tilNoOfDays.editText?.text?.let {
            if (it.isEmpty())
                return@let 0
            else
                return@let it.toString().toInt()
        } ?: 0
        val isPhoneNumberValid = mPhoneNumber.length == 10
        val isNoOfDaysValid = mNoOfDays > 0
        binding.tilPhoneNumber.error = if (isPhoneNumberValid) null else "enter valid phone number"
        binding.tilNoOfDays.error = if (isNoOfDaysValid) null else "enter valid no of days"
        return isPhoneNumberValid && isNoOfDaysValid
    }

    private fun showMessage(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}