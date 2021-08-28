package com.example.graduationitsuscanqr.helpers.utility;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class StringHelper {
    private static String EXREGEMAIL="([a-z,0-9]+((\\.|_|-)[a-z0-9]+)*)@([a-z,0-9]+(\\.[a-z0-9]+)*)\\.([a-z]{2,})(\\.([a-z]{2}))?";
    public static final String CARRERAS[] = {"Ing. en Sistemas", "Ing. en Industrial", "Ing. en Alimentarias", "Ing. en Electrónica","Ing. en Mecatrónica", "Ing. en Administración",
            "Ing. Mecánica","Ing. en Sistemas Mixta","Ing.en Administración Mixta","Ing. Civil"};

    public static final String GRUPOS[] = {"A", "B", "U", "P"};

    public static  boolean isEmail(String email)
    {
        return Pattern.matches(EXREGEMAIL,email);
    }

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


    public static String getNumCarrera(String carrera){
        String num = "";

        switch(carrera){
            case "Ing. en Sistemas":
                num = "1";
                break;

            case "Ing. en Industrial":
                num = "3";
                break;

            case "Ing. en Alimentarias":
                num = "4";
                break;

            case "Ing. en Electrónica":
                num = "5";
                break;

            case "Ing. en Mecatrónica":
                num = "6";
                break;

            case "Ing. en Administración":
                num = "7";
                break;

            case "Ing. Mecánica":
                num = "8";
                break;

            case "Ing. en Sistemas Mixta":
                num = "9";
                break;

            case "Ing.en Administración Mixta":
                num = "10";
                break;

            case "Civil":
                num = "11";
                break;
        }


        return num;
    }


}
