package be.project.farmhelp.Authentication;

public class WriteThisClassToFirebase {

    private String name, mobNo, password;

    public WriteThisClassToFirebase(String name, String mobNo, String password) {
        this.name = name;
        this.mobNo = mobNo;
        this.password = password;
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
}
