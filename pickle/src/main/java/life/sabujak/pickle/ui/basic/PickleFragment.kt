package life.sabujak.pickle.ui.basic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import life.sabujak.pickle.R
import life.sabujak.pickle.databinding.FragmentPickleBinding
import life.sabujak.pickle.ui.common.OptionMenuViewModel
import life.sabujak.pickle.ui.common.PickleViewModel
import life.sabujak.pickle.util.Calculator
import life.sabujak.pickle.util.Logger
import life.sabujak.pickle.util.ext.showToast


class PickleFragment : Fragment() {

    val logger = Logger.getLogger(PickleFragment::class.java.simpleName)

    lateinit var binding: FragmentPickleBinding
    lateinit var viewModel: PickleViewModel
    private val adapter by lazy { PickleAdapter(viewModel.selectionManager) }
    private val gridLayoutManager by lazy {
        GridLayoutManager(
            context,
            Calculator.getColumnCount(context, R.dimen.pickle_column_width)
        )
    }
    lateinit var optionMenuViewModel :OptionMenuViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(PickleViewModel::class.java)
            optionMenuViewModel = ViewModelProviders.of(it).get(OptionMenuViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        logger.d("onCreate")
        viewModel.items.observe(this, Observer { pagedList ->
            adapter.submitList(pagedList)
        })
        optionMenuViewModel.clickEvent.observe(this, Observer {
            showToast("선택된 아이템은 로그에서 확인")
            viewModel.selectionManager.selectionList.forEach { value ->
                logger.i("$value")
            }
        })

        viewModel.selectionManager.count.observe(this, Observer {
            optionMenuViewModel.count.value = it
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
