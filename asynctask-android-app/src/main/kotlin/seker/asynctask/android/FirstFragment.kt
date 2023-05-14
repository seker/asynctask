package seker.asynctask.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import seker.asynctask.android.app.R
import seker.asynctask.android.app.databinding.FragmentFirstBinding

import seker.asynctask.AsyncTaskExecutor
import seker.asynctask.logger.Log
import seker.asynctask.runOnMainThread
import seker.asynctask.runOnMainThreadWhenIdle

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.button.setOnClickListener {
            AsyncTaskExecutor.getInstance().runOnMainThread {
                Log.d("runOnMainThread")
            }
            AsyncTaskExecutor.getInstance().runOnMainThread({
                Log.d("runOnMainThread: 1000L")
            }, 1000L)

            AsyncTaskExecutor.getInstance().runOnMainThreadWhenIdle {
                Log.d("runOnMainThreadWhenIdle")
            }

            AsyncTaskExecutorDemo().demo()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}