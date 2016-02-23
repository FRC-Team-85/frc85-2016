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
/*
	//various distances of int and jazz
	private final int 12FEET = 23468	//.35128852
	private final int 8FEET  = 15645 	//.56752568
	private final int 6FEET  = 11734 	//.17564426
	private final int 4FEET  = 7822		//.78376284
	private final int 3FEET  = 5867		//.08782213
	private final int 2FEET  = 3911		//.39188142
	private final int 1FOOT  = 1955		//.69594071
	private final int 8INCH  = 1303		//.79729381
	private final int 6INCH  = 977		//.84797036
	private final int 4INCH  = 651		//.89864690
	private final int 3INCH  = 488		//.92398518
	private final int 2INCH  = 325		//.94932345
	private final int 1INCH  = 162		//.97466173
*/	
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
/*
        	if (_driveQuadEncoderPos <= 3000) {
        		_drive.setMotors(FORWARD, FORWARD);
        	}
        	_drive.setBrakeMode(true);
        	intakeMove(OPENDOOR);
        	//_drive.setMotors(1INCH, 1INCH);
        	//intakeMove(HOME);
        	//_drive.setMotors(FORWARD, FORWARD);
*/
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
