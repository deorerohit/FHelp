package be.project.farmhelp.dataholders;

public class WriteThisClassToFirebase {

    private String name, mobNo, password;
    private double latitude, longitude;
    private boolean isServiceProvider;
    private ServiceDataInFirebase serviceDataInFirebase;

    public WriteThisClassToFirebase(String name, String mobNo, String password, double latitude, double longitude) {
        this.name = name;
        this.mobNo = mobNo;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isServiceProvider = false;

        serviceDataInFirebase = new ServiceDataInFirebase("User", "", "", "");
    }

    public ServiceDataInFirebase getAddServiceClassToFirebase() {
        return serviceDataInFirebase;
    }

    public void setAddServiceClassToFirebase(ServiceDataInFirebase serviceDataInFirebase) {
        this.serviceDataInFirebase = serviceDataInFirebase;
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
