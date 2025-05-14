package com.example.travelapp.Domain;

import java.io.Serializable;

public class Booking implements Serializable {
    private String tourName;
    private String address;
    private String date;
    private String guestName;
    private int guestNumber;
    private String phone;
    private String email;
    private int total;
    private String pic;
    private int price;
    private int bed;
    private String description;
    private String distance;
    private String duration;
    private int id;
    private double score;
    private String timeTour;
    private String tourGuideName;
    private String tourGuidePhone;
    private String tourGuidePic;

    public Booking() {
    }

    public Booking(String tourName, String address, String date, String guestName, int guestNumber,
                   String phone, String email, int total, String pic, int price, int bed,
                   String description, String distance, String duration, int id, double score,
                   String timeTour, String tourGuideName, String tourGuidePhone, String tourGuidePic) {
        this.tourName = tourName;
        this.address = address;
        this.date = date;
        this.guestName = guestName;
        this.guestNumber = guestNumber;
        this.phone = phone;
        this.email = email;
        this.total = total;
        this.pic = pic;
        this.price = price;
        this.bed = bed;
        this.description = description;
        this.distance = distance;
        this.duration = duration;
        this.id = id;
        this.score = score;
        this.timeTour = timeTour;
        this.tourGuideName = tourGuideName;
        this.tourGuidePhone = tourGuidePhone;
        this.tourGuidePic = tourGuidePic;
    }

    // Getters
    public String getTourName() {
        return tourName;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getGuestName() {
        return guestName;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getTotal() {
        return total;
    }

    public String getPic() {
        return pic;
    }

    public int getPrice() {
        return price;
    }

    public int getBed() {
        return bed;
    }

    public String getDescription() {
        return description;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public int getId() {
        return id;
    }

    public double getScore() {
        return score;
    }

    public String getTimeTour() {
        return timeTour;
    }

    public String getTourGuideName() {
        return tourGuideName;
    }

    public String getTourGuidePhone() {
        return tourGuidePhone;
    }

    public String getTourGuidePic() {
        return tourGuidePic;
    }

    // Setters
    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public void setGuestNumber(int guestNumber) {
        this.guestNumber = guestNumber;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setTimeTour(String timeTour) {
        this.timeTour = timeTour;
    }

    public void setTourGuideName(String tourGuideName) {
        this.tourGuideName = tourGuideName;
    }

    public void setTourGuidePhone(String tourGuidePhone) {
        this.tourGuidePhone = tourGuidePhone;
    }

    public void setTourGuidePic(String tourGuidePic) {
        this.tourGuidePic = tourGuidePic;
    }
}