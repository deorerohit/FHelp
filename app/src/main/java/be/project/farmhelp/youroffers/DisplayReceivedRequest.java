package be.project.farmhelp.youroffers;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private RecyclerAdapterReceived recyclerAdapterReceived;
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
        recyclerAdapterReceived = new RecyclerAdapterReceived(getApplicationContext());
        recyclerView.setAdapter(recyclerAdapterReceived);

        fetchServiceRequestFromDB();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
                int position = viewHolder.getLayoutPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    dbReference.child(recyclerAdapterReceived.servciceRequestList
                            .get(position)
                            .getNumber() + "/sendRequests")
                            .child(mobNumber + "/isAccepted")
                            .setValue(-1); //editSend Requests

                    dbReference.child(mobNumber + "/receivedRequests")
                            .child(recyclerAdapterReceived.servciceRequestList.get(position).getNumber())
                            .removeValue(); //delete Received Requests

                    Toast.makeText(DisplayReceivedRequest.this, "Request Deleted!", Toast.LENGTH_SHORT).show();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    dbReference.child(recyclerAdapterReceived.servciceRequestList
                            .get(position)
                            .getNumber() + "/sendRequests")
                            .child(mobNumber + "/isAccepted")
                            .setValue(1); //editSend Requests
                    Toast.makeText(DisplayReceivedRequest.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                }
                fetchServiceRequestFromDB();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void fetchServiceRequestFromDB() {

        List<ServiceRequests> serviceRequestsList = new ArrayList<>();
        final Query checkUser = FirebaseDatabase.getInstance().getReference("Users/" + mobNumber + "/receivedRequests");

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
                recyclerAdapterReceived.setServiceList(serviceRequestsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DisplayReceivedRequest.this, "Sorry!! Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}