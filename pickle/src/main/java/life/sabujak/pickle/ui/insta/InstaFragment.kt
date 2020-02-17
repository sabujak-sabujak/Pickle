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
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.databinding.FragmentInstaBinding
import life.sabujak.pickle.ui.common.OptionMenuViewModel
import life.sabujak.pickle.ui.insta.internal.CropDataListener
import life.sabujak.pickle.util.Logger
import life.sabujak.pickle.util.ext.showToast

class InstaFragment : Fragment(), OnInstaEventListener {
    val logger = Logger.getLogger(this.javaClass.simpleName)

    lateinit var binding: FragmentInstaBinding
    private lateinit var instaViewModel: InstaViewModel
    private lateinit var optionMenuViewModel: OptionMenuViewModel
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
            ivPreview.setOnCropLogListener(object : CropDataListener {
                override fun onActionUp() {
                    val cropData = ivPreview.getCropData()
                    val selectedMedia = instaViewModel.selectedPickleMedia
                    logger.d("onActionUp() ${cropData}")
                    instaViewModel.selectionManager.updateCropData(selectedMedia.getId(), cropData)
                }
            })
            ivPreview.addOnCropListener(object : OnCropListener {
                override fun onSuccess(bitmap: Bitmap) {
                    val dialogLayout = layoutInflater.inflate(R.layout.dialog_result, null)
                    var dialogImageView = dialogLayout.findViewById<ImageView>(R.id.iv_image)
                    context?.let {
                        Glide.with(dialogLayout.context).load(bitmap).fitCenter()
                            .into(dialogImageView)
                        val alertDialog = AlertDialog.Builder(it)
                        alertDialog.setOnDismissListener {
                            logger.d("release bitmap")
                            dialogImageView.setImageDrawable(null)
                            dialogImageView = null
                        }
                        alertDialog.setView(dialogLayout).show()
                    }
                }

                override fun onFailure(e: Exception) {
                    logger.e("Failed to crop image. msg : ${e.message}")
                }
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instaViewModel.items.observe(viewLifecycleOwner, Observer { pagedList ->
            logger.d("submitList to Adapter")
            instaAdapter.submitList(pagedList, object : Runnable {
                override fun run() {
                    pagedList?.let {
                        if (it.size != 0) onItemClick(null, it.get(0)!!)
                    }
                }
            })
        })
        instaViewModel.isAspectRatio.observe(viewLifecycleOwner, Observer {
            if (!binding.ivPreview.isEmpty()) {
                if (it) binding.ivPreview.setAspectRatio() else binding.ivPreview.setCropScale()
            }
        })
        instaViewModel.isMultipleSelect.observe(viewLifecycleOwner, Observer {
            instaViewModel.selectionManager.clear()
            if (it && !binding.ivPreview.isEmpty()) {
                binding.ivPreview.getCropData()?.let {
                    instaViewModel.selectionManager.setMultiCropData(cropData = it)
                }
            }
        })
        instaViewModel.initialLoadState.observe(viewLifecycleOwner, Observer {
            logger.d("initialLoadState = $it")
        })
        instaViewModel.dataSourceState.observe(viewLifecycleOwner, Observer {
            logger.d("dataSourceState = $it")
        })

        optionMenuViewModel.clickEvent.observe(viewLifecycleOwner, Observer {
            activity?.let {
                if (instaViewModel.selectedPickleMedia.getType() != PickleMedia.Type.PHOTO) {
                    showToast("video is not supported now")
                } else if (binding.ivPreview.isOffFrame()) showToast("Image is off of the frame.")
                else {
                    if (instaViewModel.isAspectRatio.value == false) binding.ivPreview.crop()
                    else {
                        val dialogLayout = layoutInflater.inflate(R.layout.dialog_result, null)
                        val dialogImageView = dialogLayout.findViewById<ImageView>(R.id.iv_image)
                        Glide.with(it).load(instaViewModel.selectedPickleMedia.getUri()).fitCenter()
                            .into(dialogImageView)
                        AlertDialog.Builder(it).setView(dialogLayout).show()
                    }
                }
            }
        })
    }

    private fun loadPickleMedia(pickleMedia: PickleMedia) {
        if (pickleMedia.getType() == PickleMedia.Type.PHOTO) {
            binding.ivPreview.setPickleMedia(pickleMedia)
            if (!binding.ivPreview.isEmpty()) {
                if (instaViewModel.isAspectRatio.value == true) binding.ivPreview.setAspectRatio() else binding.ivPreview.setCropScale()
            }
        }
//        TODO("VIDEO 에 대한 처리")
    }

    override fun onItemClick(view: View?, pickleMedia: PickleMedia) {
        instaViewModel.setSelected(pickleMedia)
        instaViewModel.selectionManager.itemClick(
            pickleMedia.getId(),
            binding.ivPreview.getCropData()
        )
        if (instaViewModel.selectedPickleMedia.getType() != PickleMedia.Type.PHOTO) {
            showToast("video is not supported now")
            binding.ivPreview.clear()
            return
        }
        loadPickleMedia(pickleMedia)
        view?.let {
            binding.recyclerView.smoothScrollBy(0, it.top)
            binding.previewAppbarLayout.setExpanded(true)
        }
    }
}
