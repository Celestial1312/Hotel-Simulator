package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Struct;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Area {

    @JsonProperty("AreaType")
    private String areaType;

    @JsonProperty("Capacity")
    private Integer capacity;

    @JsonProperty("Classification")
    private String classification;

    private int x;
    private int y;
    private int width;
    private int height;

    public Area() {

    }

    public String getAreaType() {
        return areaType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getClassification() {
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
