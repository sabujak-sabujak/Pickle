package life.sabujak.pickle.ui.insta

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.Image
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.databinding.FragmentInstaBinding
import life.sabujak.pickle.ui.common.OptionMenuViewModel
import life.sabujak.pickle.util.Logger

class InstaFragment : Fragment(), OnInstaEventListener {
    val logger = Logger.getLogger(this.javaClass.simpleName)

    lateinit var binding: FragmentInstaBinding
    lateinit var instaViewModel: InstaViewModel
    lateinit var optionMenuViewModel: OptionMenuViewModel
    private val instaAdapter by lazy {
        InstaAdapter(lifecycle, instaViewModel.selectionManager, this)
    }
    private val gridLayoutManager by lazy {
        GridLayoutManager(context, 3)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            (it as? AppCompatActivity)?.supportActionBar?.hide()
            instaViewModel = ViewModelProviders.of(it).get(InstaViewModel::class.java)
            optionMenuViewModel = ViewModelProviders.of(it).get(OptionMenuViewModel::class.java)
            optionMenuViewModel.setCountable(false)
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
            viewModel = instaViewModel
            recyclerView.adapter = instaAdapter
            lifecycleOwner = viewLifecycleOwner
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
            ivPreview.addOnCropListener(object : OnCropListener {
                override fun onSuccess(bitmap: Bitmap) {
                    val dialogLayout = layoutInflater.inflate(R.layout.dialog_result, null)
                    val dialogImageView = dialogLayout.findViewById<ImageView>(R.id.iv_image)
                    dialogImageView.setImageBitmap(bitmap)
                    dialogImageView.rotation = (instaViewModel.selectedPickleMedia as Image).orientation
                    dialogImageView.scaleType = ImageView.ScaleType.FIT_CENTER
                    AlertDialog.Builder(ivPreview.context).setView(dialogLayout).show()
                }

                override fun onFailure(e: Exception) {
                    logger.e("Failed to crop image.")
                }
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instaViewModel.items.observe(viewLifecycleOwner, Observer { pagedList ->
            logger.d("submitList to Adapter")
            instaAdapter.submitList(pagedList)
        })
        instaViewModel.isAspectRatio.observe(viewLifecycleOwner, Observer {
            if (!binding.ivPreview.isEmpty()) loadPickleMedia(instaViewModel.selectedPickleMedia)
        })
        instaViewModel.initialLoadState.observe(viewLifecycleOwner, Observer {
            logger.d("initialLoadState = $it")
        })
        instaViewModel.dataSourceState.observe(viewLifecycleOwner, Observer {
            logger.d("dataSourceState = $it")
        })

        optionMenuViewModel.clickEvent.observe(viewLifecycleOwner, Observer {
            activity?.let {
                if (!binding.ivPreview.isOffFrame() && instaViewModel.isAspectRatio.value == false) {
                    binding.ivPreview.crop()
                } else if (instaViewModel.isAspectRatio.value == true) {
                    val dialogLayout = layoutInflater.inflate(R.layout.dialog_result, null)
                    val dialogImageView = dialogLayout.findViewById<ImageView>(R.id.iv_image)
                    dialogImageView.setImageURI(instaViewModel.selectedPickleMedia.getUri())
                    dialogImageView.rotation = (instaViewModel.selectedPickleMedia as Image).orientation
                    dialogImageView.scaleType = ImageView.ScaleType.FIT_CENTER
                    AlertDialog.Builder(it).setView(dialogLayout).show()
                }
            }
        })
    }

    private fun loadPickleMedia(pickleMedia: PickleMedia) {
        if (pickleMedia.getType() == PickleMedia.Type.PHOTO) {
            val orientation = (pickleMedia as Image).orientation
            pickleMedia.getUri()?.let {
                if (instaViewModel.isAspectRatio.value == true) binding.ivPreview.setAspectRatio(
                    it,
                    orientation
                )
                else binding.ivPreview.setCropScale(it, orientation)
            }
        }
        // TODO : VIDEO 에 대한 처리
    }

    override fun onItemClick(view: View?, pickleMedia: PickleMedia) {
        instaViewModel.setSelected(pickleMedia)
        loadPickleMedia(pickleMedia)
        view?.let {
            binding.recyclerView.smoothScrollBy(0, it.top)
            binding.previewAppbarLayout.setExpanded(true)
        }
        instaViewModel.selectionManager.toggleItemSelected(pickleMedia.getId())
    }
}
