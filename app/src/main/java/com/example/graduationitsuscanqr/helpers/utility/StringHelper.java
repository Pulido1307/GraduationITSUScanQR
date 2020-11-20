package com.example.graduationitsuscanqr.helpers.utility;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StringHelper
{
    public static String getCarrara(String val)
    {
        String carr = "";
        switch(val)
        {
            case "1":
                carr = "Ing. en Sistemas";
                break;
            case "3":
                carr = "Ing. en Industrial";
                break;
            case "4":
                carr = "Ing. en Alimentarias";
                break;
            case "5":
                carr = "Ing. en Electrónica";
                break;
            case "6":
                carr = "Ing. en Mecatrónica";
                break;
            case "7":
                carr = "Ing. en Administración";
                break;
            case "8":
                carr = "Ing. Mecánica";
                break;
            case "9":
                carr = "Ing. en Sistemas Mixta";
                break;
            case "10":
                carr = "Ing.en Administración Mixta";
                break;
            case "11":
                carr = "Civil";
                break;
        }
        return carr;
    }


}
