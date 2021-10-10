package com.example.beans;

public class CallbackInfo {

    private String uri;
    private int frequency;

    public CallbackInfo() {
    }

    public CallbackInfo(String uri, int frequency) {
        this.uri = uri;
        this.frequency = frequency;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "CallbackInfo{" +
                "uri='" + uri + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
