package com.java90.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.whenResumed
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.java90.shoppinglisttesting.R
import com.java90.shoppinglisttesting.data.local.ShoppingItem
import com.java90.shoppinglisttesting.databinding.FragmentAddShoppingItemBinding
import com.java90.shoppinglisttesting.databinding.FragmentShoppingBinding
import com.java90.shoppinglisttesting.other.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddShoppingItemFragment : Fragment() {

    lateinit var viewModel: ShoppingViewModel

    private var _binding: FragmentAddShoppingItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentAddShoppingItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)

        setUpObservables()
        setUpListeners()
    }

    private fun setUpListeners() {
        with(binding) {
            ivShoppingImage.setOnClickListener {
                findNavController().navigate(AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment())
            }
            btnAddShoppingItem.setOnClickListener {
                viewModel.insertShoppingItem(
                        etShoppingItemName.text.toString(),
                        etShoppingItemAmount.text.toString(),
                        etShoppingItemPrice.text.toString()
                )
            }
        }
    }

    private fun setUpObservables() {
        with(viewModel){
            currentImageUrl.observe(viewLifecycleOwner, Observer { url ->
                binding.ivShoppingImage.apply {
                    Glide.with(this.context)
                            .load(url)
                            .centerCrop()
                            .error(R.drawable.ic_image)
                            .placeholder(R.drawable.ic_image)
                            .into(this)
                }
            })
            insertShoppingItemStatus.observe(viewLifecycleOwner, Observer {
                it.getContentIfNotHandled()?.let { result ->
                    when (result.status) {
                        Status.LOADING -> { /* NO-DEVELOP*/ }
                        Status.SUCCESS -> {
                            Snackbar.make(binding.root, "Added shopping Item", Snackbar.LENGTH_LONG).show()
                            findNavController().popBackStack()
                        }
                        Status.ERROR -> { /* NO-DEVELOP*/ }
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}