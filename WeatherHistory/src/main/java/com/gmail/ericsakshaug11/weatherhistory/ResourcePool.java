package com.gmail.ericsakshaug11.weatherhistory;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Eric
 */
public class ResourcePool {

  //The IP address of the cloud SQL server
  public static final String SQL_IP_ADDRESS = "35.227.100.200"; 

  //The connection credentials of the cloud SQL server
  public static final String DATABASE_NAME = "weather_history";
  public static final String INSTANCE_CONNECTION_NAME = "weather-history-188703:us-east1:weather-history-2";
  public static final String DATABASE_USER_NAME = "root";
  public static final String DATABASE_PASSWORD = "HowsTheWeather";

  //For when there is no data available
  public static final String NO_DATA_AVAILABLE = "NDA";

  //Formatted String to insert data into the main weather table
  public static final String WEATHER_DATA_PREPARED_STATEMENT = "INSERT INTO weather_data "
                                                             + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

  //For wind direction cardinality between degrees and cardinal directions
  public static final HashMap<Integer, String> CARDINALITY_MAP = new HashMap<>();

  //For cloud coverage between METAR clodes, and human readable information
  public static final HashMap<String, String> COVERAGE_MAP = new HashMap<>();

  //For the atmospheric conditions (e.g. Rain, snow, fog)
  public static final HashMap<String, String> ATMOSPHERIC_CONDITION_MAP = new HashMap<>();

  static {
    CARDINALITY_MAP.put(0, "N");
    CARDINALITY_MAP.put(45, "NE");
    CARDINALITY_MAP.put(90, "E");
    CARDINALITY_MAP.put(135, "SE");
    CARDINALITY_MAP.put(180, "S");
    CARDINALITY_MAP.put(225, "SW");
    CARDINALITY_MAP.put(270, "W");
    CARDINALITY_MAP.put(315, "NW");
    CARDINALITY_MAP.put(360, "N");
    COVERAGE_MAP.put("SKC", "Clear");
    COVERAGE_MAP.put("CLR", "Clear");
    COVERAGE_MAP.put("FEW", "Few");
    COVERAGE_MAP.put("SCT", "Scattered");
    COVERAGE_MAP.put("BKN", "Broken");
    COVERAGE_MAP.put("OVC", "Overcast");
    ATMOSPHERIC_CONDITION_MAP.put("BR", "Mist");
    ATMOSPHERIC_CONDITION_MAP.put("DS", "Duststorm");
    ATMOSPHERIC_CONDITION_MAP.put("FU", "Smoke");
    ATMOSPHERIC_CONDITION_MAP.put("GR", "Hail");
    ATMOSPHERIC_CONDITION_MAP.put("GS", "Small hail");
    ATMOSPHERIC_CONDITION_MAP.put("IC", "Ice crystals");
    ATMOSPHERIC_CONDITION_MAP.put("HZ", "Haze");
    ATMOSPHERIC_CONDITION_MAP.put("PL", "Ice pellets");
    ATMOSPHERIC_CONDITION_MAP.put("PY", "Spray");
    ATMOSPHERIC_CONDITION_MAP.put("RA", "Rain");
    ATMOSPHERIC_CONDITION_MAP.put("S", "Snow");
    ATMOSPHERIC_CONDITION_MAP.put("SN", "Snow");
    ATMOSPHERIC_CONDITION_MAP.put("SS", "Sandstorm");
    ATMOSPHERIC_CONDITION_MAP.put("SW", "Snow shower");
    ATMOSPHERIC_CONDITION_MAP.put("TS", "Thunderstorm");
    ATMOSPHERIC_CONDITION_MAP.put("SG", "Snow Grains");
    ATMOSPHERIC_CONDITION_MAP.put("SP", "Snow Pellets");
    ATMOSPHERIC_CONDITION_MAP.put("SQ", "Squalls");
    ATMOSPHERIC_CONDITION_MAP.put("VA", "Volcanic ash");
    ATMOSPHERIC_CONDITION_MAP.put("UP", "Unknown precipitation");
    ATMOSPHERIC_CONDITION_MAP.put("VCBR", "Mist in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCDS", "Duststorm in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCFU", "Smoke in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCGR", "Hail in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCGS", "Small hail in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCIC", "Ice crystals in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCHZ", "Haze in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCPL", "Ice pellets in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCPY", "Spray in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCRA", "Rain in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCS", "Snow in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCSN", "Snow in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCSS", "Sandstorm in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCSW", "Snow shower in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCTS", "Thunderstorm in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCSG", "Snow Grains in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCSP", "Snow Pellets in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCSQ", "Squalls in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCVA", "Volcanic ash in the vicinity");
    ATMOSPHERIC_CONDITION_MAP.put("VCUP", "Unknown precipitation in the vicinity");
  }
}
