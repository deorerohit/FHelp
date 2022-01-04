package be.project.farmhelp.youroffers;

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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.DataHolderClass> {

    private List<ServiceRequests> servciceRequestList = new ArrayList<>();

    private Context mainActivity;

    public RecyclerAdapter(Context mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerAdapter.DataHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_element_recycler_layout, parent, false);
        return new DataHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.DataHolderClass holder, int position) {


        ServiceRequests serviceRequests = servciceRequestList.get(position);

        Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(serviceRequests.getLatitude(), serviceRequests.getLongitude(), 1);
            Address obj = addresses.get(0);
            String addressLine = obj.getAddressLine(0);
            holder.textv_recycler_name.setText(serviceRequests.getName());
            holder.textv_recycler_number.setText(serviceRequests.getNumber());
            holder.textv_recycler_location.setText(addressLine);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return servciceRequestList.size();
    }

    public void setServiceList(List<ServiceRequests> articleList) {
        this.servciceRequestList = articleList;
        notifyDataSetChanged();
    }


    public class DataHolderClass extends RecyclerView.ViewHolder {

        private TextView textv_recycler_name;
        private TextView textv_recycler_number;
        private TextView textv_recycler_location;


        public DataHolderClass(@NonNull View itemView) {
            super(itemView);

            textv_recycler_name = itemView.findViewById(R.id.recycler_name);
            textv_recycler_number = itemView.findViewById(R.id.recycler_number);
            textv_recycler_location = itemView.findViewById(R.id.recycler_location);
        }
    }
}
