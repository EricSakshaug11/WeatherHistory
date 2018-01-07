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

    /*
     * This method translates the METAR string into a human readable format
     */
    public static String translate(String newToTranslate){
      toTranslate = newToTranslate;
      String toReturn = new String();
      strip();
      split();
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

    /*
     * This method translates the METAR string into a string more usable by
     * a computer. This method is not as useful as arrayComputerTranslate
     */
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

    /*
     * Translates the METAR string into an array for direct insertion into 
     * the SQL database.
     */
    public static String[] arrayComputerTranslate(String newToTranslate, String station){
      toTranslate = newToTranslate;
      String[] toReturn = new String[12];
      toReturn[0] = station + getTimeOfDay() + getDate();
      toReturn[1] = station;
      toReturn[2] = getTimeOfDay();
      toReturn[3] = getDate();
      toReturn[4] = new Integer(getWindDirectionDegrees()).toString();
      toReturn[5] = getWindDirectionCardinal();
      toReturn[6] = getWindSpeed();
      toReturn[7] = getVisibility();
      toReturn[8] = getTemperature();
      toReturn[9] = getDewpoint();
      String cloudHolder[][] = getClouds();
      toReturn[10] = new String();
      for(int i = 0 ; i < cloudHolder[0].length ; i++){
          toReturn[10] = toReturn[10] + cloudHolder[0][i] + " at " + cloudHolder[1][i] + ", ";
      }
      toReturn[11] = new String();
      if(getAtmosphericConditions() != null){
        toReturn[11] = toReturn[11] + getAtmosphericConditions() + ", ";
      }
      return toReturn;
    }

    // Removes control data from the METAR string that we do not need
    private static void strip(){
        toTranslate = toTranslate.substring(toTranslate.indexOf("METAR") + 6);
    }

    // Splits the string into tokens that are easier to use
    private static void split(){
        holder = toTranslate.split(" ");
    }

    // Returns the station's callsign
    private static String getStationID(){
        return toTranslate.substring(0, toTranslate.indexOf(" "));
    }

    // Calls getSplitByRegex(regex,1)
    private static String getSplitByRegex(String regex) throws ArrayIndexOutOfBoundsException{
        return getSplitByRegex(regex,1)[0];
    }

    //Finds a specific match in the METAR string that matches a regex.
    //Will return an array with size max as full as it can be.
    private static String[] getSplitByRegex(String regex, int max){
        String tempToReturn[] = new String[max];
        int numMatches = 0;
        for(int i = 0 ; i < holder.length && numMatches < max ; i++){
            if(holder[i].matches(regex)){
                tempToReturn[numMatches] = holder[i];
                numMatches++;
            }
        }
        String[] toReturn = new String[numMatches];
        for(int j = 0 ; j < toReturn.length ; j++){
            toReturn[j] = tempToReturn[j];
        }
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
    
    private static String getDate(){
      return getYear() + "/" + getMonth() + "/" + getDay();
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

    //Needs to be fixed!
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
