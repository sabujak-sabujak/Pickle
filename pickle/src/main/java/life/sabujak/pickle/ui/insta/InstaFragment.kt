package life.sabujak.pickle.ui.insta

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
        preViewModel.scaleType.observe(this, Observer { scaleType ->
            iv_preview.scaleType = scaleType
            iv_preview.drawable?.let{
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
        }

        instaAdapter.itemClick = object : InstaAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                selectedPosition = position
                loadImageView(position)
                binding.recyclerView.smoothScrollBy(0, view.top)
                binding.previewLayout.setExpanded(true)
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
            Glide.with(iv_preview).load(it).into(iv_preview)
        }
    }
}