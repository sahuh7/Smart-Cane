/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package power;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpi.PiGpioController;

/**
 *
 * @author pi
 */
public class Shutdown extends Thread {
    
    private GpioController GPIO=PiGpioController.getGpioController();
    private GpioPinDigitalInput switchPin;

    public Shutdown(String name, Pin switchPin) {
        super(name);
        this.switchPin = GPIO.provisionDigitalInputPin(switchPin,PinPullResistance.PULL_UP);
    }
    
    @Override
    public void run(){
        
        while(true){
            if(switchPin.isLow())
                try {
                    GPIO.shutdown();
                    Runtime.getRuntime().exec("sudo shutdown -h now");                    
            } catch (IOException ex) {
                Logger.getLogger(Shutdown.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
