/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.ericsakshaug11.weatherhistory;

import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author Eric
 */
public class HumanTranslator {
    
    private static String toTranslate;
    private static String[] holder;
    private static Calendar utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private static Calendar cvtCal = Calendar.getInstance(TimeZone.getTimeZone("Atlantic/Cape_Verde"));
    
    public static String translate(String newToTranslate){
      toTranslate = newToTranslate;
      String toReturn = new String();
      strip();
      split();
      //System.out.println(toTranslate);
      toReturn = getStationID() + " reports the conditions at \n";
      toReturn = toReturn + getTimeOfDay() + " on " + getMonth() + "/" + getDay() + "/" + getYear() + "\n";
      toReturn = toReturn + "The wind was coming from: " + getWindDirectionDegrees() + " degrees (" + getWindDirectionCardinal() + ")" + "\n";
      toReturn = toReturn + " At: " + getWindSpeed() + "knots." + "\n";
      toReturn = toReturn + "Visibility is: " + getVisibility() + " miles." + "\n";
      toReturn = toReturn + "Current temp is: " + getTemperature() + " degrees Celcius" + "\n";
      toReturn = toReturn + "Current dewpoint is: " + getDewpoint() + " degrees Celcius" + "\n";   
      String cloudHolder[][] = getClouds();
      for(int i = 0 ; i < cloudHolder[0].length ; i++){
          toReturn = toReturn + "Cloud coverage is: " + cloudHolder[0][i] + " at " + cloudHolder[1][i] + " feet." + "\n";
      }
      if(getAtmosphericConditions() != null){
        toReturn = toReturn + "The current conditions are: " + getAtmosphericConditions() + "\n";
      }
      return toReturn;
    }
    
    public static String computerTranslate(String newToTranslate){
      toTranslate = newToTranslate;
      String toReturn = new String();
      strip();
      split();
      //System.out.println(toTranslate);
      toReturn = getStationID() + ";";
      toReturn = toReturn + getTimeOfDay() + ";" + getMonth() + "/" + getDay() + "/" + getYear() + ";";
      toReturn = toReturn + getWindDirectionDegrees() + ";" + getWindDirectionCardinal() + ";";
      toReturn = toReturn + getWindSpeed() + ";";
      toReturn = toReturn + getVisibility() + ";";
      toReturn = toReturn + getTemperature() + ";";
      toReturn = toReturn + getDewpoint() + ";";   
      String cloudHolder[][] = getClouds();
      for(int i = 0 ; i < cloudHolder[0].length ; i++){
          toReturn = toReturn + cloudHolder[0][i] + ", " + cloudHolder[1][i] + ";";
      }
      if(getAtmosphericConditions() != null){
        toReturn = toReturn + getAtmosphericConditions() + ";";
      }
      toReturn = toReturn.substring(0,toReturn.lastIndexOf(";"));
      return toReturn;
    }
    
    public static String[] arrayComputerTranslate(String newToTranslate, String station){
      toTranslate = newToTranslate;
      String[] toReturn = new String[11];
      toReturn[0] = station;
      toReturn[1] = getTimeOfDay();
      toReturn[2] = getYear() + "/" + getMonth() + "/" + getDay();
      toReturn[3] = new Integer(getWindDirectionDegrees()).toString();
      toReturn[4] = getWindDirectionCardinal();
      toReturn[5] = getWindSpeed();
      toReturn[6] = getVisibility();
      toReturn[7] = getTemperature();
      toReturn[8] = getDewpoint();
      String cloudHolder[][] = getClouds();
      toReturn[9] = new String();
      for(int i = 0 ; i < cloudHolder[0].length ; i++){
          toReturn[9] = toReturn[9] + cloudHolder[0][i] + " at " + cloudHolder[1][i] + ", ";
      }
      toReturn[10] = new String();
      if(getAtmosphericConditions() != null){
        toReturn[10] = toReturn[10] + getAtmosphericConditions() + ", ";
      }
      return toReturn;
    }
    
    private static void strip(){
        toTranslate = toTranslate.substring(toTranslate.indexOf("METAR") + 6);
        //System.out.println(toTranslate);
    }
    
    private static void split(){
        holder = toTranslate.split(" ");
    }
    
    private static String getStationID(){
        return toTranslate.substring(0, toTranslate.indexOf(" "));
    }
    
    private static String getSplitByRegex(String regex) throws ArrayIndexOutOfBoundsException{
        //System.out.println(regex);
        //System.out.println(toTranslate);
        return getSplitByRegex(regex,1)[0];
    }
    
    private static String[] getSplitByRegex(String regex, int max){
        String tempToReturn[] = new String[max];
        int numMatches = 0;
        for(int i = 0 ; i < holder.length && numMatches < max ; i++){
            if(holder[i].matches(regex)){
                //System.out.println(holder[i] + " matches " + regex);
                tempToReturn[numMatches] = holder[i];
                numMatches++;
            }
        }
        String[] toReturn = new String[numMatches];
        for(int j = 0 ; j < toReturn.length ; j++){
            toReturn[j] = tempToReturn[j];
        }
        //System.out.println(toReturn[0]);
        return toReturn;
    }
    
