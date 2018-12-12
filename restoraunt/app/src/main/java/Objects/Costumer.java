package Objects;

public class Costumer {
    public String comment = "";
    public String phone = "";
    public String name = "";
    private String city = "";
    private String floor = "";
    private String entrance = "";
    private String building = "";
    private String street = "";
    private String apartment = "";
    private double source_cord_lat;
    private double source_cord_long;
    private String another_phone = "";
    private String intercum_num = "";

    public Costumer(){}

    public Costumer(String comment, String phone, String city, String floor, String entrance, String building, String street, String apartment,
                    double source_cord_lat, double source_cord_long ,String name,String another_phone,String intercum_num) {
        this.comment = comment;
        this.phone = phone;
        this.city = city;
        this.floor = floor;
        this.entrance = entrance;
        this.building = building;
        this.street = street;
        this.apartment = apartment;
        this.source_cord_lat = source_cord_lat;
        this.source_cord_long = source_cord_long;
        this.name = name;
        this.another_phone = another_phone;
        this.intercum_num = intercum_num;

    }

    public Costumer(Costumer value) {
        this.comment = value.getComment();
        this.phone = value.getPhone();
        this.city = value.getCity();
        this.floor = value.getFloor();
        this.entrance = value.getEntrance();
        this.building = value.getBuilding();
        this.street = value.getStreet();
        this.apartment = value.getApartment();
        this.source_cord_lat = value.getSource_cord_lat();
        this.source_cord_long = value.getSource_cord_long();
        this.name = value.getName();
        this.another_phone = value.getAnother_phone();
        this.intercum_num = value.getIntercum_num();
    }

    public String getIntercum_num() {
        return intercum_num;
    }

    public void setIntercum_num(String intercum_num) {
        this.intercum_num = intercum_num;
    }

    public String getAnother_phone() {
        return another_phone;
    }

    public void setAnother_phone(String another_phone) {
        this.another_phone = another_phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public double getSource_cord_lat() {
        return source_cord_lat;
    }

    public void setSource_cord_lat(double source_cord_lat) {
        this.source_cord_lat = source_cord_lat;
    }

    public double getSource_cord_long() {
        return source_cord_long;
    }

    public void setSource_cord_long(double source_cord_long) {
        this.source_cord_long = source_cord_long;
    }
}
