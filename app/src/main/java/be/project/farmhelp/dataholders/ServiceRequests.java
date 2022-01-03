package be.project.farmhelp.dataholders;

public class ServiceRequests {
    private String name;
    private String number;
    private Double latitude, longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public ServiceRequests(String name, String number, Double latitude, Double longitude) {
        this.name = name;
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ServiceRequests() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
