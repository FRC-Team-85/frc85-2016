package org.usfirst.frc.team85.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auto {
		
	/*
	for the switch cases, add run commands based on found values - conclude with 0,0,- and camera command
	 */

		Timer _autoTimer;	
	private boolean timerInit;
	private double whatTimeIsIt;

	private double timerReference;
	
		private TankDrive _drive;
	private final double DEFAULTRAMPRATE = 4.0;
	
		private Intake _intake;
	
		private TatorCannon _cannon;
	
	private final int OBSTACLE;
	private final boolean SEEKLEFT;
	
	boolean readyForVision;
	private int stage = 0;
	
	public Auto(TankDrive drive, Intake intake, TatorCannon cannon) {
        _drive = drive;
    	_drive.setVoltageRamp(DEFAULTRAMPRATE); //Limits controllers to _V/sec
        _drive.setBrakeMode(true);
        _intake = intake;
        _cannon = cannon;
        OBSTACLE = (int) SmartDashboard.getNumber("DB/Slider 0", 0);
        SEEKLEFT = SmartDashboard.getBoolean("DB/Button 0", false);
        _autoTimer = new Timer();
        _autoTimer.start();
	}
        
	public void run() {
		if (!timerInit) {
			_autoTimer.reset();
			timerInit = true;
			return;
		}
		
		whatTimeIsIt = _autoTimer.get();
		SmartDashboard.putString("DB/String 0", whatTimeIsIt + "sec");
		SmartDashboard.putString("DB/String 1", "We are at stage " + stage);
    	SmartDashboard.putString("DB/String 2", "Ready for Vision? " + readyForVision);
		if (!readyForVision) {
	        switch (OBSTACLE) {
	        //=====================================================================================
	        case 0: //DEAD
	        	break;
	        //=====================================================================================
	        case 1: //LowBar
	        	switch (stage) {
	        	case 0:
	            	if (_intake.intakeMove(Intake.LOADPOS)) {
	            		stage++;
	            	}
	        		break;
	        	case 1:
	            	if (_cannon.armMove(TatorCannon.ALITTLEOFFTHGROUND)) {
	            		setChronicReferencePoint();
	            		stage++;
	            	}        	
	        		break;
	        	case 2:
	        		if (autoDrive(0.5, 0.5, 3)) {
	        			stage++;
	        		}
	        		break;
	        	case 3:
	        		if (autoDrive(-0.25, -0.25, 1)) {
	        			stage = 99;
	        		}
	        		break;
	        	case 99:
	        		setChronicReferencePoint();
	        		readyForVision = true;
	        		break;
	        	}
	        	break;
		    //=====================================================================================
	        case 2: //Portcullis
	
	            break;
		    //=====================================================================================
	        case 3: //Cheval de Frise
	
	
	            break;
		    //=====================================================================================
	        case 4: //Moat
	
	
	            break;
		    //=====================================================================================
	        case 5: //Ramparts
	
	
	        	break;
		    //=====================================================================================
	        case 6: //Drawbridge
	
	
	            break;
		    //=====================================================================================
	        case 7: //Sally Port
	
	
	            break;
		    //=====================================================================================
	        case 8: //Rock Wall
	
	
	            break;
		    //=====================================================================================
	        case 9: //Rough Terrain
	
	            break;
	        
	        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	        /*	
				CCCC	 AA 	SSSS	EEEE		 111	0000	0000	 ''		SSSS
				C		A  A	SS		EE			1 11	0  0	0  0	 ''		SS
				C		AA A	  SS	E			  11	0  0	0  0			  SS
				CCCC	A  A	SSSS	EEEE		1111	0000	0000	  		SSSS	
	         */	
	        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	        case 100:
	        	
	        	break;
	        	
	        //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	        	
	        case -1:
	        	runSDB();
	        	break;
	        	
	        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	        /*	
				CCCC	 AA 	SSSS	EEEE		CCCC	L		OOOO	SSSS	EEEE	DDD	
				C		A  A	SS		EE			C		L		O  O	SS		EE		D  D
				C		AA A	  SS	E			C		L		O  O	  SS	E		D  D
				CCCC	A  A	SSSS	EEEE		CCCC	LLLL	OOOO	SSSS	EEEE	DDD	
	         */	
	        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	        }
		} else {
	        /*
	        standard method for movement after clearing any of the obstacles
	         */
			_drive.ledToggle(true);
			if (ImageProcessing.contourFound) {
				if (_cannon.runAs(CannonMode.VISION) && _drive.visionCenter()) {
					
				}
			} else if (!ImageProcessing.contourFound && SEEKLEFT){
				autoDrive(-0.6, 0.6, 15);
			} else if (!ImageProcessing.contourFound && !SEEKLEFT){
				autoDrive(0.6, -0.6, 15);
			}
		}
	}
	
	private void setChronicReferencePoint() {
		timerReference = _autoTimer.get();		
	}

	private boolean autoDrive(double lTarget, double rTarget, double time) {
		_drive.setVoltageRamp(DEFAULTRAMPRATE);
		if (whatTimeIsIt >= timerReference && whatTimeIsIt < (timerReference + time)) {
			_drive.setMotors(-lTarget, -rTarget);
			return false;
		} else {
			return true;
		}
	}
	
	private boolean autoDrive(double lTarget, double rTarget, double rampRate, double time) {
		_drive.setVoltageRamp(rampRate);
		if (whatTimeIsIt >= timerReference && whatTimeIsIt < (timerReference + time)) {
			_drive.setMotors(-lTarget, -rTarget);
			return false;
		} else {
			return true;
		}
	}
	
	
	//============================================================================


	//NOT FOR ACTUAL ROBOT USE
	//the following is for obtaining drive command order and values
	
	boolean init;
	int commandSubStage;	//on command_ of command array
	double[] lt = {0,0.1};
	double[] rt = {0,0.1};
	double[] st = {0,15};
	boolean DB1 = false;
	boolean DB2 = false;
	boolean DB3 = false;
	double N1 = 0;
	double N2 = 0;
	double N3 = 0;
	//==============================================================
	
	public void runSDB() {
		
	}
	
	
	public void checkSDB() {//MAIN
		try {
			DB1 = SmartDashboard.getBoolean("DB/Button 1", false);
			DB2 = SmartDashboard.getBoolean("DB/Button 2", false);
			DB3 = SmartDashboard.getBoolean("DB/Button 3", false);
			N1 = SmartDashboard.getNumber("DB/Slider 1", 0);
			N2 = SmartDashboard.getNumber("DB/Slider 2", 0);
			N3 = SmartDashboard.getNumber("DB/Slider 3", 0);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		if (DB1) {//RUN
			if (!init) {
				commandSubStage = 0;
				init = true;
				_autoTimer.reset();
			}
			runCommands();
		} else {//PAUSE - END
			resetRun();
		}
		if (DB2) {//PULL
			addCommand(N1, N2, N3);
			SmartDashboard.putBoolean("DB/Button 2", false);
		} 
		if (DB3) {//CLEAR
			clearCommands();
			resetRun();
		}
		putString();
	}
	
	public void resetRun() {
		commandSubStage = 0;
		init = false;
		SmartDashboard.putBoolean("DB/Button 1", false);
		SmartDashboard.putBoolean("DB/Button 2", false);
		SmartDashboard.putBoolean("DB/Button 3", false);
		DB1 = false;
		DB2 = false;
		DB3 = false;
		_drive.setMotors(0, 0);		
		return;
	}
	
	public void putString() {
		for (int i = 1; i < lt.length+1; i++) {
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
