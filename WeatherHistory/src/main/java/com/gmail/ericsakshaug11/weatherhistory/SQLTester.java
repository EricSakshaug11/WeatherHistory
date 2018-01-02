package com.gmail.ericsakshaug11.weatherhistory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Eric
 */
public class SQLTester {

    public final static String dateFormat = "HH:mm:ss zzzz";
    public final static SimpleDateFormat format = new SimpleDateFormat(dateFormat);
    
  public static void main(String[] args) {
    SQLManager sQLManager = SQLManager.getSQLManager();    
    LinkedList<Station> stations = sQLManager.createStations();
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    ses.scheduleAtFixedRate(new Runnable(){
      @Override
      public void run(){
        Iterator stationIterator = stations.iterator();
        System.out.println("Updating " + stations.size() + " stations at: " + format.format(new Date()));
        while(stationIterator.hasNext()){
          Station tempStation = (Station)stationIterator.next();
          tempStation.update();
        }
        sQLManager.addWeatherData(stations);
        System.out.println("Wait for timer beginning at: " + format.format(new Date()));
      }
    },0,1,TimeUnit.HOURS);
  }
  

}
