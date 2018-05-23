package com.example.anar.gridlayout;

public class Camera {

    private String Id;
    public String Description;
    private String imageUrl;
    private String Type;

    Camera() {
    }

    public Camera(String id, String description, String imageUrl, String type) {
        this.Id = id;
        this.Description = description;
        this.imageUrl = imageUrl;
        this.Type = type;
    }
    // getters
    public String getId() { return Id; }

    public String getDescription() { return Description; }

    public String getImageUrl() { return imageUrl; }

    public String getType() { return Type; }

    // setters
    public void setId(String id) { Id = id; }

    public void setDescription(String description) { Description = description; }

    public void setImageUrl (String imageUrl) { this.imageUrl = "http://www.seattle.gov/trafficcams/images/" + imageUrl; }

    public void setType(String type) { Type = type; }

}
