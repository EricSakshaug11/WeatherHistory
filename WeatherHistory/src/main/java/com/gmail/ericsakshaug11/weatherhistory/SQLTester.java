/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.ericsakshaug11.weatherhistory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Eric
 */
public class SQLTester {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    SQLManager sQLManager = SQLManager.getSQLManager();    
    LinkedList<Station> stations = sQLManager.createStations();
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    ses.scheduleAtFixedRate(new Runnable(){
      @Override
      public void run(){
        Iterator stationIterator = stations.iterator();
        System.out.println("Updating stations, this process usually takes 5-10 minutes.");
        while(stationIterator.hasNext()){
          Station tempStation = (Station)stationIterator.next();
          tempStation.update();
        }
        sQLManager.addWeatherData(stations);
        System.out.println("All data inserted, waiting for timer...");
      }
    },0,1,TimeUnit.HOURS);
  }
  
}
