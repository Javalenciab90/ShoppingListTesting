package com.java90.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.java90.shoppinglisttesting.databinding.FragmentShoppingBinding
import com.java90.shoppinglisttesting.ui.adapters.ShoppingItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingFragment : Fragment() {

    lateinit var viewModel: ShoppingViewModel

    private var _binding: FragmentShoppingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        setUpObservables()
        setUpRecyclerView()
        binding.fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment())
        }
    }

    private fun setUpRecyclerView() {
        binding.rvShoppingItems.apply {
            adapter = ShoppingItemAdapter()
            layoutManager = LinearLayoutManager(requireContext())
            handleSwipeRecyclerView(this)
        }
    }

    private fun handleSwipeRecyclerView(recyclerView: RecyclerView) {
        val itemTouchCallback = object :
                ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {

            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                binding.rvShoppingItems.adapter?.let {
                    if (it is ShoppingItemAdapter) {
                        val item = it.currentList[pos]
                        viewModel.deleteShoppingItem(item)
                        Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).apply {
                            setAction("Undo") {
                                viewModel.insertShoppingItemIntoDb(item)
                            }
                            show()
                        }
                    }
                }
            }
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView)
    }

    private fun setUpObservables() {
        with(viewModel) {
            shoppingItems.observe(viewLifecycleOwner, Observer { listData ->
                binding.rvShoppingItems.adapter?.let {
                    if (it is ShoppingItemAdapter) {
                        it.submitList(listData)
                    }
                }
            })
            totalPrice.observe(viewLifecycleOwner, Observer {
                val price = it ?: 0f
                val priceText = "Total Price: $price€"
                binding.tvShoppingItemPrice.text = priceText
            })
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}