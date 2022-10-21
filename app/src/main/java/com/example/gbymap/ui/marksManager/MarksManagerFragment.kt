package com.example.gbymap.ui.marksManager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gbymap.databinding.FragmentMarksManagerBinding
import com.example.gbymap.domain.marks.MarksEntity
import com.example.gbymap.ui.mainScreen.MarksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class MarksManagerFragment : Fragment() {

    private var _binding: FragmentMarksManagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MarksViewModel by viewModel(named("marks_view_model"))

    private lateinit var adapter: MarksManagerRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarksManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MarksManagerRecyclerAdapter(viewModel)
        rvInit()
        viewModel.getMarks()
        loadMarks()
        editMarkWaiter()
    }

    private fun rvInit() {
        binding.rvMarks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMarks.adapter = adapter

    }

    private fun loadMarks() {
        viewModel.marks.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
    }

    private fun editMarkWaiter() {
        viewModel.edited.observe(viewLifecycleOwner) { edited ->
            if (edited) {
                viewModel.editedMark.observe(viewLifecycleOwner) { mark ->
                    editMarkScreenVisibility(true)
                    binding.markName.setText(mark.name)
                    binding.markLat.setText(mark.latitude.toString())
                    binding.markLong.setText(mark.longitude.toString())

                    onYesEditOnClick(mark)
                    onNoEditOnClick()
                }
            }
        }
    }

    private fun onYesEditOnClick(mark: MarksEntity) {
        binding.addMarkAccept.setOnClickListener {
            if (binding.markName.text.toString().isNotEmpty()) {
                mark.name = binding.markName.text.toString()
            } else {
                Toast.makeText(requireContext(), "Введите имя", Toast.LENGTH_SHORT)
                    .show()
            }
            if (isDouble(binding.markLat.text.toString()) &&
                isDouble(binding.markLong.text.toString())) {
                mark.latitude = binding.markLat.text.toString().toDouble()
                mark.longitude = binding.markLong.text.toString().toDouble()
            } else {
                Toast.makeText(requireContext(), "Некорректное значение", Toast.LENGTH_SHORT)
                    .show()
            }

            viewModel.updateMark(mark)
            viewModel.getMarks()
            editMarkScreenVisibility(false)
            viewModel.updateMarkEnd()
        }
    }

    private fun onNoEditOnClick() {
        binding.addMarkCancel.setOnClickListener {
            editMarkScreenVisibility(false)
            viewModel.updateMarkEnd()
        }
    }

    private fun isDouble(value: String): Boolean {
        return value.toDoubleOrNull() != null
    }

    private fun editMarkScreenVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.blockScreen.visibility = View.VISIBLE
            binding.editMarkScreen.visibility = View.VISIBLE
            binding.blockScreen.isClickable = true
        } else {
            binding.blockScreen.isClickable = false
            binding.editMarkScreen.visibility = View.GONE
            binding.blockScreen.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}