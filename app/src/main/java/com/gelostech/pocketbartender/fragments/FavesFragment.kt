package com.gelostech.pocketbartender.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gelostech.pocketbartender.R
import com.gelostech.pocketbartender.activities.CocktailActivity
import com.gelostech.pocketbartender.activities.MainActivity
import com.gelostech.pocketbartender.adapters.FavesAdapter
import com.gelostech.pocketbartender.commoners.BartenderUtil
import com.gelostech.pocketbartender.commoners.DatabaseHelper
import com.gelostech.pocketbartender.models.HomeModel
import com.gelostech.pocketbartender.utils.RecyclerFormatter
import com.gelostech.pocketbartender.utils.setFont
import kotlinx.android.synthetic.main.fragment_faves.*
import kotlinx.android.synthetic.main.fragment_faves.view.*
import com.gelostech.pocketbartender.utils.snack
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

/**
 * A simple [Fragment] subclass.
 */
class FavesFragment : Fragment(), FavesAdapter.OnItemClickListener {
    private var favesAdapter: FavesAdapter? = null
    private var faves: MutableList<HomeModel>? = null
    private var db: DatabaseHelper? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_faves, container, false)
        setHasOptionsMenu(false)

        db = DatabaseHelper(activity)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadFaves()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews(v: View) {
        setHasOptionsMenu(true)
        (activity as MainActivity).setSupportActionBar(v.favesToolbar)
        (activity as MainActivity).supportActionBar?.title = null
        v.favesToolbarTitle.setFont(BartenderUtil.FONT)
        v.favesToolbarTitle.text = getString(R.string.faves)

        v.favesRv.setHasFixedSize(true)
        v.favesRv.layoutManager = LinearLayoutManager(context)
        v.favesRv.addItemDecoration(RecyclerFormatter.SimpleDividerItemDecoration(activity!!))

        faves = mutableListOf()

    }

    private fun loadFaves() {
        if (faves!!.size > 0) faves!!.clear()
        faves = db!!.fetchFaves()

        if (faves!!.size == 0) {
            view!!.favesLoading.visibility = View.GONE
            view!!.favesNoContent.visibility = View.VISIBLE
            if (view!!.favesRv.isShown) favesRv!!.visibility = View.GONE

        } else {
            view!!.favesNoContent?.visibility = View.GONE

            favesAdapter = FavesAdapter(activity, faves, this)
            view!!.favesRv.adapter = favesAdapter
            view!!.favesRv.visibility = View.VISIBLE
            view!!.favesLoading.visibility = View.GONE

        }

    }

    override fun onItemClick(model: HomeModel, viewID: Int, position: Int) {
        when (viewID) {
            0 -> {
                activity!!.alert("Remove ${model.name} from favorites?") {
                    yesButton {
                        db!!.deleteRow(model.id.toString(), DatabaseHelper.FAVES_TABLE)
                        BartenderUtil.removeFaveCocktail(activity!!, model.id.toString())
                        view?.snack("${model.name} removed from favorites"){}
                        favesAdapter!!.removeFave(position)

                        if(faves!!.size == 0) {
                            view!!.favesNoContent.visibility = View.VISIBLE
                            if (view!!.favesRv.isShown) favesRv!!.visibility = View.GONE
                        }
                    }
                }.show()
            }

            1 -> {
                model.cocktailThumb = null

                val intent = Intent(activity, CocktailActivity::class.java)
                intent.putExtra("cocktail", model)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.enter_b, R.anim.exit_a)
            }

            else -> {
            }
        }
    }

    override fun onResume() {
        loadFaves()
        super.onResume()
    }
}
