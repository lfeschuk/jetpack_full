package Objects;

import android.util.Log;

import java.util.ArrayList;

public class DeliveryGuys extends DeliveryObject {
    public static final String TAG = "DeliveryGuys";
    String timeBeFree = "";
   ArrayList<Delivery> deliveries = new ArrayList<>();
    double latetude = 0;
    double longtitude = 0;

    public DeliveryGuys(String name, String timeBeFree, ArrayList<Delivery> deliveries, double latetude, double longtitude, String picture,String index_string,Boolean is_active,
                        long index,double salary,String phone) {

        this.name = name;
        this.timeBeFree = timeBeFree;
        if (deliveries != null)
        {
            this.deliveries = deliveries;
        }
        this.latetude = latetude;
        this.longtitude = longtitude;
        this.picture = picture;
        this.index_string = index_string;
        this.is_active = is_active;
        this.index = index;
        this.salary = salary;
        this.phone = phone;
    }
    public DeliveryGuys(){}

    public DeliveryGuys( DeliveryGuys d)
    {
        this.name = d.getName();
        this.timeBeFree = d.getTimeBeFree();
        this.deliveries = d.getDeliveries();
        this.latetude = d.getLatetude();
        this.longtitude = d.getLongtitude();
        this.picture = d.getPicture();
        this.index_string = d.getIndex_string();
        this.is_active = d.getIs_active();
        this.index = d.getIndex();
        this.salary = d.getSalary();
        this.phone = d.getPhone();
    }



    public ArrayList<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(ArrayList<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public void addDelivery(Delivery d)
    {
        this.deliveries.add(d);
    }
    public void removeDelivery(String delivery_index)
    {
        for (Delivery d : deliveries)
        {
            Log.d(TAG,"iter indx: "+ d.getIndexString() + " wanted: " +delivery_index);
            if (d.getIndexString().equals(delivery_index))
            {
                Log.d(TAG,"remove delivery from guy: " + index_string + " deliv: " + delivery_index);
                deliveries.remove(d);
                return;
            }
        }
    }



    public String getTimeBeFree() {
        return timeBeFree;
    }

    public void setTimeBeFree(String timeBeFree) {
        this.timeBeFree = timeBeFree;
    }



    public double getLatetude() {
        return latetude;
    }

    public void setLatetude(double latetude) {
        this.latetude = latetude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }


}
