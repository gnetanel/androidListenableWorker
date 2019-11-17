package com.example.asynctasktest;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class CallbackWorker extends ListenableWorker {
    private static final String TAG = CallbackWorker.class.getSimpleName();

    public CallbackWorker(Context context, WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            Log.i(TAG, "getFuture called...");
            Callback callback = new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i(TAG, "onFailure from worker has called...");
                    completer.set(Result.failure());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.i(TAG, "onResponse from worker is called with response = " + response);
                    completer.set(Result.success());
                    try {
                        Thread.sleep(1000);
                        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(CallbackWorker.class).build();
                        Log.i(TAG, "=== Enqueue the work again...");
                        WorkManager mWorkManager = WorkManager.getInstance();
                        mWorkManager.enqueue(mRequest);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://www.google.com")
                    .build();
            client.newCall(request).enqueue(callback);
            return callback;
        });
    }
}





//    void doGetRequestEn(String url) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.i(TAG, "onFailure");
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                Log.i(TAG, "onResponse callback is !!! " + response);
//
//            }
//        });
//    }