package com.gmail.ericsakshaug11.weatherhistory;

import java.util.Scanner;

/**
 *
 * @author Eric
 */
public class Station {

  private String callsign; //The station's callsign, e.g. KROC
  private String location; //The station's location, e.e. Rochester
  private String lastReading; //The previous reading (no longer used)
  private String currReading; //The current reading
  private String currHuman; //The current human translation
  private String currComputer; //The current computer translation
  private String[] currComputerArray; //The current computer translation array
  private String state; //The state that the station is located in, e.g. NY
  private String url; //The URL for the station

  /*
   * For use when the stations are created from local files.
   * This constructor should almost never be used.
   */
  public Station(String input) {
    String[] temp = input.split(":");
    callsign = temp[1];
    location = temp[0];
    lastReading = null;
    currReading = null;
    state = temp[2];
    url = "http://w1.weather.gov/data/METAR/" + callsign + ".1.txt";
  }

  /*
   * For use when the stations are created from the database
   * This is the preferred constructor.
   */
  public Station(String callsign, String state, String location){
    this.callsign = callsign;
    this.location = location;
    this.state = state;
    lastReading = null;
    currReading = null;
    url = "http://w1.weather.gov/data/METAR/" + callsign + ".1.txt";
  }

  /*
   * Gets updated data from the station's webpage.
   */
  public void update(){
    TextWebAccessor access = new TextWebAccessor(url);
    Scanner scanner = new Scanner(access.getStream());        
    while(scanner.hasNextLine()){
      currReading += scanner.nextLine() + " ";
    }
    if(!currReading.equals(lastReading)){
      lastReading = currReading;
    }
    scanner.close();
    access.closeStream();
    currHuman = HumanTranslator.translate(currReading);
    currComputer = HumanTranslator.computerTranslate(currReading);
    currComputerArray = HumanTranslator.arrayComputerTranslate(currReading, callsign).clone();
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
