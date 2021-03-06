package com.example.singlemind.Utility;

import android.content.Context;
import android.net.Uri;

import com.example.singlemind.Model.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportUtil {

    public ImportUtil(){}

    private final static String TAG = "com.example.singlemind.Utility.ImportUtil";

    public String[] stringList;

    List<Event> calCC = new ArrayList<>();

    private String cName, dEnd, summary;
    Event event = new Event();

    //This code can be further optimized to support large ICS files.
    public List<Event> readTextFromUri(Context context, Uri uri) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = reader.readLine()) != null) {

            if(line.contains("CATEGORIES")){
                cName= parseCatagories(line);
                event.setmEventName(cName);
                event.setmEventType(0);
                event.setmEventUID(System.currentTimeMillis());
                calCC.add(event);
                event = new Event();
            }
            else if(line.contains("DTEND")){
                dEnd = fixDate(line);
                event.setmEventTime(dEnd);
            }
            else if(line.contains("SUMMARY")){
                summary= parseSummary(line);
                event.setmEventDescription(summary);
            }

        }//end of while-loop

        inputStream.close();

        return calCC;
    }

    private String fixDate(String d){

        String fdate = "";
        String newDate = null;
        Date date = null;

        //parsing the ics to reformt the date
        fdate = parseDates(d);

        //old format of parsed dates
        String inputPattern = "yyyyMMddhhmmss";

        //the new output format
        String outputPattern = "MM/dd/yyyy hh:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        //reformating the dates
        try {

            date = inputFormat.parse(fdate);
            newDate = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }//end of fixDate

    //Parse ICS date to only use date & hour
    public String parseDates(String s){
        //(\d*) grabs all numbers
        Pattern p = Pattern.compile("(\\d*)");

        //matching date numbers & hours only
        Matcher m = p.matcher(s);
        StringBuilder i = new StringBuilder();

        while(m.find()){
            i.append(m.group());
        }
        return i.toString();
    }//return only fixed start date numbers


    private String parseSummary(String s) {
        //regex code to ignore Summary and just grab discription
        Pattern p = Pattern.compile("\\b(?!(?:SUMMARY:\\b))[\\w ]+");
        //matching date numbers & hours only
        Matcher m = p.matcher(s);
        StringBuilder i = new StringBuilder();

        while(m.find()){
            i.append(m.group());
        }//end of whileloop
        return i.toString();
    }//end of summary parse method


    private String parseCatagories(String s) {
        //regex code to ignore Summary and just grab discription
        Pattern p = Pattern.compile("\\b(?!(?:CATEGORIES:\\b))[\\w-]+");
        //matching date numbers & hours only
        Matcher m = p.matcher(s);
        StringBuilder i = new StringBuilder();

        while(m.find()){
            i.append(m.group());
        }//end of whileloop
        return i.toString();
    }//end of catagories parse method
}//end of class

