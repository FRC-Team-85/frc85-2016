package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Auto {
	private TankDrive _drive;
	
	/*private JoystickButton _controller2;
		_controller2 = Button; */
	
	private int obstacle /*= button.get*/;
	
	private int FORWARD = 0;
	
	private double RATE = 1; //"1" is a place holder
	
	public Timer _timer;
	
	
	public Auto(TankDrive drive) {
        _drive = drive;
        _timer.start();
        _drive.setVoltageRamp(RATE);
        
        switch (obstacle) {
        case 0: //LowBar
        	
        	if (_timer.get() <= 15) {
        		_drive.setMotors(FORWARD, FORWARD);
        	} else {
        		_drive.setBrakeMode(true);
        	}
        	
            break;
        case 1: //Portcullis
        
        	
            break;
        case 2: //Cheval de Frise
            
        	
            break;
        case 3: //Moat
        	
        	if (_timer.get() <= 15) {
        		_drive.setMotors(FORWARD, FORWARD);
        	} else {
        		_drive.setBrakeMode(true);
        	}
        	
            break;
        case 4: //Ramparts
        	
        	if (_timer.get() <= 15) {
        		_drive.setMotors(FORWARD, FORWARD);
        	} else {
        		_drive.setBrakeMode(true);
        	}
            
        	break;
        case 5: //Drawbridge
        	
        	
            break;
        case 6: //Sally Port
            
        	
            break;
        case 7: //Rock Wall
            
        	if (_timer.get() <= 15) {
        		_drive.setMotors(FORWARD, FORWARD);
        	} else {
        		_drive.setBrakeMode(true);
        	}
        	
            break;
        case 8: //Rough Terrain
            
        	if (_timer.get() <= 15) {
        		_drive.setMotors(FORWARD, FORWARD);
        	} else {
        		_drive.setBrakeMode(true);
        	}
        	
            break;
        }
	}
}
