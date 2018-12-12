package Objects;

public class Destination {
    private String adressTo;
    private String adressFrom;
    private  String timeInserted;
    private String timeTaken;
    private String timeDeliver;
    private Boolean to_costumer;
    private String index_string = "";
    private String name_costumer = "";
    private String business_name = "";
    private double latitude;
    private double longitude;
    private static long index_static = 0;
    private long index = 0;


    public Destination(String adressTo, String adressFrom, String timeInserted, String timeTaken, String timeDeliver, Boolean is_costumer,String index_string,
                       double lati,double longi ) {
        this.adressTo = adressTo;
        this.adressFrom = adressFrom;
        this.timeInserted = timeInserted;
        this.timeTaken = timeTaken;
        this.timeDeliver = timeDeliver;
        this.to_costumer = is_costumer;
        this.index_string = index_string;
        this.latitude = lati;
        this.longitude = longi;
        this.index = index_static;
        index_static++;

    }
    public Destination(Destination d) {
        this.adressTo = d.getAdressTo();
        this.adressFrom = d.getAdressFrom();
        this.timeInserted = d.getTimeInserted();
        this.timeTaken = d.getTimeTaken();
        this.timeDeliver = d.getTimeDeliver();
        this.to_costumer = d.getTo_costumer();
        this.index_string = d.getIndex_string();
        this.name_costumer = d.getName_costumer();
        this.business_name = d.getBusiness_name();
        this.latitude = d.getLatitude();
        this.longitude = d.getLongitude();
    }

    public Destination(Delivery d,Boolean to_costumer)
    {
        this.adressTo = d.getAdressTo();
        this.adressFrom = d.getAdressFrom();
        this.timeInserted = d.getTimeInserted();
        this.timeTaken = d.getTimeTaken();
        this.timeDeliver = d.getTimeDeliver();
        this.to_costumer = to_costumer;
        this.index_string = d.getIndexString();
        this.name_costumer = d.getCostumerName();
        this.business_name = d.getBusiness_name();
        if (to_costumer)
        {
            this.longitude = d.getDest_cord_long();
            this.latitude = d.getDest_cord_lat();
        }
        else
        {
            this.longitude = d.getSource_cord_long();
            this.latitude = d.getSource_cord_lat();
        }
        this.index = index_static;
        this.index_static ++;
    }

    public static long getIndexStatic() {
        return index_static;
    }

    public long getIndex() {
        return index;
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

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getName_costumer() {
        return name_costumer;
    }

    public void setName_costumer(String name_costumer) {
        this.name_costumer = name_costumer;
    }

    public String getIndex_string() {
        return index_string;
    }

    public void setIndex_string(String index_string) {
        this.index_string = index_string;
    }

    public String getAdressTo() {
        return adressTo;
    }

    public void setAdressTo(String adressTo) {
        this.adressTo = adressTo;
    }

    public String getAdressFrom() {
        return adressFrom;
    }

    public void setAdressFrom(String adressFrom) {
        this.adressFrom = adressFrom;
    }

    public String getTimeInserted() {
        return timeInserted;
    }

    public void setTimeInserted(String timeInserted) {
        this.timeInserted = timeInserted;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getTimeDeliver() {
        return timeDeliver;
    }

    public void setTimeDeliver(String timeDeliver) {
        this.timeDeliver = timeDeliver;
    }

    public Boolean getTo_costumer() {
        return to_costumer;
    }

    public void setTo_costumer(Boolean to_costumer) {
        this.to_costumer = to_costumer;
    }
}
