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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
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
import es.dmoral.toasty.Toasty;

public class DisplayReceivedRequest extends AppCompatActivity {
    private String mobNumber;
    SessionManager currentUser;

    private RecyclerView recyclerView;
    private RecyclerAdapterReceived recyclerAdapterReceived;
    LinearLayout linearLayoutNoRequest;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_received_request);

        currentUser = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = currentUser.getUsersDetailsFromSession();
        mobNumber = userDetails.get(SessionManager.KEY_MOBILE);

        linearLayoutNoRequest = findViewById(R.id.no_request_layout);
        swipeRefreshLayout = findViewById(R.id.refreshLayout_received);

        recyclerView = findViewById(R.id.recyclerView_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerAdapterReceived = new RecyclerAdapterReceived(getApplicationContext());
        recyclerView.setAdapter(recyclerAdapterReceived);

        fetchServiceRequestFromDB();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchServiceRequestFromDB();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "Swipe Left : Delete Request\nSwipe Right : Accept Request", Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.purple_700))
                .show();

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
                    dbReference.child(recyclerAdapterReceived.servciceRequestList.get(position).getNumber() + "/sendRequests")
                            .child(mobNumber + "/isAccepted")
                            .setValue(-1); //editSend Requests

                    dbReference.child(mobNumber + "/receivedRequests")
                            .child(recyclerAdapterReceived.servciceRequestList.get(position).getNumber())
                            .removeValue(); //delete Received Requests

                    Toasty.error(DisplayReceivedRequest.this, "Request Deleted!", Toast.LENGTH_LONG, true).show();

                } else if (direction == ItemTouchHelper.RIGHT) {
                    dbReference.child(recyclerAdapterReceived.servciceRequestList
                            .get(position)
                            .getNumber() + "/sendRequests")
                            .child(mobNumber + "/isAccepted")
                            .setValue(1); //editSend Requests

                    Toasty.success(DisplayReceivedRequest.this, "Request Accepted!", Toast.LENGTH_LONG, true).show();
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