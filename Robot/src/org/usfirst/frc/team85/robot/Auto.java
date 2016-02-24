package org.usfirst.frc.team85.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	private final int 12FEET =  23468	//.35128852
	private final int 8FEET  =  15645	//.56752568
	private final int 6FEET  =  11734	//.17564426
	private final int 4FEET  =  7822	//.78376284
	private final int 3FEET  =  5867	//.08782213
	private final int 2FEET  =  3911	//.39188142
	private final int 1FOOT  =  1955	//.69594071
	private final int 8INCH  =  1303	//.79729381
	private final int 6INCH  =  977 	//.84797036
	private final int 4INCH  =  651 	//.89864690
	private final int 3INCH  =  488 	//.92398518
	private final int 2INCH  =  325 	//.94932345
	private final int 1INCH  =  162 	//.97466173
*/	
	private double _driveQuadEncoderPos;
	
	private double target;
	
	/*
	for the switch cases, add run commands based on found values - conclude with 0,0,- and camera command
	 */
	
	public Auto(TankDrive drive) {
        _drive = drive;
        //_driveQuadEncoderPos = _talons[1].getEncPosition();
		//Note: if we are not using the PID loops for driving, we can
		//connect the encoder directly to the roboRIO
	_autoTimer = new Timer();
	_autoTimer.start();
	}
        
public void run()        {
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
	
	//the following is for obtaining drive command order and values
	
	Timer _autoTimer;
	int commandSubStage;	//on command_ of command array
	double[][] commandArray;//current runlist from DB input
	
	public void checkSDB() {
		boolean run = SmartDashboard.getBoolean("DB/Button 1", false);
		if (run) {
			runCommands();
		}
		if (SmartDashboard.getBoolean("DB/Button 2", false)) {
			addCommand(
				SmartDashboard.getNumber("DB/Slider 1", 0),
				SmartDashboard.getNumber("DB/Slider 2", 0),
				SmartDashboard.getNumber("DB/Slider 3", 0));
			SmartDashboard.putBoolean("DB/Button 2", false);
		} 
		if (SmartDashboard.getBoolean("DB/Button 3", false)) {
			clearCommands();
			SmartDashboard.putBoolean("DB/Button 3", false);
		}
		if (SmartDashboard.getBoolean("DB/Button 4", false)) {
			commandSubStage = 0;
			SmartDashboard.putBoolean("DB/Button 4", false);
		}
		
		
	}
	
	public void putString() {
		for (int i = 0; i < commandArray[0].length; i++) {
			SmartDashboard.putString("DB/String 0",
					"lt: " + commandArray[0][i] +
					"rt: " + commandArray[1][i] +
					"stopAt: " + commandArray[2][i]);			
		}
	}
	
	public void addCommand(double lTarget, double rTarget, double timeToStop) {
	
		int i = commandArray[0].length;
		
		double[][] B = new double[3][i + 1];
		B[0] = Arrays.copyOf(commandArray[0], i);
		B[1] = Arrays.copyOf(commandArray[1], i);
		B[2] = Arrays.copyOf(commandArray[2], i);
		
		B[0][i] = lTarget;
		B[1][i] = rTarget;
		B[2][i] = timeToStop;
		
		commandArray = B;
	}
	
	public void clearCommands() {
		commandArray = new double[3][];
	}
	
	public void runCommands() {
		// based on
		//for XXX comandArray[][]
		//commandSubStage++
		double lastLeftSet = _drive.getLeftAvgSpeed();
		double lastRightSet = _drive.getRightAvgSpeed();
		double leftTargetOutput = commandArray[0][commandSubStage];
		double rightTargetOutput = commandArray[1][commandSubStage];
		double timeToStop = commandArray[2][commandSubStage];
		
		if (_autoTimer.get() > timeToStop) {
			commandSubStage++;
		} else {
			_drive.setMotors(
				lastLeftSet + 1.0*(leftTargetOutput -lastLeftSet),
				lastRightSet + 1.0*(rightTargetOutput -lastRightSet));
		}
		/*
		use .get to find last attempted motor.set
		ramp from there to target motor speeds
		until Timer.get > last command end + time for command
			then commandSubStage++
		 */
	}
	
}
