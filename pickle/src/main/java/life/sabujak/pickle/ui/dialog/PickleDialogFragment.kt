package life.sabujak.pickle.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Lazy
import dagger.android.AndroidInjector
import life.sabujak.pickle.Config
import life.sabujak.pickle.R
import life.sabujak.pickle.databinding.DialogPickleBinding
import life.sabujak.pickle.ui.common.PickleViewModel
import life.sabujak.pickle.util.GridSpaceDecoration
import javax.inject.Inject

class PickleDialogFragment constructor() : DaggerPickleFragment() {
    private lateinit var binding: DialogPickleBinding
    private val viewModel: PickleViewModel by viewModels()
    private val topBarViewModel: TopBarViewModel by viewModels()

    @Inject
    lateinit var adapter: PickleDialogAdapter

    @Inject
    lateinit var layoutManager: Lazy<RecyclerView.LayoutManager>

    @Inject
    lateinit var decoration: GridSpaceDecoration

    private lateinit var config: Config

    override fun fragmentInjector(): AndroidInjector<out PickleDialogFragment> {
        return DaggerPickleDialogComponent.factory().create(this)
    }

    constructor(config: Config) : this() {
        this.config = config
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.config = this.config
        } else {
            this.config = viewModel.config!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_pickle, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet: FrameLayout =
                (dialog as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).isHideable = true
        }
        return bottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.topBarViewModel = topBarViewModel
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager.get()
        binding.recyclerView.addItemDecoration(decoration)
        viewModel.items.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
        viewModel.selectionManager.count.observe(
            viewLifecycleOwner,
            Observer { topBarViewModel.count.value = if (it == 0) "" else it.toString() })
        topBarViewModel.doneEvent.observe(viewLifecycleOwner, Observer {
            config.onResultListener.onSuccess(viewModel.getPickleResult())
            dismiss()
        })
    }

}