    private static String getTimeOfDay(){
        String temp = getSplitByRegex("\\d+Z");
        return temp.substring(2,4) + ":" + temp.substring(4,6) + " UTC";
    }
    
    private static String getDay(){
       String temp = getSplitByRegex("\\d+Z");                
       return temp.substring(0,2);
    }
    
    private static String getMonth(){
        int hour = Integer.parseInt(getTimeOfDay().substring(0,2));
        String toReturn;
        if(hour >= 23){
            toReturn = Integer.toString(cvtCal.get(Calendar.MONTH) + 1);
        }else{
            toReturn = Integer.toString(utcCal.get(Calendar.MONTH) + 1);
        }
        return toReturn;
    }
    
    private static String getYear(){
       int hour = Integer.parseInt(getTimeOfDay().substring(0,2));
        String toReturn;
        if(hour >= 23){
            toReturn = Integer.toString(cvtCal.get(Calendar.YEAR));
        }else{
            toReturn = Integer.toString(utcCal.get(Calendar.YEAR));
        }
        return toReturn;
    }
    
    private static String getWindSpeed(){
        String toReturn;
        try{
            String temp = getSplitByRegex("\\S+KT");
            toReturn = temp.substring(temp.indexOf("KT") - 2,temp.indexOf("KT") + 2);
        }catch (ArrayIndexOutOfBoundsException e){
            toReturn = ResourcePool.NO_DATA_AVAILABLE;
        }
        return toReturn;
    }
    
    private static int getWindDirectionDegrees(){
        int toReturn;
        try{
            String temp = getSplitByRegex("\\S+KT");
            toReturn = Integer.parseInt(temp.substring(0,3));
        }catch(Exception e){
            toReturn = -1;
        }
        return toReturn;
    }
    
    private static String getWindDirectionCardinal(){
        String toReturn;
        if(getWindDirectionDegrees() == -1){
          toReturn = ResourcePool.NO_DATA_AVAILABLE;
        }else{
          toReturn = ResourcePool.CARDINALITY_MAP.get(45 * (int)Math.round((getWindDirectionDegrees() % 360) / 45));
        }        
        return toReturn;
    }
    
    private static String getVisibility(){
        String toReturn;
        try{
            String temp = getSplitByRegex("\\S+SM");
            toReturn = temp.substring(0,temp.indexOf("SM"));
        }catch(ArrayIndexOutOfBoundsException e){
            toReturn = ResourcePool.NO_DATA_AVAILABLE;
        }
        return toReturn;
    }
    
    private static String getTemperature(){
        String toReturn;
        try{
            String temp = getSplitByRegex("(M\\d+\\/|\\d+\\/)(M\\d+|\\d+)");
            temp = temp.split("/")[0];
            if(temp.contains("M")){
                toReturn = "-" + temp.substring(1,temp.length());
            }else{
                toReturn = temp.substring(0,temp.length());
            }
        }catch(ArrayIndexOutOfBoundsException e){
            toReturn = ResourcePool.NO_DATA_AVAILABLE;
        }
        return toReturn;
    }
    
    private static String getDewpoint(){
        String toReturn;
        try{
            String temp = getSplitByRegex("(M\\d+\\/|\\d+\\/)(M\\d+|\\d+)");
            temp = temp.split("/")[1];
            if(temp.contains("M")){
                toReturn = "-" + temp.substring(1,temp.length());
            }else{
                toReturn = temp.substring(0,temp.length());
            }
        }catch(ArrayIndexOutOfBoundsException e){
            toReturn = ResourcePool.NO_DATA_AVAILABLE;
        }
        return toReturn;
    }
    
    private static String[][] getClouds(){
        String[] temp = getSplitByRegex("([A-Z]{2}[A-OQ-Z]\\d{3})", 6);
        String[][] toReturn = new String[2][temp.length];
        for(int i = 0 ; i < toReturn[0].length ; i++){
            String condition,altitude;
            condition = temp[i].substring(0,3);
            altitude = temp[i].substring(3,6);
            //System.out.println("Condition: " + condition);
            //System.out.println("Altitude: " + altitude);
            toReturn[0][i] = ResourcePool.COVERAGE_MAP.get(condition);
            toReturn[1][i] = Integer.toString(Integer.parseInt(altitude) * 100);
        }
        return toReturn;
    }
    
    private static String getAtmosphericConditions(){
        String conditions = new String();
        boolean foundConditions = false;
        Object[] objHolder = ResourcePool.ATMOSPHERIC_CONDITION_MAP.keySet().toArray();
        String[] keyHolder = new String[objHolder.length];
        for(int i = 0 ; i < objHolder.length ; i++){
            keyHolder[i] = objHolder[i].toString();
        }
        for(String tempKey:keyHolder){
            for(String temp:holder){
                if(temp.equals(tempKey)){
                    conditions = conditions+(String)ResourcePool.ATMOSPHERIC_CONDITION_MAP.get(tempKey) + ", ";
                    foundConditions  = true;
                }
            }
        }
        return foundConditions?conditions.substring(0,conditions.lastIndexOf(",")):null;
    }
            
}
