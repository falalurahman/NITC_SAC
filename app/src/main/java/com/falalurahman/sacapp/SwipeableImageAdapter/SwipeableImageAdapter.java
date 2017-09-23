package com.falalurahman.sacapp.SwipeableImageAdapter;


import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.falalurahman.sacapp.CircularProgressBarDrawable.CircularProgressBarDrawable;
import com.falalurahman.sacapp.R;
import com.falalurahman.sacapp.ZoomableDraweeView.DoubleTapGestureListener;
import com.falalurahman.sacapp.ZoomableDraweeView.ZoomableDraweeView;

import java.util.ArrayList;

public class SwipeableImageAdapter extends PagerAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mResources;

    public SwipeableImageAdapter(Context context, ArrayList<String> resources) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = resources;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.fragment_image_slider, container, false);

        final ZoomableDraweeView zoomableDraweeView = itemView.findViewById(R.id.zoomableView);
        zoomableDraweeView.setAllowTouchInterceptionWhileZoomed(true);
        zoomableDraweeView.setIsLongpressEnabled(false);
        zoomableDraweeView.setTapListener(new DoubleTapGestureListener(zoomableDraweeView));
        zoomableDraweeView.getHierarchy().setProgressBarImage(new CircularProgressBarDrawable());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mResources.get(position)))
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(zoomableDraweeView.getController())
                .setTapToRetryEnabled(true)
                .build();
        zoomableDraweeView.setController(controller);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
