package com.fiap18Mob.clean.view.listServices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.model.CleaningService
import com.fiap18Mob.clean.utils.Status
import com.fiap18Mob.clean.utils.toDateTime
import kotlinx.android.synthetic.main.activity_cleaner_detail.view.*
import kotlinx.android.synthetic.main.services_list_item.view.*
import java.util.*

class ListServicesAdapter(
    val services: List<CleaningService>,
    val clickListener: (CleaningService) -> Unit
) : RecyclerView.Adapter<ListServicesAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.services_list_item, parent, false)
        return ServiceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.bindView(service, clickListener)
    }

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(service: CleaningService,
                     clickListener: (CleaningService) -> Unit) = with(itemView) {
            tvProfCleanerName.text = service.cleanerName
            tvScheduleTime.text = service.scheduledTime.toDateTime()
            tvStatus.text = Status.translateStatus(service.cleaningStatus)
            setOnClickListener { clickListener(service) }
        }
    }
}