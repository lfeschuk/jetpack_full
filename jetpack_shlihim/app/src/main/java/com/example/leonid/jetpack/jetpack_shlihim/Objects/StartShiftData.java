package com.example.leonid.jetpack.jetpack_shlihim.Objects;

public class StartShiftData {
    String date = "";
    String index = "";
    String name = "";
    String time = "";
    String num_of_plate = "";
    String num_of_gas_card = "";
    String company_phone_index = "";

    public StartShiftData(String date, String index, String name, String time, String num_of_plate, String num_of_gas_card, String company_phone_index) {
        this.date = date;
        this.index = index;
        this.name = name;
        this.time = time;
        this.num_of_plate = num_of_plate;
        this.num_of_gas_card = num_of_gas_card;
        this.company_phone_index = company_phone_index;
    }

    public StartShiftData() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNum_of_plate() {
        return num_of_plate;
    }

    public void setNum_of_plate(String num_of_plate) {
        this.num_of_plate = num_of_plate;
    }

    public String getNum_of_gas_card() {
        return num_of_gas_card;
    }

    public void setNum_of_gas_card(String num_of_gas_card) {
        this.num_of_gas_card = num_of_gas_card;
    }

    public String getCompany_phone_index() {
        return company_phone_index;
    }

    public void setCompany_phone_index(String company_phone_index) {
        this.company_phone_index = company_phone_index;
    }
}
