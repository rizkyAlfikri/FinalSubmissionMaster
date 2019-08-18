
package com.dicoding.picodiploma.finalsubmission.utils;

import android.database.Cursor;

// interface ini digunakan untuk helper ketika melakukan proses asynchronous query data ke database
public interface LoadCallback {
    void preExecute();
    void postExecute(Cursor cursor);
}