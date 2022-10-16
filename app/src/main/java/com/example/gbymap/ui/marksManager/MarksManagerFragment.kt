package com.example.gbymap.ui.marksManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gbymap.databinding.FragmentMarksManagerBinding

class MarksManagerFragment : Fragment() {

    private var _binding: FragmentMarksManagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarksManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = MarksManagerFragment()
    }
}