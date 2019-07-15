package com.dicoding.picodiploma.finalsubmission;

import android.database.Cursor;

public interface LoadCallback {
    void preExecute();
    void postExecute(Cursor cursor);
}
