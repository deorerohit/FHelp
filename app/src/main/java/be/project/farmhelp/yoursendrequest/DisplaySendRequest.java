package be.project.farmhelp.yoursendrequest;

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

public class DisplaySendRequest extends AppCompatActivity {
    private String mobNumber;
    SessionManager currentUser;

    private RecyclerView recyclerView;
    private RecyclerAdapterSend recyclerAdapterSend;
    LinearLayout linearLayoutNoRequest;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_send_request);

        currentUser = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = currentUser.getUsersDetailsFromSession();
        mobNumber = userDetails.get(SessionManager.KEY_MOBILE);

        linearLayoutNoRequest = findViewById(R.id.no_request_layout);
        swipeRefreshLayout = findViewById(R.id.refreshLayout_send);

        recyclerView = findViewById(R.id.recyclerView_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerAdapterSend = new RecyclerAdapterSend(getApplicationContext());
        recyclerView.setAdapter(recyclerAdapterSend);

        getSendRequestsDetails();

        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "Swipe Left : Delete Request", Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.purple_700))
                .show();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSendRequestsDetails();

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
                int position = viewHolder.getLayoutPosition();

                dbReference.child(recyclerAdapterSend.sendRequestList.get(position).getNumber() + "/receivedRequests")
                        .child(mobNumber)
                        .removeValue(); //edit Received Requests

                dbReference.child(mobNumber + "/sendRequests")
                        .child(recyclerAdapterSend.sendRequestList.get(position).getNumber())
                        .removeValue(); //delete Send Requests(not accepted requests)

                Toasty.error(DisplaySendRequest.this, "Request Deleted!", Toast.LENGTH_LONG, true).show();

                getSendRequestsDetails();
            }
        }).attachToRecyclerView(recyclerView);

    }

    public void getSendRequestsDetails() {
        List<ServiceRequests> sendServiceList = new ArrayList<>();
        final Query sendRequestQuery = FirebaseDatabase.getInstance().getReference("Users");

        sendRequestQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(mobNumber + "/sendRequests").getChildrenCount() == 0) {
                    linearLayoutNoRequest.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutNoRequest.setVisibility(View.INVISIBLE);
                }

                DataSnapshot sendRequestsSnapshot = snapshot.child(mobNumber + "/sendRequests");

                for (DataSnapshot postSnapShot : sendRequestsSnapshot.getChildren()) {
                    String currentPost = postSnapShot.getKey();

                    String name = snapshot.child(currentPost + "/name").getValue(String.class);
                    Double latitude = snapshot.child(currentPost + "/latitude").getValue(Double.class);
                    Double longitude = snapshot.child(currentPost + "/longitude").getValue(Double.class);
                    String number = snapshot.child(currentPost + "/mobNo").getValue(String.class);
                    String rate = snapshot.child(currentPost + "/addServiceClassToFirebase/rate").getValue(String.class) + "  ₹";
                    Integer isAccepted = snapshot.child(mobNumber + "/sendRequests/" + currentPost + "/isAccepted").getValue(Integer.class);

                    ServiceRequests serviceRequests = new ServiceRequests(name, number, latitude, longitude);
                    serviceRequests.setRate(rate);
                    serviceRequests.setIsAccepted(isAccepted);

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