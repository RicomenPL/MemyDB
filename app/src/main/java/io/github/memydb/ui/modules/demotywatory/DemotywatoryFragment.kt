package io.github.memydb.ui.modules.demotywatory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import io.github.memydb.R
import io.github.memydb.data.api.ApiResponse
import io.github.memydb.data.pojos.contents.Content
import io.github.memydb.data.pojos.contents.ImageContent
import io.github.memydb.ui.base.BaseActivity
import io.github.memydb.ui.base.BaseFragment
import io.github.memydb.ui.base.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_demotywatory.*
import javax.inject.Inject

class DemotywatoryFragment : BaseFragment() {

    @Inject
    lateinit var viewmodelFactory: ViewModelFactory

    private lateinit var demotywatoryViewModel: DemotywatoryViewModel

    private lateinit var demotAdapter: FastAdapter<DemotywatoryItem>

    private lateinit var itemAdapter: ItemAdapter<DemotywatoryItem>

    companion object {
        fun newInstance() = DemotywatoryFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_demotywatory, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? BaseActivity)?.supportActionBar?.title = getString(R.string.demotywatory_title)
        demotywatoryViewModel = ViewModelProviders.of(this, viewmodelFactory)
            .get(DemotywatoryViewModel::class.java)

        initView()
        initObserves()
    }

    private fun initView() {
        itemAdapter = ItemAdapter.items()
        demotAdapter = FastAdapter.with(itemAdapter)
        demotRecyler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = demotAdapter
            layoutManager?.let {
                addOnScrollListener(object : EndlessRecyclerOnScrollListener(it, 4) {
                    override fun onLoadMore(currentPage: Int) {
                        demotywatoryViewModel.downloadPage(currentPage)
                    }
                }.apply { resetPageCount(1) })
            }
        }
    }

    private fun initObserves() {
        demotywatoryViewModel.demotywatoryPage.observe(this, Observer {
            if (it is ApiResponse.SuccessApiResponse) {
                val imageItems = it.value.memes.filter { meme -> meme.content.contentType == Content.Type.IMAGE }
                    .map { meme -> DemotywatoryItem(meme.content as ImageContent) }

                itemAdapter.add(itemAdapter.itemList.size(), imageItems)
            } else if (it is ApiResponse.ErrorApiResponse) {
                showMessage(it.error.localizedMessage)
            }
        })
    }
}