package io.github.memydb.ui.modules.anonimowe

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
import kotlinx.android.synthetic.main.fragment_anonimowe.*
import javax.inject.Inject

class AnonimoweFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var anonimoweViewModel: AnonimoweViewModel

    private lateinit var anonimoweAdapter: FastAdapter<AbstractItem<*>>

    private var savedView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (savedView == null) {
            savedView = inflater.inflate(R.layout.fragment_anonimowe, container, false)
        }
        return savedView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? BaseActivity)?.supportActionBar?.title = getString(R.string.anonimowe_title)
        anonimoweViewModel = ViewModelProviders.of(this, viewModelFactory).get(AnonimoweViewModel::class.java)
        initView()
    }

    private fun initView() {
        anonimoweViewModel.initialize()
        anonimoweViewModel.anonimoweMemes.observe(viewLifecycleOwner, Observer {
            memesAdapter.setNewList(it)
        })

        anonimoweAdapter = FastAdapter.with(memesAdapter)

        anonimoweRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = anonimoweAdapter
            setEndlessOnScrollListener(15) { anonimoweViewModel.downloadNextPage() }
            itemAnimator = null
        }
    }
}