package com.wind.main.network.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by xiaoxiao on 2017/12/22.
 */

public class BillboardAndOrder extends BillboardInfo {

    Bitmap bitmap;
    ImageView imageView;
    BillboardInfo billboardInfo;
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public BillboardInfo getBillboardInfo() {
        return billboardInfo;
    }

    public void setBillboardInfo(BillboardInfo billboardInfo) {
        this.billboardInfo = billboardInfo;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
