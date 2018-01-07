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

    /*
     * Creates a TextWebAccessor object. Requires the url of the website
     */
    public TextWebAccessor(String siteURL){
        try{
            url = new URL(siteURL); 
            openStream();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
     * Opens a stream with the website to begin reading text. 
     */
    private void openStream(){
        try{
            urlStream = url.openStream();
            ready = true;
        }catch(IOException e){
            ready = false;
            e.printStackTrace();
        }
    }

    /*
     * Safely closes the stream with the website
     */
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
