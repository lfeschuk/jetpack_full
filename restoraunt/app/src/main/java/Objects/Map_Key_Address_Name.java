package Objects;

public class Map_Key_Address_Name {
    String name = "";
    String phone = "";

    public Map_Key_Address_Name(String name, String phone) {
        this.name = name;
        this.phone = phone;

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + name.hashCode() ;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        Map_Key_Address_Name arg =   (Map_Key_Address_Name)obj;
        return ( arg.getPhone().equals(phone) );
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


}
