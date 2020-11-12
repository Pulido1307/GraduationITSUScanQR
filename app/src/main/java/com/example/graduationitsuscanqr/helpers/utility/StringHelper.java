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
                carr = "Sistemas";
                break;
            case "3":
                carr = "Industrial";
                break;
            case "4":
                carr = "Alimentarias";
                break;
            case "5":
                carr = "Electrónica";
                break;
            case "6":
                carr = "Mecatrónica";
                break;
            case "7":
                carr = "Mecánica";
                break;
            case "8":
                carr = "Administración";
                break;
            case "9":
                carr = "Sistemas Mixta";
                break;
            case "10":
                carr = "Administración Mixta";
                break;
            case "11":
                carr = "Civil";
                break;
        }
        return carr;
    }


}
