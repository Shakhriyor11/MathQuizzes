package com.portfolio.someapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.portfolio.someapplication.R
import com.portfolio.someapplication.adapters.OptionAdapter
import com.portfolio.someapplication.databinding.FragmentQuestionBinding
import com.portfolio.someapplication.models.Question
import com.portfolio.someapplication.models.Quizz

class QuestionFragment : Fragment() {
    private var _binding: FragmentQuestionBinding? = null
    private val binding: FragmentQuestionBinding
        get() = _binding ?: throw RuntimeException("FragmentQuestionBinding == null")

    private var quizzes: MutableList<Quizz>? = null
    private var questions: MutableMap<String, Question>? = null
    private var index = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFirestore()
        setUpEventListener()
    }

    private fun setUpEventListener() {
        with(binding) {
            btnPrevious.setOnClickListener {
                index--
                bindViews()
            }

            btnNext.setOnClickListener {
                index++
                bindViews()
            }

            btnSubmit.setOnClickListener {
                Log.d("FINALQUIZ", questions.toString())
                val json  = Gson().toJson(quizzes!![0])
                findNavController().navigate(
                    QuestionFragmentDirections.actionQuestionFragmentToResultFragment(json)
                )
            }
        }
    }

    private fun setUpFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        var date = arguments?.getString("DATE")
        if (date != null) {
            firestore.collection("quizzes").whereEqualTo("title", date)
                .get()
                .addOnSuccessListener {
                    if(it != null && !it.isEmpty){
                        quizzes = it.toObjects(Quizz::class.java)
                        questions = quizzes!![0].questions
                        bindViews()
                    }
                }
        }
    }

    private fun bindViews() {
        with(binding) {
            btnPrevious.visibility = View.GONE
            btnSubmit.visibility = View.GONE
            btnNext.visibility = View.GONE

            when (index) {
                1 -> { //first question
                    btnNext.visibility = View.VISIBLE
                }
                questions!!.size -> { // last question
                    btnSubmit.visibility = View.VISIBLE
                    btnPrevious.visibility = View.VISIBLE
                }
                else -> { // Middle
                    btnPrevious.visibility = View.VISIBLE
                    btnNext.visibility = View.VISIBLE
                }
            }

            val question = questions!!["question$index"]
            question?.let {
                description.text = it.description
                val optionAdapter = OptionAdapter(requireContext(), it)
                optionList.layoutManager = LinearLayoutManager(requireContext())
                optionList.adapter = optionAdapter
                optionList.setHasFixedSize(true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val fab: FloatingActionButton = (activity as AppCompatActivity).findViewById(R.id.fab)
        fab.visibility = View.INVISIBLE
    }

    override fun onStop() {
        super.onStop()
        val fab: FloatingActionButton = (activity as AppCompatActivity).findViewById(R.id.fab)
        fab.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}