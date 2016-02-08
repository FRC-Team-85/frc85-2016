package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Auto {
	
	private TankDrive _drive;
/*
	private JoystickButton _controller2;
		_controller2 = Button;
*/
	private int obstacle /*= button.get*/;

	private int FORWARD = 0;

	private double _driveQuadEncoderPos;
	
	private double target;
	
	public Auto(TankDrive drive) {
        _drive = drive;
        //_driveQuadEncoderPos = _talons[1].getEncPosition();
		//Note: if we are not using the PID loops for driving, we can
		//connect the encoder directly to the roboRIO
        switch (obstacle) {
        case 0: //LowBar
/*
        	if (_driveQuadEncoderPos <= 3000) {
        		_drive.setMotors(FORWARD, FORWARD);
        	}
        	_drive.setBrakeMode(true);
*/
        	target = 1000;
            
        	break;
        case 1: //Portcullis


            break;
        case 2: //Cheval de Frise


            break;
        case 3: //Moat


            break;
        case 4: //Ramparts


        	break;
        case 5: //Drawbridge


            break;
        case 6: //Sally Port


            break;
        case 7: //Rock Wall


            break;
        case 8: //Rough Terrain

            break;
        }
        /*
        standard method for movement after clearing any of the obstacles, with clearance for 
        various overshoots per obstacles, input distance over barrier into enemy territory
         */
	}

	public void resetAutoDriveDist() {
		
	}
	
	public boolean autoDrive(double target) {
		if (/*target >= distance*/true) {
			return true;
		} 
		return false;
	}
	
}
