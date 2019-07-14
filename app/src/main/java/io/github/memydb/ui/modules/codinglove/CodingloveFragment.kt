package io.github.memydb.ui.modules.codinglove

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import io.github.memydb.R
import io.github.memydb.ui.base.BaseActivity
import io.github.memydb.ui.base.BaseFragment
import io.github.memydb.ui.base.ViewModelFactory
import io.github.memydb.utils.setEndlessOnScrollListener
import kotlinx.android.synthetic.main.fragment_codinglove.*
import javax.inject.Inject

class CodingloveFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var codingloveViewModel: CodingloveViewModel

    private lateinit var codingloveAdapter: FastAdapter<AbstractItem<*>>

    private var savedView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (savedView == null) {
            savedView = inflater.inflate(R.layout.fragment_codinglove, container, false)
        }
        return savedView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? BaseActivity)?.supportActionBar?.title = getString(R.string.thecodinglove_title)
        codingloveViewModel = ViewModelProviders.of(this, viewModelFactory).get(CodingloveViewModel::class.java)
        initView()
    }

    private fun initView() {
        codingloveViewModel.initialize()
        codingloveViewModel.codingloveMemes.observe(viewLifecycleOwner, Observer {
            memesAdapter.setNewList(it)
        })

        codingloveAdapter = FastAdapter.with(memesAdapter)

        codingloveRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = codingloveAdapter
            setEndlessOnScrollListener(15) { codingloveViewModel.downloadNextPage() }
            itemAnimator = null
        }
    }
}