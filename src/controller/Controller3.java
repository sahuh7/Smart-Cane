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
 * @author pi
 */
public class Controller3 {
    
    public static void main(String[] args) {
        System.out.println("started");
        GpioController GPIO=PiGpioController.getGpioController();
        GpioPinDigitalOutput buzzerPin=GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_06);
        GpioPinDigitalOutput ledPin=GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_03);
        
        buzzerPin.high();
        ledPin.high();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        buzzerPin.low();
        ledPin.low();
        
        double dangerZone=150.0;
        
        UltrasonicSensor lowerSensor=new UltrasonicSensor("LowerUltrasonic-Thread", RaspiPin.GPIO_00, RaspiPin.GPIO_02);
        UltrasonicSensor higherSensor=new UltrasonicSensor("HigherUltrasonic-Thread", RaspiPin.GPIO_04, RaspiPin.GPIO_05);
        Shutdown sd=new Shutdown("Shutdown-Thread", RaspiPin.GPIO_09);
        
        
        
        try{
            sd.start();
            lowerSensor.start();
            higherSensor.start();
            while(true){
                Thread.sleep(50);
                GPIO.shutdown();
                if((lowerSensor.getDistance()<dangerZone&&lowerSensor.getDistance()>0) || (higherSensor.getDistance()<dangerZone&&higherSensor.getDistance()>0)){                    
                    if((higherSensor.getDistance()<=50.0&&higherSensor.getDistance()>=30.0) || (lowerSensor.getDistance()<=50.0&&lowerSensor.getDistance()>=30.0)){                        
                        System.out.println("Object is coming close!!! In range from 0.3 meters to 0.5 meters..----->> Alarm!! Alarm!!");
                        Alarm.generateALarm(ledPin,50,6);
                        Alarm.generateALarm(buzzerPin,50,6);
                        continue;
                    }
                    if((higherSensor.getDistance()<30.0&&higherSensor.getDistance()>=15.0) || (lowerSensor.getDistance()<30.0&&lowerSensor.getDistance()>=15.0)){                        
                        System.out.println("Object very close!!! In range from 0.15 meters to 0.3 meters..----->> Alarm!! Alarm!!");
                        Alarm.generateALarm(ledPin,30,8);
                        Alarm.generateALarm(buzzerPin,30,8);
                        continue;
                    }
                    System.out.println("Object detected inside 1.5 meters.. ----->> Alarm!!");
                    Alarm.generateALarm(ledPin,100,4);
                    Alarm.generateALarm(buzzerPin,100,4);
                }
                
            }
    //        System.exit(0);
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
