package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Area {

    private int x;
    private int y;
    private int width;
    private int height;
    private int classification;
    private boolean isClaimed = false;

    private Guest guest;
    private Tile tile;

    public Area() {

    }

    public String getAreaType() {
        return areaType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public int getClassification() {
        return classification;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getIsClaimed() {
        return isClaimed;
    }

    public Guest getGuest() {
        return guest;
    }

    public Tile getTile() {
        return tile;
    }

    public boolean setIsClaimed(boolean isClaimed) {
        return this.isClaimed = isClaimed;
    }

    public Guest setGuest(Guest guest) {
        return this.guest = guest;
    }

    @JsonProperty("AreaType")
    private String areaType;

    @JsonProperty("Capacity")
    private Integer capacity;

    @JsonProperty("Classification")
    public void setClassification(String classification) {
        String [] parts = classification.split(" ");
        this.classification = Integer.parseInt(parts[0].trim());
    }

    @JsonProperty("Position")
    public void setPosition(String position) {
        String[] parts = position.split(",");
        this.x = Integer.parseInt(parts[0].trim());
        this.y = Integer.parseInt(parts[1].trim());
    }

    @JsonProperty("Dimension")
    public void setDimension(String dimension) {
        String[] parts = dimension.split(",");
        this.width = Integer.parseInt(parts[0].trim());
        this.height = Integer.parseInt(parts[1].trim());
    }
}
