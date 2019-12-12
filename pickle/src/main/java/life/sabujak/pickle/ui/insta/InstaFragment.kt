package life.sabujak.pickle.ui.insta

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_insta.*
import life.sabujak.pickle.R
import life.sabujak.pickle.databinding.FragmentInstaBinding
import life.sabujak.pickle.ui.PickleViewModel
import life.sabujak.pickle.util.Logger

class InstaFragment : Fragment() {
    val logger = Logger.getLogger("InstaFragment")

    lateinit var binding: FragmentInstaBinding
    lateinit var viewModel: PickleViewModel
    lateinit var preViewModel: PreViewModel
    val instaAdapter = InstaAdapter()
    val gridLayoutManager by lazy {
        GridLayoutManager(context, 3)
    }
    var selectedPosition: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AppCompatActivity).supportActionBar?.hide()
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(PickleViewModel::class.java)
            preViewModel = ViewModelProviders.of(it).get(PreViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.d("onCreate")
        viewModel.items.observe(this, Observer { pagedList ->
            instaAdapter.submitList(pagedList)
        })
        preViewModel.scaleType.observe(this, Observer {
            iv_preview.drawable?.let {
                loadImageView(selectedPosition)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_insta, container, false)
        binding.apply {
            viewModel = preViewModel
            recyclerView.adapter = instaAdapter
            recyclerView.layoutManager = gridLayoutManager
            val appBar = previewAppbarLayout as AppBarLayout
            appBar.layoutParams?.let{
                val behavior = AppBarLayout.Behavior()
                behavior.setDragCallback(object: AppBarLayout.Behavior.DragCallback(){
                    override fun canDrag(p0: AppBarLayout): Boolean {
                        return false
                    }
                })
                (it as CoordinatorLayout.LayoutParams).behavior = behavior
            }
        }

        instaAdapter.itemClick = object : InstaAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                selectedPosition = position
                loadImageView(position)
                binding.recyclerView.smoothScrollBy(0, view.top)
                binding.previewAppbarLayout.setExpanded(true)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun loadImageView(position: Int) {
        val selected = instaAdapter.getPickleMeida(position)
        selected?.getUri()?.let {
            when (preViewModel.scaleType.value) {
                GlideScaleType.CENTER_CROP -> Glide.with(iv_preview).load(it).centerCrop().into(iv_preview)
                GlideScaleType.CENTER_INSIDE -> Glide.with(iv_preview).load(it).centerInside().into(iv_preview)
                else -> {}
            }
        }
    }
}
