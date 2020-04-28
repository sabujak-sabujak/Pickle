package life.sabujak.pickle.ui.dialog

import android.app.Dialog
import android.content.Context.WINDOW_SERVICE
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
    private val viewModel: PickleViewModel by viewModels()

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
            this.viewModel.config = this.config
        } else {
            this.config = this.viewModel.config!!
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
            val rotation = windowManager.defaultDisplay.rotation

            when (rotation) {
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
            config.onResultListener?.onSuccess(viewModel.getPickleResult())
            dismiss()
        })

        binding.root.done
    }

}