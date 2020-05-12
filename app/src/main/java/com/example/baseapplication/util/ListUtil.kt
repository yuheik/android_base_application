package com.example.baseapplication.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Usage example
 *
 * class SampleAdapter<T> : ListUtil.Adapter<T> {
 *   constructor() : super(R.layout.xxxx)
 *   override fun onBind(itemView: View, data: T) {
 *     itemView.findViewById<TextView>(R.id.item_text1).also {
 *       it.text = data.toString()
 *     }
 *   }
 * }
 * 
 * var adapter = SampleAdapter<Type>()
 *
 * findViewById<RecyclerView>(R.id.recycler_view).let {
 *   it.adapter = adapter
 *   it.layoutManager = LinearLayoutManager(this)
 * }
 *
 * findViewById<Button>(R.id.button).let {
 *   it.setOnClickListener { adapter.add(XXX) } // XXX must be Type
 * }
 *
 **/
class ListUtil {
    abstract class Adapter<T> : RecyclerView.Adapter<ViewHolder<T>> {
        companion object {
            const val VIEW_TYPE_TITLE: Int = 0
            const val VIEW_TYPE_NORMAL: Int = 1
        }

        abstract fun onBind(itemView: View, data: T)

        private var dataSet: ArrayList<T>
        private val list_item_layout_id: Int

        constructor(list_item_layout_id: Int) : super() {
            this.list_item_layout_id = list_item_layout_id
            this.dataSet = ArrayList()
        }

        constructor(list_item_layout_id: Int, initialData: ArrayList<T>) : super() {
            this.list_item_layout_id = list_item_layout_id
            this.dataSet = initialData
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }

        override fun getItemViewType(position: Int): Int {
            return getItemViewType(dataSet[position])
        }

        protected open fun getItemViewType(data: T): Int {
            return VIEW_TYPE_NORMAL
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
            return ViewHolder(
                createItemView(parent, viewType, list_item_layout_id)
            )
        }

        protected open fun createItemView(parent: ViewGroup, viewType: Int, itemViewLayoutId: Int): View {
            return when (viewType) {
                VIEW_TYPE_TITLE -> {
                    // TODO Suppose switching layout according to the viewType is expected behaviour.
                    LayoutInflater.from(parent.context).inflate(itemViewLayoutId, parent, false)
                }
                else -> {
                    LayoutInflater.from(parent.context).inflate(itemViewLayoutId, parent, false)
                }
            }
        }

        override fun onBindViewHolder(viewHolder: ViewHolder<T>, position: Int) {
            onBind(viewHolder.itemView, dataSet[position])
        }

        fun addData(value: T) {
            LogUtil.traceFunc(value.toString())
            this.dataSet.add(value)
            this.notifyItemInserted(dataSet.size)
        }

        fun setData(dataSet: ArrayList<T>) {
            this.dataSet = dataSet
            this.notifyDataSetChanged()
        }
    }

    // TODO Not clear why this needs to be extended
    class ViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView)
}
