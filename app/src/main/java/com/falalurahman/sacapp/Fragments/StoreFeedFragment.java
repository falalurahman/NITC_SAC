package com.falalurahman.sacapp.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.falalurahman.sacapp.AddItemActivity;
import com.falalurahman.sacapp.Helper.RecyclerDivider;
import com.falalurahman.sacapp.JavaBean.StoreItem;
import com.falalurahman.sacapp.R;
import com.falalurahman.sacapp.ViewHolder.StoreFeedHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

public class StoreFeedFragment extends Fragment {

    private static final String STOREFEED_REF = "StoreFeed";
    private static final int DIVIDER_LENGTH = 40;
    private static final String SHARED_PREF_PATH = "SACNITC";
    private static final String SHARED_PREF_ROLLNO = "RollNo";
    FirebaseRecyclerAdapter<StoreItem, StoreFeedHolder> mAdapter;
    ShimmerRecyclerView storeFeedListView;
    FloatingActionButton addItemButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_activity_storefeed, container, false);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference storeFeedRef = firebaseDatabase.getReference(STOREFEED_REF);

        storeFeedListView = contentView.findViewById(R.id.storefeed_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        storeFeedListView.setLayoutManager(linearLayoutManager);
        RecyclerDivider recyclerDivider = new RecyclerDivider(DIVIDER_LENGTH);
        storeFeedListView.addItemDecoration(recyclerDivider);

        addItemButton = contentView.findViewById(R.id.addItem);

        mAdapter =
                new FirebaseRecyclerAdapter<StoreItem, StoreFeedHolder>(
                        StoreItem.class,
                        R.layout.row_storefeed,
                        StoreFeedHolder.class,
                        storeFeedRef) {
                    @Override
                    protected void populateViewHolder(StoreFeedHolder viewHolder, StoreItem model, final int position) {
                        viewHolder.setUsername(model.getUsername(), model.getRollNo());
                        viewHolder.setLogo();
                        viewHolder.setDate(model.getTimeStamp());
                        viewHolder.setStatus(model.getMessage());
                        viewHolder.setPhoneNumber(model.getPhoneNumber());
                        viewHolder.setContactAddress(model.getContactAddress());
                        if (model.getImageUrls() != null) {
                            viewHolder.setImages(getContext(), model.getImageUrls());
                        }

                        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                deleteItem(mAdapter.getRef(position), getItem(position));
                                return true;
                            }
                        });
                    }

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
                        storeFeedListView.hideShimmerAdapter();
                    }
                };

        storeFeedListView.setAdapter(mAdapter);
        storeFeedListView.showShimmerAdapter();

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    private void deleteItem(DatabaseReference databaseReference, StoreItem storeItem) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_PATH, MODE_PRIVATE);
        if (storeItem.getRollNo() == sharedPreferences.getString(SHARED_PREF_ROLLNO, null)) {
            showAlertDialog(getContext(), databaseReference);
        }
    }

    private void showAlertDialog(Context context, final DatabaseReference databaseReference) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage("Do you want to delete this item?");
        alertBuilder.setCancelable(true);

        alertBuilder.setPositiveButton(
                "Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        databaseReference.removeValue();
                        dialog.dismiss();
                    }
                });

        alertBuilder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
