package com.android.aquagiraffe.rotaryconnect.model;

import android.net.Uri;

public class ImageModel {
    private String name;
    private Uri uri;
    private String path;
    public boolean checked;


    public ImageModel(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
