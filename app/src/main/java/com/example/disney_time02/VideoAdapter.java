package com.example.disney_time02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final List<YouTubeVideos> youtubeVideoList;
    Activity mainActivity;

    VideoAdapter(List<YouTubeVideos> youtubeVideoList, Movie_Page movie_page) {
        this.youtubeVideoList = youtubeVideoList;
        this.mainActivity = movie_page;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.videoWeb.loadData(youtubeVideoList.get(position).getVideoUrl(), "text/html", "utf-8");
    }

    @Override
    public int getItemCount() {
        return youtubeVideoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        WebView videoWeb;

        @SuppressLint("SetJavaScriptEnabled")
        VideoViewHolder(View itemView) {
            super(itemView);
            videoWeb = itemView.findViewById(R.id.webView);
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient() {
                private View mCustomView;
                private WebChromeClient.CustomViewCallback mCustomViewCallback;
                private int mOriginalOrientation;
                private int mOriginalSystemUiVisibility;

                public Bitmap getDefaultVideoPoster() {
                    if (mainActivity == null) {
                        return null;
                    }
                    return BitmapFactory.decodeResource(mainActivity.getApplicationContext().getResources(), 2130837573);
                }

                public void onHideCustomView() {
                    ((FrameLayout) mainActivity.getWindow().getDecorView()).removeView(this.mCustomView);
                    this.mCustomView = null;
                    mainActivity.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
                    mainActivity.setRequestedOrientation(this.mOriginalOrientation);
                    this.mCustomViewCallback.onCustomViewHidden();
                    this.mCustomViewCallback = null;
                }

                public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
                    if (this.mCustomView != null) {
                        onHideCustomView();
                        return;
                    }
                    this.mCustomView = paramView;
                    this.mOriginalSystemUiVisibility = mainActivity.getWindow().getDecorView().getSystemUiVisibility();
                    this.mOriginalOrientation = mainActivity.getRequestedOrientation();
                    this.mCustomViewCallback = paramCustomViewCallback;
                    ((FrameLayout) mainActivity.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
                    mainActivity.getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            });
        }
    }
}