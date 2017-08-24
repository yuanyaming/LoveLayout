package com.yym.lovelayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private LoveLayout mCompound_bride_ll;
    int i = 0;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCompound_bride_ll = (LoveLayout) findViewById(R.id.compound_bride_ll);

        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0x123) {
                    mCompound_bride_ll.addLove();
                }
            }
        };
    }

    public void startView(View v) {
        i = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (i < 150) {
                        i++;
                        Thread.sleep(10);
                        mHandler.sendEmptyMessage(0x123);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
