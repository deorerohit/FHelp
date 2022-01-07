package be.project.farmhelp.yoursendrequest;

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

public class DisplaySendRequest extends AppCompatActivity {
    private String mobNumber;
    SessionManager currentUser;

    private RecyclerView recyclerView;
    private RecyclerAdapterSend recyclerAdapterSend;
    LinearLayout linearLayoutNoRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_send_request);

        currentUser = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = currentUser.getUsersDetailsFromSession();
        mobNumber = userDetails.get(SessionManager.KEY_MOBILE);

        linearLayoutNoRequest = findViewById(R.id.no_request_layout);

        recyclerView = findViewById(R.id.recyclerView_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerAdapterSend = new RecyclerAdapterSend(getApplicationContext());
        recyclerView.setAdapter(recyclerAdapterSend);

        getSendRequestsDetails();

    }

    public void getSendRequestsDetails() {
        List<ServiceRequests> sendServiceList = new ArrayList<>();
        final Query sendRequestQuery = FirebaseDatabase.getInstance().getReference("Users");

        sendRequestQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(mobNumber+"/sendRequests").getChildrenCount() == 0) {
                    linearLayoutNoRequest.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutNoRequest.setVisibility(View.INVISIBLE);
                }

                DataSnapshot sendRequestsSnapshot = snapshot.child(mobNumber + "/sendRequests");

                for (DataSnapshot postSnapShot : sendRequestsSnapshot.getChildren()) {
                    String currentPost = postSnapShot.getValue(String.class);

                    String name = snapshot.child(currentPost + "/name").getValue(String.class);
                    Double latitude = snapshot.child(currentPost + "/latitude").getValue(Double.class);
                    Double longitude = snapshot.child(currentPost + "/longitude").getValue(Double.class);
                    String number = snapshot.child(currentPost + "/mobNo").getValue(String.class);
                    String rate = snapshot.child(currentPost + "/addServiceClassToFirebase/rate").getValue(String.class) + "  ₹";

                    ServiceRequests serviceRequests = new ServiceRequests(name, number, latitude, longitude);
                    serviceRequests.setRate(rate);

                    sendServiceList.add(serviceRequests);
                }
                recyclerAdapterSend.setSendServiceList(sendServiceList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DisplaySendRequest.this, "Sorry!! Try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
}