package life.sabujak.pickle.ui.dialog

import android.app.Application
import android.app.Dialog
import android.content.Context.WINDOW_SERVICE
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Lazy
import dagger.android.AndroidInjector
import kotlinx.android.synthetic.main.layout_top_bar.view.*
import life.sabujak.pickle.R
import life.sabujak.pickle.databinding.DialogPickleBinding
import life.sabujak.pickle.util.recyclerview.GridSpaceDecoration
import life.sabujak.pickle.util.Logger
import javax.inject.Inject


class PickleDialogFragment constructor() : DaggerPickleFragment() {
    private val logger = Logger.getLogger(PickleDialogFragment::javaClass.name)

    private lateinit var binding: DialogPickleBinding
    private lateinit var config: Config
    private lateinit var viewModel: PickleViewModel

    @Inject
    lateinit var adapter: PickleDialogAdapter

    @Inject
    lateinit var layoutManager: Lazy<RecyclerView.LayoutManager>

    @Inject
    lateinit var decoration: GridSpaceDecoration

    override fun fragmentInjector(): AndroidInjector<out PickleDialogFragment> {
        return DaggerPickleDialogComponent.factory().create(this)
    }

    constructor(config: Config) : this() {
        this.config = config
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val viewModelFactory = PickleViewModelFactory(requireContext().applicationContext as Application, config)
            this.viewModel = ViewModelProvider(this, viewModelFactory).get(PickleViewModel::class.java)
        } else {
            this.viewModel = ViewModelProvider(this).get(PickleViewModel::class.java)
            this.config = viewModel.config
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
            val bsd = dialog as BottomSheetDialog
            val bottomSheet: FrameLayout = bsd.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!

            val windowManager = context?.getSystemService(WINDOW_SERVICE) as WindowManager
            when (windowManager.defaultDisplay.rotation) {
                Surface.ROTATION_0 -> {
                    BottomSheetBehavior.from(bottomSheet).state = STATE_COLLAPSED
                    BottomSheetBehavior.from(bottomSheet).skipCollapsed = false
                }
                else -> {
                    BottomSheetBehavior.from(bottomSheet).state = STATE_EXPANDED
                    BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
                }
            }


        }
        return bottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        adapter.selectionManager = viewModel.selectionManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager.get()
        binding.recyclerView.addItemDecoration(decoration)
        viewModel.items.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
        viewModel.doneEvent.observe(viewLifecycleOwner, Observer {
            config.onResultListener?.onSuccess(viewModel.selectionManager.getPickleResult())
            dismiss()
        })

    }

}