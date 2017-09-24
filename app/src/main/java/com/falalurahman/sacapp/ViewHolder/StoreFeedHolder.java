package com.falalurahman.sacapp.ViewHolder;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.falalurahman.sacapp.R;
import com.falalurahman.sacapp.SwipeableImageAdapter.SwipeableImageAdapter;

import org.ocpsoft.prettytime.PrettyTime;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoreFeedHolder extends RecyclerView.ViewHolder {

    private final SimpleDraweeView mLogoView;
    private final TextView mUsernameView;
    private final TextView mStatusView;
    private final TextView mDateView;
    private final TextView mPhoneNumberView;
    private final TextView mContactAddressView;
    private final ViewPager mViewPager;
    private final TabLayout mTabLayout;

    public StoreFeedHolder(View itemView) {
        super(itemView);
        mUsernameView = itemView.findViewById(R.id.username_view);
        mStatusView = itemView.findViewById(R.id.status_view);
        mDateView = itemView.findViewById(R.id.date_view);
        mPhoneNumberView = itemView.findViewById(R.id.phoneNumber_view);
        mContactAddressView = itemView.findViewById(R.id.contactAddress_view);
        mViewPager = itemView.findViewById(R.id.imageSlider);
        mTabLayout = itemView.findViewById(R.id.tabDots);
        mLogoView = itemView.findViewById(R.id.logo);
    }

    public void setUsername(String username, String rollNo) {
        mUsernameView.setText(username + " (" + rollNo + ")");
    }

    public void setLogo() {
        mLogoView.setImageURI(Uri.parse("asset:///sac_logo.jpg"));
    }

    public void setStatus(String status) {
        byte[] bytes = status.getBytes(Charset.forName("UTF-8"));
        mStatusView.setText(new String(bytes, Charset.forName("UTF-8")));
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumberView.setText(phoneNumber);
    }

    public void setContactAddress(String contactAddress) {
        byte[] bytes = contactAddress.getBytes(Charset.forName("UTF-8"));
        mContactAddressView.setText(new String(bytes, Charset.forName("UTF-8")));
    }

    public void setDate(Long timestamp) {
        PrettyTime prettyTime = new PrettyTime();
        mDateView.setText(prettyTime.format(new Date(timestamp)));
    }

    public void setImages(Context context, List<String> images) {
        SwipeableImageAdapter mSwipeableImageAdapter = new SwipeableImageAdapter(context, new ArrayList<>(images));

        mViewPager.setVisibility(View.VISIBLE);
        if (images.size() > 1) {
            mTabLayout.setVisibility(View.VISIBLE);
        }
        mViewPager.setAdapter(mSwipeableImageAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

}
