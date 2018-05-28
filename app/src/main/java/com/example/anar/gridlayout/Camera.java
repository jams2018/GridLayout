package com.example.anar.gridlayout;

public class Camera {

    private double Latitude;
    private double Longitude;
    private String Id;
    public String Description;
    private String imageUrl;
    private String Type;

    Camera() {
    }

    public Camera(double latitude, double longitude,String id, String description, String imageUrl, String type) {
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Id = id;
        this.Description = description;
        this.imageUrl = imageUrl;
        this.Type = type;
    }

    // getters
    public double getLatitude() { return Latitude; }

    public double getLongitude() { return Longitude; }

    public String getId() { return Id; }

    public String getDescription() { return Description; }

    public String getImageUrl() { return imageUrl; }

    public String getType() { return Type; }

    // setters
    public void setLatitude(Double latitude) { Latitude = latitude; }

    public void setLongitude(Double longitude) { Longitude = longitude; }

    public void setId(String id) { Id = id; }

    public void setDescription(String description) { Description = description; }

    public void setImageUrl (String imageUrl) { this.imageUrl = "http://www.seattle.gov/trafficcams/images/" + imageUrl; }

    public void setType(String type) { Type = type; }

}
