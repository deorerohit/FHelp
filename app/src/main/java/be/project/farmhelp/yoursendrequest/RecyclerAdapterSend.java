package be.project.farmhelp.yoursendrequest;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import be.project.farmhelp.R;
import be.project.farmhelp.dataholders.ServiceRequests;

public class RecyclerAdapterSend extends RecyclerView.Adapter<RecyclerAdapterSend.DataHolderClass> {

    private List<ServiceRequests> sendRequestList = new ArrayList<>();
    private Context mainActivity;

    public RecyclerAdapterSend(Context mainActivity) {
        this.mainActivity = mainActivity;
    }


    @NonNull
    @Override
    public RecyclerAdapterSend.DataHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_element_recycler_layout, parent, false);
        return new RecyclerAdapterSend.DataHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterSend.DataHolderClass holder, int position) {
        ServiceRequests serviceRequests = sendRequestList.get(position);

        Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(serviceRequests.getLatitude(), serviceRequests.getLongitude(), 1);
            Address obj = addresses.get(0);
            String addressLine = obj.getAddressLine(0);
            String rate_location = serviceRequests.getRate() + "\n" + addressLine;

            holder.textv_recycler_name.setText(serviceRequests.getName());
            holder.textv_recycler_number.setText(serviceRequests.getNumber());
            holder.textv_recycler_location_rate.setText(rate_location);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return sendRequestList.size();
    }

    public void setSendServiceList(List<ServiceRequests> articleList) {
        this.sendRequestList = articleList;
        notifyDataSetChanged();
    }

    public class DataHolderClass extends RecyclerView.ViewHolder {

        private TextView textv_recycler_name;
        private TextView textv_recycler_number;
        private TextView textv_recycler_location_rate;


        public DataHolderClass(@NonNull View itemView) {
            super(itemView);

            textv_recycler_name = itemView.findViewById(R.id.recycler_name);
            textv_recycler_number = itemView.findViewById(R.id.recycler_number);
            textv_recycler_location_rate = itemView.findViewById(R.id.recycler_location);
        }
    }
}
