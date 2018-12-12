package Objects;

public class Restoraunt extends Business {

    int time_to_prepare;
    int time_to_costumer;

    public Restoraunt(String name, String index, double longt, double lat,int time_to_costumer,int time_to_prepare) {
        super(name, index, longt, lat);
        this.time_to_costumer = time_to_costumer;
        this.time_to_prepare = time_to_prepare;
    }

    public Restoraunt(Restoraunt b) {
        super(b);
        this.time_to_prepare = b.getTime_to_prepare();
        this.time_to_costumer = b.getTime_to_costumer();
    }
    public Restoraunt(){}

    public int getTime_to_costumer() {
        return time_to_costumer;
    }

    public int getTime_to_prepare() {
        return time_to_prepare;
    }

    public void setTime_to_costumer(int time_to_costumer) {
        this.time_to_costumer = time_to_costumer;
    }

    public void setTime_to_prepare(int time_to_prepare) {
        this.time_to_prepare = time_to_prepare;
    }
}
