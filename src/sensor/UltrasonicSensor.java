/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.Pin;
import rpi.PiGpioController;

/**
 *
 * @author pi
 */
public class UltrasonicSensor extends Thread{    
    private GpioPinDigitalOutput triggerPin ;
    private GpioPinDigitalInput echoPin ;
    private double distance;

    public UltrasonicSensor(String name, Pin triggerPin, Pin echoPin) {
        super(name);
        GpioController GPIO = PiGpioController.getGpioController();
        this.triggerPin =  GPIO.provisionDigitalOutputPin(triggerPin); // Trigger pin as OUTPUT
        this.echoPin = GPIO.provisionDigitalInputPin(echoPin,PinPullResistance.PULL_DOWN); // Echo pin as INPUT
    }

    public double getDistance() {
        return distance;
    }
    
    public void run(){        
        while(true){
            try {
                Thread.sleep(500);
                triggerPin.high(); // Make trigger pin HIGH
                Thread.sleep(0,10000);// Delay for 10 microseconds  10000 nanoseconds
                triggerPin.low(); //Make trigger pin LOW
                
                while(echoPin.isLow());//Wait until the ECHO pin gets HIG
                long startTime= System.nanoTime(); // Store the surrent time to calculate ECHO pin HIGH time.
                while(echoPin.isHigh());//Wait until the ECHO pin gets LOW
                long endTime= System.nanoTime(); // Store the echo pin HIGH end time to calculate ECHO pin HIGH time.

                distance =Math.round(((((endTime-startTime)/1e3)/2) / 29.1)*100.0)/100.0; //Calculating the distance in cm  
                //System.out.println(distance+" cms");
                Thread.sleep(400);

              } 
              catch (InterruptedException e) {
                  e.printStackTrace();
              }              
        }    
    }   
}
