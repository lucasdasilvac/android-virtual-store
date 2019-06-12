package lucasdasilvac.dev.androidvirtualstore.Model;

public class User {
    private String name;
    private String password;
    private String phone;
    private String isstaff;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        isstaff = "false";
    }

    public String getIsstaff() {
        return isstaff;
    }

    public void setIsstaff(String isstaff) {
        this.isstaff = isstaff;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
