package life.sabujak.pickle.ui.insta

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import life.sabujak.pickle.R
import life.sabujak.pickle.databinding.FragmentInstaBinding
import life.sabujak.pickle.ui.common.PickleViewModel
import life.sabujak.pickle.util.Logger
import life.sabujak.pickle.util.Status

class InstaFragment : Fragment() {
    val logger = Logger.getLogger("InstaFragment")

    lateinit var binding: FragmentInstaBinding
    lateinit var pickleViewModel: PickleViewModel
    lateinit var preViewModel: PreViewModel
    private val instaAdapter = InstaAdapter()
    private val gridLayoutManager by lazy {
        GridLayoutManager(context, 3)
    }
    var selectedPosition: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            (it as? AppCompatActivity)?.supportActionBar?.hide()
            pickleViewModel = ViewModelProviders.of(it).get(PickleViewModel::class.java)
            preViewModel = ViewModelProviders.of(it).get(PreViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.d("onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.d("onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_insta, container, false)
        binding.apply {
            viewModel = preViewModel
            recyclerView.adapter = instaAdapter
            recyclerView.layoutManager = gridLayoutManager
            val appBar = previewAppbarLayout
            appBar.layoutParams?.let {
                val behavior = AppBarLayout.Behavior()
                behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                    override fun canDrag(p0: AppBarLayout): Boolean {
                        return false
                    }
                })
                (it as CoordinatorLayout.LayoutParams).behavior = behavior
            }
            (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        }

        instaAdapter.itemClick = object : InstaAdapter.ItemClick {
            override fun onClick(view: View?, position: Int) {
                selectedPosition = position
                loadImageView(position)
                view?.let {
                    binding.recyclerView.smoothScrollBy(0, it.top)
                    binding.previewAppbarLayout.setExpanded(true)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pickleViewModel.items.observe(viewLifecycleOwner, Observer { pagedList ->
            instaAdapter.submitList(pagedList)
        })
        preViewModel.isAspectRatio.observe(viewLifecycleOwner, Observer {
            if (!binding.ivPreview.isEmpty()) loadImageView(selectedPosition)
        })
        pickleViewModel.initialLoadState.observe(viewLifecycleOwner, Observer {
            logger.d("initialLoadState = $it")
            if(it.status == Status.SUCCESS){
                logger.d("Item Count : " + instaAdapter.itemCount)
//                instaAdapter.itemClick?.onClick(null, 0)
            }
        })
        pickleViewModel.dataSourceState.observe(viewLifecycleOwner, Observer{
            logger.d("dataSourceState = $it")
        })
    }


    fun loadImageView(position: Int) {
        logger.d("loadImageView")
        val selected = instaAdapter.getPickleMedia(position)
        selected?.getUri()?.let {
            if (preViewModel.isAspectRatio.value == true) binding.ivPreview.setAspectRatio(it)
            else binding.ivPreview.setCropScale(it)
        }
    }
}