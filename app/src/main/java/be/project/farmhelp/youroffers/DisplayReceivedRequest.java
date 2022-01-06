package be.project.farmhelp.youroffers;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.project.farmhelp.R;
import be.project.farmhelp.authentication.SessionManager;
import be.project.farmhelp.dataholders.ServiceRequests;

public class DisplayReceivedRequest extends AppCompatActivity {
    private String mobNumber;
    SessionManager currentUser;

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    LinearLayout linearLayoutNoRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_received_request);

        currentUser = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = currentUser.getUsersDetailsFromSession();
        mobNumber = userDetails.get(SessionManager.KEY_MOBILE);

        linearLayoutNoRequest = findViewById(R.id.no_request_layout);

        recyclerView = findViewById(R.id.recyclerView_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext());
        recyclerView.setAdapter(recyclerAdapter);

        fetchServiceRequestFromDB();
    }

    public void fetchServiceRequestFromDB() {

        List<ServiceRequests> serviceRequestsList = new ArrayList<>();
        final Query checkUser = FirebaseDatabase.getInstance().getReference("Users/" + mobNumber + "/requests");

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0) {
                    linearLayoutNoRequest.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutNoRequest.setVisibility(View.INVISIBLE);
                }

                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    ServiceRequests post = postSnapShot.getValue(ServiceRequests.class);
                    serviceRequestsList.add(post);
                }
                recyclerAdapter.setServiceList(serviceRequestsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DisplayReceivedRequest.this, "Sorry!! Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}