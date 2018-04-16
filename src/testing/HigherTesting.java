/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Level;
import java.util.logging.Logger;
import sensor.UltrasonicSensor;
import alarm.Alarm;
import rpi.PiGpioController;

/**
 *
 * @author HARSHIT
 */
public class HigherTesting {
    public static void main(String[] args) {
        UltrasonicSensor higherSensor=new UltrasonicSensor("HigherUltrasonic-Thread", RaspiPin.GPIO_04, RaspiPin.GPIO_05);
        GpioPinDigitalOutput buzzerPin=PiGpioController.getGpioController().provisionDigitalOutputPin(RaspiPin.GPIO_06);
        GpioPinDigitalOutput ledPin=PiGpioController.getGpioController().provisionDigitalOutputPin(RaspiPin.GPIO_03);
        try{           
            higherSensor.start();
            while(true){
                Thread.sleep(50);
                PiGpioController.getGpioController().shutdown();                                 
                    if(higherSensor.getDistance()<=50.0&&higherSensor.getDistance()>=30.0){                        
                        System.out.println("Inner if1");
                        Alarm.generateALarm(ledPin,50,6);
                        Alarm.generateALarm(buzzerPin,100,6);
                        continue;
                    }
                    if(higherSensor.getDistance()<30.0&&higherSensor.getDistance()>=15.0){                        
                        System.out.println("Inner if2");
                        Alarm.generateALarm(ledPin,30,8);
                        Alarm.generateALarm(buzzerPin,30,8);
                        continue;
                    }                    
                }
                
            }
         catch (InterruptedException ex) {
            Logger.getLogger(HigherTesting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
