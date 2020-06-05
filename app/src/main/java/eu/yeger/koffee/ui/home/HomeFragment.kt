package eu.yeger.koffee.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentHomeBinding
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.deleteUserIdFromSharedPreferences
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences
import eu.yeger.koffee.utility.viewModelFactories

class HomeFragment : Fragment() {

    private val userId by lazy {
        requireContext().getUserIdFromSharedPreferences()
    }

    private val homeViewModel: HomeViewModel by viewModelFactories {
        val context = requireContext()
        HomeViewModel(
            userId!!,
            UserRepository(context),
            ProfileImageRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (userId == null) {
            onNotFound()
            return null
        }
        return FragmentHomeBinding.inflate(inflater).apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private fun onNotFound() {
        val message = when (userId) {
            null -> R.string.no_user_selected
            else -> R.string.active_user_deleted
        }
        requireContext().deleteUserIdFromSharedPreferences()
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.got_to_selection) { _, _ ->
                val direction = HomeFragmentDirections.toUserSelection()
                findNavController().navigate(direction)
            }
            .setCancelable(false)
            .create()
            .show()
    }
}