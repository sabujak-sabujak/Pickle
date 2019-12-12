package life.sabujak.pickle.ui.basic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import life.sabujak.pickle.R
import life.sabujak.pickle.databinding.FragmentPickleBinding
import life.sabujak.pickle.ui.PickleViewModel
import life.sabujak.pickle.util.Calculator
import life.sabujak.pickle.util.Logger


class PickleFragment : Fragment() {

    val logger = Logger.getLogger(PickleFragment::class.java.simpleName)

    lateinit var binding: FragmentPickleBinding
    lateinit var viewModel: PickleViewModel
    private val adapter = PickleAdapter()
    private val gridLayoutManager by lazy {
        GridLayoutManager(context, Calculator.getColumnCount(context, R.dimen.pickle_column_width))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(PickleViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.d("onCreate")
        viewModel.items.observe(this, Observer { pagedList ->
            adapter.submitList(pagedList)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pickle, container, false)
        binding.recyclerView.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.layoutManager = gridLayoutManager
        return binding.root
    }


}