/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.ericsakshaug11.weatherhistory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author Eric
 */
public class StationFileManager {
    private Scanner scan;
    private final File FOLDER = new File("src\\main\\java\\resources\\");
    private File files[];
    
    public StationFileManager(){
        System.out.println(System.getProperty("user.dir"));
        System.out.println(FOLDER.exists());
        File[] tempFiles = FOLDER.listFiles();
        files = getFiles();
        boolean isFormatted = false;
        for(int i = 0 ; !isFormatted && i < tempFiles.length ; i++){
            isFormatted = tempFiles[i].getName().equals("formatted.txt");
        }
        if(!isFormatted){
            formatFiles();
        }
    }
    
    private File[] getFiles(){
        return FOLDER.listFiles(new FilenameFilter() { 
                 public boolean accept(File dir, String filename)
                      { return filename.endsWith(".txt"); }
        } );
    }
    
    public LinkedList<String> getStationList(){
        LinkedList<String> toReturn = new LinkedList<String>();
        for(File currFile:files){
            try{
                scan = new Scanner(currFile);
                while(scan.hasNextLine()){
                    toReturn.add(scan.nextLine() + ":" + currFile.getName().substring(0,2));
                }
            }catch(Exception e){
              e.printStackTrace();  
            }
        }
        return toReturn;
    }
    
    public LinkedList<String> getStationCallsigns(){
        LinkedList<String> toReturn = new LinkedList<String>();
        for(File currFile:files){
            try{
                scan = new Scanner(currFile);
                while(scan.hasNextLine()){
                    String temp = scan.nextLine();
                    String[] temps = temp.split(":");
                    toReturn.add(temps[1]);
                }
            }catch(Exception e){
            
            }
        }
        return toReturn;
    }
    
    public void formatFiles(){
        for(File currFile:files){
            try{
                scan = new Scanner(currFile);
                String toWrite = "";
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    if(line.contains("(")){
                        String[] temp = line.split("[(]");
                        if(temp.length == 2){
                            temp[1] = temp[1].substring(0, temp[1].indexOf(")"));
                            //System.out.println(temp[0]);
                            //System.out.println(temp[1]);
                            toWrite = toWrite + temp[0].trim() + ":" + temp[1] + System.getProperty("line.separator");
                        }else{
                            String name = new String();
                            for(int i = 0 ; i < temp.length - 1 ; i++){
                                name = name + temp[i];
                                if(i < temp.length - 2)
                                    name = name + "(";
                            } 
                            toWrite = toWrite + name.trim() + ":" + temp[temp.length - 1].substring(0,temp[temp.length - 1].indexOf(")")) + System.getProperty("line.separator");
                        }
                    }
                }
                scan.close();
                String name = currFile.getPath();
                currFile.renameTo(new File(name + "(old)"));
                File newFile = new File(name);
                newFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
                //System.out.println(toWrite);
                writer.write(toWrite);
                writer.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        File marker = new File("src\\resources\\formatted.txt");
        try{
            marker.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
