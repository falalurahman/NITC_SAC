package com.falalurahman.sacapp.JavaBean;

import java.util.List;

public class NewsFeed {
    private Long TimeStamp;
    private String Status;
    private List<String> ImageUrls;

    public NewsFeed() {
    }

    public NewsFeed(Long timeStamp, String status, List<String> imageUrls) {
        TimeStamp = timeStamp;
        Status = status;
        ImageUrls = imageUrls;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<String> getImageUrls() {
        return ImageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        ImageUrls = imageUrls;
    }
}
