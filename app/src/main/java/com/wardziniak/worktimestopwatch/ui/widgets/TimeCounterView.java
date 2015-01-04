package com.wardziniak.worktimestopwatch.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wardziniak.worktimestopwatch.R;


/**
 * Created by wardziniak on 1/3/15.
 */
public class TimeCounterView extends TextView {

    public final static int UP = 0;
    public final static int DOWN = 1;
    private final static int DEFAULT_DIRECTIO = UP;
    private final static int TICK = 1;

    private boolean isUp = true;
    private boolean normalizedValue = true;

    private boolean mStarted;
    private boolean mRunning;
    private boolean mVisibile;

    private long min = 0l;
    private long max;
    private long startTime = -1;
    private long end;

    private StringBuilder mRecycle = new StringBuilder(8);

    public TimeCounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TimeCounterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray styled = getContext().obtainStyledAttributes(attributeSet, R.styleable.TimeCounterView);
            final int direction = styled.getInt(R.styleable.TimeCounterView_count_direction, DEFAULT_DIRECTIO);
            this.isUp = (direction == UP);
        }
    }

    public void setMin(long min) {
        this.min = min;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public void setCurrentTime(long time) {
        this.startTime = time;
    }

    public void setCounterDirection(int direction) {
        if (direction == UP)
            this.isUp = true;
        else if (direction == DOWN)
            this.isUp = false;
        else
            throw new IllegalArgumentException("Incorrect value. Was " + direction + " should be one of: TimeCounterView.UP, TimeCounterView.DOWN. ");
    }

    private synchronized void updateText(long now) {
        long seconds = now / 1000;
        String text = DateUtils.formatElapsedTime(mRecycle, seconds);
        setText(text);
    }

    public void start() {
        if (max == 0) {
            throw new IllegalArgumentException("Value max wasn't set");
        }
        if (normalizedValue) {
            startTime = min;
            max -= min;
            min = 0;
        }
        this.mStarted = true;
        if (startTime == -1)
            startTime = SystemClock.elapsedRealtime();
        updateRunning();
    }

    public void stop() {
        this.mStarted = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisibile = visibility == VISIBLE;
        updateRunning();
    }

    private void updateRunning() {
        boolean running = mVisibile && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(getCurrentTime());
                mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK), 1000);
            }
            else
                mHandler.removeMessages(TICK);

            mRunning = running;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mRunning) {
                //long elapsedTime = SystemClock.elapsedRealtime() - startTime;
                //long now = isUp ? min+elapsedTime : max-elapsedTime;
                updateText(getCurrentTime());
                sendMessageDelayed(Message.obtain(this, TICK), 1000);
            }
        }
    };

    private long getCurrentTime() {
        long elapsedTime = SystemClock.elapsedRealtime() - startTime;
        if (isUp) {
            long newTime = min+elapsedTime;
            if (newTime > max) {
                newTime = max;
                stop();
            }
                return newTime;
        }
        else {
            long newTime = max-elapsedTime;
            if (newTime < min) {
                newTime = min;
                stop();
            }
            return newTime;
        }
        //return isUp ? min+elapsedTime : max-elapsedTime;
    }
}
