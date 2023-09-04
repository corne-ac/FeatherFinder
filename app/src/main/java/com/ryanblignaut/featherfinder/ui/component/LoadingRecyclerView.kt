package com.ryanblignaut.featherfinder.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.ryanblignaut.featherfinder.R


class LoadingRecyclerView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private val loader: CircularProgressIndicator
    private val noObservationsText: TextView
    private val recyclerView: RecyclerView
    private val loadingTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.component_loading_recycler_view, this, true)
        loader = findViewById(R.id.loader)
        noObservationsText = findViewById(R.id.no_items_found)
        recyclerView = findViewById(R.id.birding_recycler_view)
        loadingTextView = findViewById(R.id.loading_text)

        // Customize attributes if needed
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.LoadingRecyclerView)
        val emptyText = attributes.getString(R.styleable.LoadingRecyclerView_emptyText)
        if (!emptyText.isNullOrEmpty()) {
            noObservationsText.text = emptyText
        }
        val loadingText = attributes.getString(R.styleable.LoadingRecyclerView_loadingText)
        if (!loadingText.isNullOrEmpty()) {
            loadingTextView.text = loadingText
        }
        val listItemLayout = attributes.getString(R.styleable.LoadingRecyclerView_listItemLayout)
        if (listItemLayout != null) {
            recyclerView.tag = "tools:listitem=$listItemLayout";
        }

        val contextAttr = attributes.getString(R.styleable.LoadingRecyclerView_context)
        if (contextAttr != null) {
            recyclerView.tag = "tools:context=$context";
        }

        attributes.recycle()
    }

    // Add methods to show/hide loading indicator, set adapter, etc.
    fun showLoading() {
        loader.visibility = View.VISIBLE
        loadingTextView.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loader.visibility = View.GONE
        loadingTextView.visibility = View.GONE
    }

    fun showEmptyText() {
        hideLoading()
        noObservationsText.visibility = View.VISIBLE
    }


    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        hideLoading()
        recyclerView.adapter = adapter
    }

}
