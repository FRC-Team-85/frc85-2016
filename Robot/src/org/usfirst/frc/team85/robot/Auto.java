package org.usfirst.frc.team85.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auto {
	

	//NOT FOR ACTUAL ROBOT USE
	//the following is for obtaining drive command order and values
	
	Timer _autoTimer;	//stage base cmd
	int commandSubStage;	//on command_ of command array
	double[] lt = {0,0.1};
	double[] rt = {0,0.1};
	double[] st = {0,15};
	boolean init = false;	//run init toggle for Timer reset
	boolean DB0 = false;
	boolean DB1 = false;
	boolean DB2 = false;
	double N0 = 0;
	double N1 = 0;
	double N2 = 0;
	//==============================================================
	
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
        
	public void run() {
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
	
	public void checkSDB() {//MAIN
		try {
			DB0 = SmartDashboard.getBoolean("DB/Button 0", false);
			DB1 = SmartDashboard.getBoolean("DB/Button 1", false);
			DB2 = SmartDashboard.getBoolean("DB/Button 2", false);
			N0 = SmartDashboard.getNumber("DB/Slider 0", 0);
			N1 = SmartDashboard.getNumber("DB/Slider 1", 0);
			N2 = SmartDashboard.getNumber("DB/Slider 2", 0);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		if (DB0) {//RUN
			if (!init) {
				commandSubStage = 0;
				init = true;
				_autoTimer.reset();
			}
			runCommands();
		} else {//PAUSE - END
			resetRun();
		}
		if (DB1) {//PULL
			addCommand(N0, N1, N2);
			SmartDashboard.putBoolean("DB/Button 1", false);
		} 
		if (DB2) {//CLEAR
			clearCommands();
			resetRun();
		}
		SmartDashboard.putNumber("DB/Slider 3", _autoTimer.get());
		putString();
	}
	
	public void resetRun() {
		commandSubStage = 0;
		init = false;
		SmartDashboard.putBoolean("DB/Button 0", false);
		SmartDashboard.putBoolean("DB/Button 1", false);
		SmartDashboard.putBoolean("DB/Button 2", false);
		DB0 = false;
		DB1 = false;
		DB2 = false;
		_drive.setMotors(0, 0);		
		return;
	}
	
	public void putString() {
		for (int i = 0; i < lt.length; i++) {
			SmartDashboard.putString("DB/String " + i,
					"lt: " + lt[i] +
					"rt: " + rt[i] +
					"stopAt: " + st[i]);			
		}
	}
	
	public void addCommand(double lTarget, double rTarget, double timeToStop) {
	
		int i = lt.length;

		double[] newLT = Arrays.copyOf(lt, i+1);
		double[] newRT = Arrays.copyOf(rt, i+1);
		double[] newST = Arrays.copyOf(st, i+1);
		
		newLT[i] = lTarget;
		newRT[i] = rTarget;
		newST[i] = timeToStop;
		
		lt = newLT;
		rt = newRT;
		st = newST;
	}
	
	public void clearCommands() {
		double[] poof = new double[] {0};
		lt = poof;
		rt = poof;
		st = poof;
	}
	
	public void runCommands() {
		// based on
		//for XXX comandArray[][]
		//commandSubStage++
		double lastLeftSet = _drive.getLeftAvgSpeed();
		double lastRightSet = _drive.getRightAvgSpeed();
		double leftTargetOutput = lt[commandSubStage];
		double rightTargetOutput = rt[commandSubStage];
		double timeToStop = st[commandSubStage];
		
		if (_autoTimer.get() > timeToStop) {
			if (commandSubStage < lt.length-1) {
				commandSubStage++;
			} else {
				resetRun();
			}
		} else {
			_drive.setMotors(
				lastLeftSet + 1.0*(leftTargetOutput - lastLeftSet),
				lastRightSet + 1.0*(rightTargetOutput - lastRightSet));
		}
		/*
		use .get to find last attempted motor.set
		ramp from there to target motor speeds
		until Timer.get > last command end + time for command
			then commandSubStage++
		 */
	}
	
}
