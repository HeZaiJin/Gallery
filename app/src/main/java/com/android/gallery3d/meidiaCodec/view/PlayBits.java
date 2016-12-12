package com.android.gallery3d.meidiaCodec.view;

import android.graphics.Bitmap;

import com.android.gallery3d.glrenderer.GLCanvas;
import com.android.gallery3d.meidiaCodec.anim.BitmapStream;
import com.android.gallery3d.meidiaCodec.anim.MediaStream;

/**
 * Created by linusyang on 16-12-8.
 */

public class PlayBits implements VIPlayControl ,StateIs{

    public interface OnNotifyChangeListener {
        void doInvalidate();
        void notifyCompletion();
    }

    protected int mWidth;
    protected int mHeight;
    private MediaStream mCurrentMediaStream;

    private OnNotifyChangeListener mOnNotifyChangeListener;


    public void setOnNotifyChangeListener(OnNotifyChangeListener listener) {
        this.mOnNotifyChangeListener = listener;
    }

    public void setResolution(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public  void onDraw(GLCanvas canvas) {
        boolean requestRender = false;
        if (mCurrentMediaStream != null) {
            requestRender |= mCurrentMediaStream.calculate(BitmapStream.getCurrentTime());
            canvas.save(GLCanvas.SAVE_FLAG_ALPHA | GLCanvas.SAVE_FLAG_MATRIX);
            mCurrentMediaStream.apply(canvas);
            canvas.restore();
            if(mCurrentMediaStream.isCompletion()) {
                mCurrentMediaStream.stop();
                mOnNotifyChangeListener.notifyCompletion();
            }
        }

        if (requestRender) mOnNotifyChangeListener.doInvalidate();
    }


    public void prepare(MediaStream mediaStream) {
        this.mCurrentMediaStream = mediaStream;
    }

    @Override
    public void prepare() {
        mCurrentMediaStream.pause();
    }

    @Override
    public void start() {
        mCurrentMediaStream.start();
    }

    @Override
    public void restart() {
        mCurrentMediaStream.restart();
    }

    @Override
    public void pause() {
        mCurrentMediaStream.pause();
    }

    @Override
    public void stop() {
        mCurrentMediaStream.stop();
    }

    @Override
    public void seekTo(long durationT) {
        mCurrentMediaStream.seekTo(durationT);
    }

    @Override
    public int getPlayState() {
        return mCurrentMediaStream.getPlayState();
    }

    @Override
    public long getProgress() {
        return mCurrentMediaStream.getProgress();
    }

    @Override
    public void setDuration(long duration) {
        mCurrentMediaStream.setDuration(duration);
    }

    @Override
    public long getDuration() {
        return mCurrentMediaStream.getDuration();
    }

}