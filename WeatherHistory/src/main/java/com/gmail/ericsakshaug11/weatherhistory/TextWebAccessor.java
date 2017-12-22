/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.ericsakshaug11.weatherhistory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author Eric
 */
public class TextWebAccessor {
    
    private URL url;
    private InputStream urlStream;
    private boolean ready;
    
    public TextWebAccessor(String siteURL){
        try{
            url = new URL(siteURL); 
            //System.out.println("Got URL");
            openStream();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void openStream(){
        try{
            urlStream = url.openStream();
            ready = true;
        }catch(IOException e){
            ready = false;
            e.printStackTrace();
        }
    }
    
    public boolean closeStream(){
        if(ready){
            try{
                urlStream.close();
                ready = false;
            }catch(Exception e){
                e.printStackTrace();
                ready = true;
            }
        }
        return !ready;
    }
    
    public InputStream getStream(){
        return ready ? urlStream : null;
    }
    
    public boolean isReady(){
        return ready;
    }
    
    public boolean resetConnection(){
        closeStream();
        openStream();
        return ready;
    }
    
}
