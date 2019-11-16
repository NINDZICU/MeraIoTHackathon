package ru.sovcombank.iotmerahackathon.adapters

import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.table_item.view.*
import ru.sovcombank.iotmerahackathon.R

class TableItemAdapter(private val context: Context) : RecyclerView.Adapter<TableItemAdapter.TableItemViewHolder>() {


    var list: List<Boolean> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TableItemViewHolder{
        return TableItemViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.table_item, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: TableItemViewHolder, p1: Int) {
        return p0.bind(list[p1])
    }


    inner class TableItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(exist: Boolean) {
            if(exist) itemView.table_item_text.background = ContextCompat.getDrawable(context, R.drawable.android)
        }
    }


}