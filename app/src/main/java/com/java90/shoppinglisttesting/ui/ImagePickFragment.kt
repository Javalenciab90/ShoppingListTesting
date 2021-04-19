package com.java90.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.java90.shoppinglisttesting.databinding.FragmentImagePickBinding
import com.java90.shoppinglisttesting.databinding.FragmentShoppingBinding
import com.java90.shoppinglisttesting.other.Constants.GRID_SPAN_COUNT
import com.java90.shoppinglisttesting.ui.adapters.ImageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePickFragment : Fragment() {

    lateinit var viewModel: ShoppingViewModel

    private var _binding: FragmentImagePickBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentImagePickBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        setUpImageAdapter()
    }

    private fun setUpImageAdapter() {
        binding.rvImages.apply {
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
            adapter = ImageAdapter(onItemClickListener = { url ->
                setCurrentImageAndNavigateAddItem(url)
            })
        }
    }

    private fun setCurrentImageAndNavigateAddItem(urlString: String) {
        findNavController().popBackStack()
        viewModel.serCurrentImageUrl(urlString)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}