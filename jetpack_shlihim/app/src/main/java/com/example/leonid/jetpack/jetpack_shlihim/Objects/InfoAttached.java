package com.example.leonid.jetpack.jetpack_shlihim.Objects;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class InfoAttached {
    String indexString = "";
    String name = "";
    String profile;
    String drivers_license;
    String id_image ;
    String form_101;
    String licesnse_exp = "";
    Boolean admin_approved = false;

    public String getProfile() {
        return profile;
    }

    public InfoAttached(String indexString) {
        this.indexString = indexString;

    }

    public String bitmap2string(Bitmap image)
    {
        if (image == null)
        {
            return "";
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return encodedImage;
    }
//    public InfoAttached(InfoAttached pa)
//    {
//        this.indexString = pa.getIndexString();
//        this.profile = pa.getProfile();
//        this.drivers_license = pa.getDrivers_license();
//        this.id_image = pa.getId_image();
//        this.form_101 = pa.getForm_101();
//        this.admin_approved = pa.getAdmin_approved();
//        this.name=  pa.getName();
//        this.licesnse_exp = pa.getLicesnse_exp();
//
//    }
    public InfoAttached()
    {

    }

    public String getLicesnse_exp() {
        return licesnse_exp;
    }

    public void setLicesnse_exp(String licesnse_exp) {
        this.licesnse_exp = licesnse_exp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAdmin_approved() {
        return admin_approved;
    }

    public void setAdmin_approved(Boolean admin_approved) {
        this.admin_approved = admin_approved;
    }

    public InfoAttached(String indexString,String name, Bitmap profile, Bitmap drivers_license, Bitmap id_image, Bitmap form_101,String licesnse_exp) {
        this.indexString = indexString;
        this.profile = bitmap2string(profile);
        this.drivers_license = bitmap2string(drivers_license);
        this.id_image = bitmap2string(id_image);
        this.form_101 = bitmap2string(form_101);
        this.name = name;
        this.licesnse_exp = licesnse_exp;
    }




    public String getIndexString() {
        return indexString;
    }

    public void setIndexString(String indexString) {
        this.indexString = indexString;
    }


}
