package com.gmail.ericsakshaug11.weatherhistory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Eric
 */
public class SQLManager {

  
  private final static SQLManager HOLDER;

  //The URL for the SQL server  
  String jdbcUrl = String.format(
    "jdbc:mysql://google/%s?cloudSqlInstance=%s&"
        + "socketFactory=com.google.cloud.sql.mysql.SocketFactory",
    ResourcePool.DATABASE_NAME,
    ResourcePool.INSTANCE_CONNECTION_NAME);
  private Connection connection;
  private Statement statement;
  
  private SQLManager() {
    try {
      connection = DriverManager.getConnection(jdbcUrl, ResourcePool.DATABASE_USER_NAME, ResourcePool.DATABASE_PASSWORD);
      statement = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  //For future Eric: this is called a Singleton
  static {
    HOLDER = new SQLManager();
  }

  public static SQLManager getSQLManager() {
    return HOLDER;
  }

  /* 
   * Adds the data from a LinkedList of stations to the database. This method
   * Will query the stations, format it for  the server, and ensure there are
   * No duplicates in the server by catching the MySQLIntegrityConstraint 
   * Exception
   */ 
  public void addWeatherData(LinkedList<Station> stations){
    System.out.println("Inserting data for " + stations.size() + " stations.");
    Iterator<Station> iterate = stations.iterator();
    PreparedStatement prepare = null;
    int numOfDupes = 0;
      try{
        prepare = connection.prepareStatement(ResourcePool.WEATHER_DATA_PREPARED_STATEMENT);
      }catch(Exception e){
        e.printStackTrace();
      }
    while(iterate.hasNext()){
      Station tempStation = iterate.next();
      String[] holder = tempStation.getComputerReadableArray().clone();
      for(int i = 0 ; i < holder.length ; i++){
        try{
          prepare.setString(i+1, holder[i]);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
      try{
        prepare.execute();
      }
      /*
       * Since execute can throw multiple types of SQLException, and I only 
       * care about one, we're going to catch all of them, and handle the one
       * with an if statement.
       */ 
      catch(SQLException e){ 

	  //Duplicate entries are handled with this if statement
        if(e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException){
            System.out.println("Duplicate entry detected, for station: " + tempStation.getCallsign() + ", skipping");
            numOfDupes++;
        }else{
          e.printStackTrace();
        }
    }
    System.out.println("Successfully inserted all data, with skipping " + numOfDupes + " duplicates"); 
  }

    //Creates all Station objects from the station_data table
  public LinkedList<Station> createStations(){
    String dumpStations = "SELECT * FROM station_data;";
    LinkedList<Station> toReturn = new LinkedList<Station>();
    System.out.println("Getting stations from database");
    try{
      Statement stmt = connection.createStatement();
      ResultSet stationResults = stmt.executeQuery(dumpStations);
      while(stationResults.next()){
        toReturn.add(new Station(stationResults.getString(1), stationResults.getString(2), stationResults.getString(3)));
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    System.out.println("All station objects created");
    return toReturn;
  }

  /* 
   * Old code for creating the database. Should not ever need to be used, but 
   * will remain commented out for emergencies.
   */
  /*public void createTables(){
    String stationTableCreation = "CREATE TABLE station_data( "
                                + "Callsign varchar(10), "
                                + "State char(2), "
                                + "LocationDescription varchar(255), "
                                + "PRIMARY KEY (Callsign) "
                                + ");";
    String dropWeatherData = "DROP TABLE weather_data";
    String weatherDataTableCreation = 
                                  "CREATE TABLE weather_data( "
                                + "ID varchar(31) NOT NULL, "
                                + "Callsign varchar(10), "
                                + "Time char(9), "
                                + "Date char(12), "
                                + "WindDirectionDegrees int, "
                                + "WindDirectionCardinal varchar(3), "
                                + "WindSpeed varchar(10), "
                                + "Visibility varchar(10), "
                                + "Temperature varchar(10), "
                                + "Dewpoint varchar(10), "
                                + "Clouds varchar(255), "
                                + "AtmosphericConditions varchar(255), "
                                + "PRIMARY KEY (ID));";
    try{
      statement.addBatch(dropWeatherData);
      statement.addBatch(weatherDataTableCreation);
      statement.executeBatch();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
    public void addStationData(LinkedList<Station> stations){
    Iterator<Station> iterate = stations.iterator();
    try{
      PreparedStatement prepare = connection.prepareStatement("INSERT INTO station_data VALUES (?,?,?)");
      while(iterate.hasNext()){      
        Station currStation = iterate.next();
        prepare.setString(1, currStation.getCallsign());
        prepare.setString(2, currStation.getState());
        prepare.setString(3, currStation.getLocation());
        prepare.execute();
      }
      //executeBatch();
    }catch(Exception e){
      e.printStackTrace();
    }
  }*/
  
}
