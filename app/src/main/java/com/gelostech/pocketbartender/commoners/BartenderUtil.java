package com.gelostech.pocketbartender.commoners;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gelostech.pocketbartender.models.HomeModel;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by tirgei on 3/12/18.
 */

public class BartenderUtil {
    public static final String HOME_ALCOHOLIC = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic";
    public static final String HOME_NON_ALCOHOLIC = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Non_Alcoholic";
    public static final String COCKTAIL = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";
    public static final String MORE_COCKTAILS = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=";
    public static final String SEARCH_URL = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=";
    private List<HomeModel> cocktails;

    public BartenderUtil(){}

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm != null ? cm.getAllNetworkInfo() : new NetworkInfo[0];
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
