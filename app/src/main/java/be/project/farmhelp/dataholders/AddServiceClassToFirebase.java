package be.project.farmhelp.dataholders;

public class AddServiceClassToFirebase {

    private String service, description, contact;
    private int rate;

    public AddServiceClassToFirebase(String service, String description, String contact, int rate) {
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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
