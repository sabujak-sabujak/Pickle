package life.sabujak.pickle.ui.insta

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_insta.*
import life.sabujak.pickle.R
import life.sabujak.pickle.databinding.FragmentInstaBinding
import life.sabujak.pickle.util.Logger

class InstaFragment : Fragment() {

    val logger = Logger.getLogger(InstaFragment::class)

    lateinit var binding: FragmentInstaBinding
    lateinit var viewModel :InstaViewModel
    val adapter = InstaAdapter()
    val gridLayoutManager by lazy {
        GridLayoutManager(context, 4)
    }

    var selectedPosition: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = InstaViewModel(context, lifecycle)
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_insta, container, false)
        binding.frag = this
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = gridLayoutManager
        adapter.itemClick = object: InstaAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                selectedPosition = position
                loadImageView(position)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun loadImageView(position: Int){
        val selected = adapter.getPickleMeida(position)
        selected?.getUri()?.let{
            Glide.with(iv_preview).load(it).into(iv_preview)
        }
    }

    fun ratioBtnClicked(view: View){
        logger.d("ratioBtnClicked")
        if(iv_preview.scaleType == ImageView.ScaleType.CENTER_CROP) iv_preview.scaleType = ImageView.ScaleType.CENTER_INSIDE
        else iv_preview.scaleType = ImageView.ScaleType.CENTER_CROP
        loadImageView(selectedPosition)
    }

}