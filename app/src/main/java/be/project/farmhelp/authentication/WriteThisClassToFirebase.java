package be.project.farmhelp.authentication;

public class WriteThisClassToFirebase {

    private String name, mobNo, password;
    private double latitude, longitude;
    private boolean isServiceProvider;

    public WriteThisClassToFirebase(String name, String mobNo, String password, double latitude, double longitude, boolean isServiceProvider) {
        this.name = name;
        this.mobNo = mobNo;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isServiceProvider=isServiceProvider;
    }


    public boolean isServiceProvider() {
        return isServiceProvider;
    }

    public void setServiceProvider(boolean serviceProvider) {
        isServiceProvider = serviceProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
