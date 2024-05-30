package com.capstone.hicare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capstone.hicare.model.Disease
import java.util.ArrayList
import java.util.Locale
import com.capstone.hicare.databinding.ItemDiseaseBinding


class DiseaseAdapter (private val DiseaseList: ArrayList<Disease>): RecyclerView.Adapter<DiseaseAdapter.RecyclerViewHolder>(),Filterable {


    private lateinit var onItemClickCallBack: OnItemClickCallBack
    private var filterDiseaseList: ArrayList<Disease> = DiseaseList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    inner class RecyclerViewHolder(private val binding: ItemDiseaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(guavaDisease: Disease) {
            Glide.with(itemView.context)
                .load(guavaDisease.diseaseImage)
                .apply(RequestOptions().override(300, 300))
                .into(binding.ivItemPicture)
            binding.tvItemName.text = guavaDisease.diseaseName
            binding.tvSubName.text = guavaDisease.diseaseSubName
            itemView.setOnClickListener { onItemClickCallBack.onItemClicked(guavaDisease) }
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(filterDiseaseList[position])

        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = filterDiseaseList.size


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val itemSearch = constraint.toString()
                filterDiseaseList = if (itemSearch.isEmpty()) {
                    DiseaseList
                } else {
                    val List = ArrayList<Disease>()
                    for (item in DiseaseList) {
                        if (item.diseaseName?.toLowerCase(Locale.ROOT)?.contains(
                                itemSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )!!
                        ) {
                            List.add(item)
                        }
                    }
                    List
                }
                var filterResults = FilterResults()
                filterResults.values = filterDiseaseList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, result: FilterResults?) {
                filterDiseaseList = result?.values as ArrayList<Disease>
                notifyDataSetChanged()
            }

        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallback
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: Disease)
    }

}