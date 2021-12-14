package be.project.farmhelp.dataholders;

public class ServiceDataInFirebase {

    private String service, description, contact, rate;


    public ServiceDataInFirebase(String service, String description, String contact, String rate) {
        this.service = service;
        this.description = description;
        this.contact = contact;
        this.rate = rate;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
