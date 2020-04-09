package com.example.instagram.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.instagram.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class UniversalImageLoader {

    private static int defaultImage = R.drawable.ic_profile;
    Context context;

    public UniversalImageLoader(Context context) {
        this.context = context;
    }

    ImageLoaderConfiguration config;

    public ImageLoaderConfiguration getConfig() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new SimpleBitmapDisplayer())
                .displayer(new FadeInBitmapDisplayer(400))
                .build();
        return new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100*1024*1024).build();
    }

    public static void setImage(String imgURL, ImageView imgView, final ProgressBar mProgressBar, String extension) {

        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(imgURL, imgView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (mProgressBar != null)
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (mProgressBar != null)
                {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                if (mProgressBar != null)
                {
                    mProgressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (mProgressBar != null)
                {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}
