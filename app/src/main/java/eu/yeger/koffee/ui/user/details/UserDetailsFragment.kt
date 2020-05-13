package eu.yeger.koffee.ui.user.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.TransactionListAdapter
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences
import eu.yeger.koffee.utility.viewModelFactories

class UserDetailsFragment : Fragment() {

    private val userDetailsViewModel: UserDetailsViewModel by viewModelFactories {
        val context = requireContext()

        val userId = UserDetailsFragmentArgs.fromBundle(requireArguments()).userId

        UserDetailsViewModel(
            isActiveUser = userId == context.getUserIdFromSharedPreferences(),
            userId = userId,
            userRepository = UserRepository(context),
            transactionRepository = TransactionRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUserDetailsBinding.inflate(inflater).apply {
            viewModel = userDetailsViewModel
            transactionRecyclerView.adapter = TransactionListAdapter(OnClickListener { /*ingore*/ })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
