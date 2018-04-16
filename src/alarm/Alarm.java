/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 *
 * @author pi
 */
public class Alarm {
    public static void generateALarm(GpioPinDigitalOutput outputPin,long sleepFrequency, int frequency) throws InterruptedException{
        int count=0;
        while(count<frequency){            
            outputPin.toggle();
            //System.out.println("LED "+ledPin.getState().toString());
            count++;
            Thread.sleep(sleepFrequency);
        }
    }
}
