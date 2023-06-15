package com.example.sellspot.ui.activities.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sellspot.databinding.DialogProgressBinding

open class BaseFragment : Fragment() {

    private lateinit var mProgressDialog: Dialog
    private var _progressDialogBinding: DialogProgressBinding? = null
    private val progressDialogBinding get() = _progressDialogBinding!!

    private var _fragmentBinding: DialogProgressBinding? = null
    private val fragmentBinding get() = _fragmentBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentBinding = DialogProgressBinding.inflate(inflater, container, false)
        return fragmentBinding.root
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(requireActivity())
        _progressDialogBinding = DialogProgressBinding.inflate(layoutInflater)
        mProgressDialog.setContentView(progressDialogBinding.root)
        progressDialogBinding.tvProgressText.text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _progressDialogBinding = null
        _fragmentBinding = null
    }
}
