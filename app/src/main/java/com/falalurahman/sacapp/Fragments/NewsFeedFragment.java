package com.falalurahman.sacapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.falalurahman.sacapp.Helper.RecyclerDivider;
import com.falalurahman.sacapp.JavaBean.NewsFeed;
import com.falalurahman.sacapp.R;
import com.falalurahman.sacapp.ViewHolder.NewsFeedHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewsFeedFragment extends Fragment {

    private static final String NEWSFEED_REF = "NewsFeed";
    private static final int DIVIDER_LENGTH = 40;
    FirebaseRecyclerAdapter<NewsFeed, NewsFeedHolder> mAdapter;
    ShimmerRecyclerView newsFeedListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_activity_newsfeed, container, false);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference newsFeedRef = firebaseDatabase.getReference(NEWSFEED_REF);

        newsFeedListView = contentView.findViewById(R.id.newsfeed_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        newsFeedListView.setLayoutManager(linearLayoutManager);
        RecyclerDivider recyclerDivider = new RecyclerDivider(DIVIDER_LENGTH);
        newsFeedListView.addItemDecoration(recyclerDivider);

        mAdapter =
                new FirebaseRecyclerAdapter<NewsFeed, NewsFeedHolder>(
                        NewsFeed.class,
                        R.layout.row_newsfeed,
                        NewsFeedHolder.class,
                        newsFeedRef) {
                    @Override
                    protected void populateViewHolder(NewsFeedHolder viewHolder, NewsFeed model, int position) {
                        viewHolder.setSACLogo();
                        viewHolder.setDate(model.getTimeStamp());
                        viewHolder.setStatus(model.getStatus());
                        if (model.getImageUrls() != null) {
                            viewHolder.setImages(getContext(), model.getImageUrls());
                        }
                    }

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
                        newsFeedListView.hideShimmerAdapter();
                    }
                };

        newsFeedListView.setAdapter(mAdapter);
        newsFeedListView.showShimmerAdapter();

        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }
}
