package com.gmail.ericsakshaug11.weatherhistory;

import java.util.Scanner;

/**
 *
 * @author Eric
 */
public class Station {
    
    private String callsign;
    private String location;
    private String lastReading;
    private String currReading;
    private String currHuman;
    private String currComputer;
    private String[] currComputerArray;
    private String state;
    private String url;
    
    public Station(String input) {
      //System.out.print(input);
      String[] temp = input.split(":");
      //System.out.println(temp.length);
      callsign = temp[1];
      location = temp[0];
      lastReading = null;
      currReading = null;
      state = temp[2];
      url = "http://w1.weather.gov/data/METAR/" + callsign + ".1.txt";
      //System.out.println("made the object for: " + callsign);
    }
    
    public Station(String callsign, String state, String location){
        this.callsign = callsign;
        this.location = location;
        this.state = state;
        lastReading = null;
        currReading = null;
        url = "http://w1.weather.gov/data/METAR/" + callsign + ".1.txt";
        //System.out.println("made the object for: " + callsign);
    }
    
    public void update(){
        //System.out.println("Updating: " + location);
        TextWebAccessor access = new TextWebAccessor(url);
        //System.out.println(url);
        Scanner scanner = new Scanner(access.getStream());        
        while(scanner.hasNextLine()){
            currReading += scanner.nextLine() + " ";
        }
        if(!currReading.equals(lastReading)){
            //System.out.println(currReading);
            lastReading = currReading;
        }
        scanner.close();
        access.closeStream();
        currHuman = HumanTranslator.translate(currReading);
        currComputer = HumanTranslator.computerTranslate(currReading);
        currComputerArray = HumanTranslator.arrayComputerTranslate(currReading, callsign).clone();
        //System.out.println("test2");
    }
    
    public String getHumanReadable(){
        return currHuman;
    }
    
    public String getComputerReadable(){
        return currComputer;
    }
    
    public String[] getComputerReadableArray(){
      return currComputerArray;
    }
    
    public String getCallsign(){
      return callsign;
    }
    
    public String getState(){
      return state;
    }
    
    public String getLocation(){
      return location;
    }
            
    
}
