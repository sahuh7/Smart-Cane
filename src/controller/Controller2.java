/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import alarm.Alarm;
import com.pi4j.io.gpio.GpioController;
//import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
//import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import sensor.UltrasonicSensor;
import rpi.PiGpioController;
import power.Shutdown;

/**
 *
 * @author Rajeev
 */
public class Controller2 {
    private static final GpioController GPIO=PiGpioController.getGpioController();

    public static void main(String[] args) {
        System.out.println("started");
        
        double negativeRange=0.0,positiveRange=0.0;
        GpioPinDigitalOutput buzzerPin=GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_06);
        GpioPinDigitalOutput ledPin=GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_03);
        buzzerPin.high();
        ledPin.high();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            
        }
        buzzerPin.low();
        ledPin.low();
        double dangerZone=100.0;
        
        UltrasonicSensor lowerSensor=new UltrasonicSensor("LowerUltrasonic-Thread", RaspiPin.GPIO_00, RaspiPin.GPIO_02);
        UltrasonicSensor higherSensor=new UltrasonicSensor("HigherUltrasonic-Thread", RaspiPin.GPIO_04, RaspiPin.GPIO_05);
        Shutdown sd=new Shutdown("Shutdown-Thread", RaspiPin.GPIO_09);
        sd.start();
        
        try{           
            lowerSensor.start();
            higherSensor.start();
            while(true){
                Thread.sleep(50);
                
                if(higherSensor.getDistance()<dangerZone&&higherSensor.getDistance()>0){
                    negativeRange=higherSensor.getDistance()-10.0;
                    positiveRange=higherSensor.getDistance()+10.0;
                    //System.out.println("Range -ve ("+negativeRange+" - "+positiveRange+") +ve");
                    if(lowerSensor.getDistance()<positiveRange&&lowerSensor.getDistance()>negativeRange){
                        System.out.println("Inside inner if");
                        Alarm.generateALarm(ledPin,50,6);
                        Alarm.generateALarm(buzzerPin,50,6);
                        continue;
                    }
                    System.out.println("outer if");
                    Alarm.generateALarm(ledPin,100,4);
                    Alarm.generateALarm(buzzerPin,100,4);
                }
                GPIO.shutdown();
            }            
        }
        catch(InterruptedException ex){
            ex.printStackTrace();
        }
        finally{
            if(GPIO!=null){
                GPIO.shutdown();
            }
        }
    }
}
