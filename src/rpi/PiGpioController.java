/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

/**
 *
 * @author pi
 */
public class PiGpioController {
    
    private static final GpioController GPIO;
    static {
        GPIO=GpioFactory.getInstance();
    }
    public static GpioController getGpioController() {
        return GPIO;
    }   
}
