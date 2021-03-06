package com.leaderpower.baechelin_owner_android.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.leaderpower.baechelin_owner_android.R;
import com.leaderpower.baechelin_owner_android.adapter.OrderListAdapter;
import com.leaderpower.baechelin_owner_android.model.Order;
import com.leaderpower.baechelin_owner_android.model.OwnerItem;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OrderCompleteFragment extends Fragment {
//    @BindView(R.id.fragment_edit_start_date)
//    EditText startDate;
//    @BindView(R.id.fragment_edit_end_date)
//    EditText endDate;
    @BindView(R.id.fragment_order_complete_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_order_complete_dscrp)
    TextView txtCompleteDscrp;
    @BindView(R.id.fragment_order_complete_progressbar)
    ProgressBar progressBar;

    private OrderListAdapter orderAdapter;
    private View fragView = null;
    private ArrayList<Order> orderList;
    private Calendar calendar = Calendar.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Query dbQuery;
    private ListenerRegistration dbListener;
    private EditText selectedEdit = null;
    private final String strDateFormat = "MM/dd/yy";
    private String oid;
    private OwnerItem owner;

    public OrderCompleteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
        dbListener.remove();
        orderList.clear();
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();

        dbListener = dbQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.d("COMPLETE FRAGMENT", "Listen Failed " + e);
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                    int pos = 0;
                    String id = dc.getDocument().getId();
                    switch (dc.getType()) {
                        case ADDED:
                            Order order = dc.getDocument().toObject(Order.class);
                            order.setId(dc.getDocument().getId());

                            //filter orders that are not complted
                            if (order.getStatus().equals("2" )){
                                order.setFoodOrdered();
                                orderList.add(order);
                                orderAdapter.notifyItemInserted(orderList.size());
                            }


                            break;

                        case REMOVED:
                            int idx = 0;
                            Iterator iterator = orderList.iterator();
                            while (iterator.hasNext()){
                                Order item = (Order) iterator.next();
                                if (item.getId().equals(id)) {
                                    iterator.remove();
                                    orderAdapter.notifyItemRemoved(idx);
                                }
                                idx++;
                            }
                            break;

                        case MODIFIED:
                            for (Order item : orderList){
                                if (item.getId().equals(id)){
                                    Order modifedOrder = dc.getDocument().toObject(Order.class);
                                    modifedOrder.setId(dc.getDocument().getId());
                                    modifedOrder.setFoodOrdered();
                                    orderList.set(pos, modifedOrder);
                                    orderAdapter.notifyItemChanged(pos);
                                    break;
                                }
                                pos++;
                            }

                            break;
                    }
                }


            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragView == null) {
            fragView = inflater.inflate(R.layout.fragment_order_complete, container, false);
            ButterKnife.bind(this, fragView);

//            setDatePicker();

            //receive oid
            oid = getArguments().getString("oid");
            owner = (OwnerItem) getArguments().getSerializable("owner");
            initRecycleView();


            //set DB
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -200);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Log.d("COMPLETE FRAGMENT", sdf.format(cal.getTime()));
            dbQuery = db.collection("owner").document(oid).collection("order")
                    .whereGreaterThan("created_at", sdf.format(cal.getTime())).orderBy("created_at");
        }

        return fragView;
    }

//    private void setDatePicker() {
//        final SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat, Locale.US);
//        startDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectedEdit = startDate;
//                try {
//                    if (selectedEdit.getText().toString() == "") calendar.setTime(new Date());
//                    else calendar.setTime(sdf.parse(selectedEdit.getText().toString()));
//                } catch (Exception e) {
//                    calendar.setTime(new Date());
//                }
//
//                new DatePickerDialog(getContext(), R.style.DatePickerTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });
//
//        endDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectedEdit = endDate;
//                try {
//                    if (selectedEdit.getText().toString() == "") calendar.setTime(new Date());
//                    else calendar.setTime(sdf.parse(selectedEdit.getText().toString()));
//                } catch (Exception e) {
//                    calendar.setTime(new Date());
//                }
//
//                new DatePickerDialog(getContext(), R.style.DatePickerTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });
//    }

//    @OnClick(R.id.fragment_order_complete_search)
//    void onSearchClicked() {
//        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat, Locale.US);
//        SimpleDateFormat toDBFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
//        String strStart = startDate.getText().toString();
//        String strEnd = endDate.getText().toString();
//
//        if (strStart.equals("") || endDate.equals(""))
//            Toast.makeText(getContext(), "날짜를 입력해주세요.", Toast.LENGTH_LONG).show();
//        else {
//            try {
//                Date startDate = sdf.parse(strStart);
//                Date endDate = sdf.parse(strEnd);
//
//                if (endDate.before(startDate)) {
//                    Toast.makeText(getContext(), "옳바르지 않은 입력입니다.", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                progressBar.setVisibility(View.VISIBLE);
//                txtCompleteDscrp.setVisibility(View.GONE);
//
//                db.collection("owner").document(oid).collection("order")
//                        .whereGreaterThanOrEqualTo("created_at", toDBFormat.format(startDate))
//                        .whereLessThanOrEqualTo("created_at", toDBFormat.format(endDate))
//                        .orderBy("created_at", Query.Direction.DESCENDING).get()
//                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                orderList.clear();
//                                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
//                                for (DocumentSnapshot doc : docs) {
//                                    Order order = doc.toObject(Order.class);
//                                    order.setId(doc.getId());
//
//                                    orderList.add(order);
//                                }
//                                orderAdapter.notifyDataSetChanged();
//                                txtCompleteDscrp.setVisibility(View.VISIBLE);
//                                txtCompleteDscrp.setText("총 " + orderList.size() + "건의 주문을 받았습니다.");
//                                progressBar.setVisibility(View.GONE);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressBar.setVisibility(View.GONE);
//                        txtCompleteDscrp.setVisibility(View.VISIBLE);
//                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            } catch (Exception e) {
//                progressBar.setVisibility(View.GONE);
//                txtCompleteDscrp.setVisibility(View.VISIBLE);
//                Log.d("FRAG", e.getMessage());
//            }
//
//        }
//    }

    private void initRecycleView() {
        orderList = new ArrayList<>();

        orderAdapter = new OrderListAdapter(orderList, getActivity(), owner);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderAdapter);

    }

    private void updateLabel() {

        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat, Locale.US);
        selectedEdit.setText(sdf.format(calendar.getTime()));

    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel();
        }
    };
}
