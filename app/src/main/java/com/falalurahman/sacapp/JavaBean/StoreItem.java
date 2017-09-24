package com.falalurahman.sacapp.JavaBean;

import java.util.List;

public class StoreItem {
    private String Username;
    private String RollNo;
    private Long TimeStamp;
    private String Message;
    private String PhoneNumber;
    private String ContactAddress;
    private List<String> ImageUrls;

    public StoreItem() {
    }

    public StoreItem(String username, String rollNo, Long timeStamp, String message, String phoneNumber, String contactAddress, List<String> imageUrls) {
        Username = username;
        RollNo = rollNo;
        TimeStamp = timeStamp;
        Message = message;
        PhoneNumber = phoneNumber;
        ContactAddress = contactAddress;
        ImageUrls = imageUrls;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getRollNo() {
        return RollNo;
    }

    public void setRollNo(String rollNo) {
        RollNo = rollNo;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getContactAddress() {
        return ContactAddress;
    }

    public void setContactAddress(String contactAddress) {
        ContactAddress = contactAddress;
    }

    public List<String> getImageUrls() {
        return ImageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        ImageUrls = imageUrls;
    }
}
