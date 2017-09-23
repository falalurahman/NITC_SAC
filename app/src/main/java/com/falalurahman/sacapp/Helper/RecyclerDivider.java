package com.falalurahman.sacapp.Helper;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerDivider extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public RecyclerDivider(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
