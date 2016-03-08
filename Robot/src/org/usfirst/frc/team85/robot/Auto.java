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
	private final boolean GOWITHOUTVISION;
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
        GOWITHOUTVISION = SmartDashboard.getBoolean("DB/Button 0", false);
        SEEKLEFT = SmartDashboard.getBoolean("DB/Button 1", false);
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
	        case 1: //LowBar =better code below
	        	switch (stage) {
	        	case 0:
	            	if (_intake.intakeMove(Intake.LOADPOS)) {
	            		stage++;
	            	}
	        		break;
	        	case 1:
	            	if (_cannon.armMove(TatorCannon.ALITTLEOFFTHEGROUND)) {
	            		rtns();
	            	}        	
	        		break;
	        	case 2:
	        		if (autoDrive(0.5, 0.5, 3)) {
	        			if (!GOWITHOUTVISION) {
	        				goVision();
	        			} else {
	        				rtns();
	        			}
	        		}
	        		break;
	        	case 3:
	        		autoDrive(0, 0, 15);
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
	        case 4://Moat =needs stop before vision
	        	switch(stage) {
	        	case 0:
	            	boolean c1 = _intake.intakeMove(Intake.AUTOANGLE);
	        		boolean c2 = (_intake.belowFortyFive()) ? 
	        				_cannon.armMove(TatorCannon.AUTOHEIGHT) : false;
	        			        		
	        		if (c1 && c2) {
	        			rtns();
	        		}
	        		break;
	        	case 1:
	        		if (autoDrive(0.65, 0.65, 4, 3)) {
	        			goVision();
	        		}
	        		break;
	        	}
	        	break;
		    //=====================================================================================
	        case 5://rampart =TOTALY DONE
	        	switch (stage) {
	        	case 0:
	            	boolean c1 = _intake.intakeMove(Intake.AUTOANGLE);
	        		boolean c2 = (_intake.belowFortyFive()) ? 
	        				_cannon.armMove(TatorCannon.AUTOHEIGHT) : false;
	        			        		
	        		if (c1 && c2) {
	        			rtns();
	        		}
	        		break;
	        	case 1:
	        		if (autoDrive(0.4, 0.4, 4, 1.5)) {
	        			rtns();
	        		}
	        		break;
	        	case 2:
	        		if (autoDrive(0.4, 0.6, 4, 1.5)) {
	        			rtns();
	        		}
	        		break;
	        	case 3:
	        		if (autoDrive(0, 0, 4, 1.5)) {
	        			goVision();
	        		}
	        	}
	        	break;
		    //=====================================================================================
	        case 6: //Drawbridge
	
	
	            break;
		    //=====================================================================================
	        case 7: //Sally Port
	
	
	            break;
		    //=====================================================================================
	        case 8://Rock Wall
	        	switch(stage) {
	        	case 0:
	            	boolean c1 = _intake.intakeMove(Intake.AUTOANGLE);
	        		boolean c2 = (_intake.belowFortyFive()) ? 
	        				_cannon.armMove(TatorCannon.AUTOHEIGHT) : false;
	        			        		
	        		if (c1 && c2) {
	        			rtns();
	        		}
	        		break;
	        	case 1:
	        		if (autoDrive(0.4, 0.4, 4, 3.5)){
	        			rtns();
	        		}
	        		break;
	        	case 2:
	        		if (autoDrive(0.7, 0.7, 6, 1.5)){
	        			goVision();
	        		}
	        		break;
//		       	case 3://not tested
//		       		if (autoDrive(0, 0, 6, 1)){
//		       			goVision();
//		        	}
//		        	break;
	        		
	        	}
	        	break;
		    //=====================================================================================
	        case 9://Rough Terrain =may need stop b vision
	        	switch(stage) {
	        	case 0:
	            	boolean c1 = _intake.intakeMove(Intake.AUTOANGLE);
	        		boolean c2 = (_intake.belowFortyFive()) ? 
	        				_cannon.armMove(TatorCannon.AUTOHEIGHT) : false;
	        			        		
	        		if (c1 && c2) {
	        			rtns();
	        		}
	        		break;
	        	case 1:
	        		if (autoDrive(0.4, 0.4, 5, 4)) {
	        			goVision();
	        		}
	        		break;
	        	}
	        	break;
	        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	        /*	
				CCCC	 AA 	SSSS	EEEE		 111	0000	0000	 ''		SSSS
				C		A  A	SS		EE			1 11	0  0	0  0	 ''		SS
				C		AA A	  SS	E			  11	0  0	0  0			  SS
				CCCC	A  A	SSSS	EEEE		1111	0000	0000	  		SSSS	
	         */	
	        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	       
	        case 100://low
	        	switch (stage) {
	        	
	        	case 0:
	            	boolean c1 = _intake.intakeMove(Intake.HORIZONTAL);
	        		boolean c2 = (_intake.belowFortyFive()) ? 
	        				_cannon.armMove(TatorCannon.ALITTLEOFFTHEGROUND) : false;
	        			        		
	        		if (c1 && c2) {
	        			rtns();
	        		}
	        		break;
	        	case 1:
	        		if (autoDrive(0.5, 0.5, 4, 4)) {
	        			if (!GOWITHOUTVISION) {
	        				goVision();
	        			} else {
	        				rtns();
	        			}
	        		}
	        		break;
	        	case 2:
	        		if (autoDrive(0, 0, 8, 1)) {
	        			rtns();
	        		}
	        		break;
	        	case 3:
	        		if (autoDrive(0.9, 0.1, 4, 2.75)) {
	        			goVision();
	        		}
	        		break;
	        	}
	        	
	        	break;
	        	
	        case 110://rampart
	        	switch (stage) {
	        	case 0:
	            	boolean c1 = _intake.intakeMove(Intake.AUTOANGLE);
	        		boolean c2 = (_intake.belowFortyFive()) ? 
	        				_cannon.armMove(TatorCannon.AUTOHEIGHT) : false;
	        			        		
	        		if (c1 && c2) {
	        			rtns();
	        		}
	        		break;
	        	case 1:
	        		if (autoDrive(0.4, 0.4, 4, 1.5)) {
	        			rtns();
	        		}
	        		break;
	        	case 2:
	        		if (autoDrive(
	        				0.5 - SmartDashboard.getNumber("DB/Slider 1", 0.1), 
	        				0.5 + SmartDashboard.getNumber("DB/Slider 1", 0.1),
	        				SmartDashboard.getNumber("DB/Slider 2", 4),
	        				SmartDashboard.getNumber("DB/Slider 3", 1.5))) {
	        			rtns();
	        		}
	        		break;
	        	case 3:
	        		if (autoDrive(0, 0, 4, 1.5)) {
	        			goVision();
	        		}
	        	}
	        	break;
	        	
	        case 120://wall
	        	switch(stage) {
	        	case 0:
	            	boolean c1 = _intake.intakeMove(Intake.AUTOANGLE);
	        		boolean c2 = (_intake.belowFortyFive()) ? 
	        				_cannon.armMove(TatorCannon.AUTOHEIGHT) : false;
	        			        		
	        		if (c1 && c2) {
	        			rtns();
	        		}
	        		break;
	        	case 1:
	        		if (autoDrive(0.4, 0.4, 4, 3.5)){
	        			rtns();
	        		}
	        		break;
	        	case 2:
	        		if (autoDrive(0.7, 0.7, 6, 1.5)){
	        			rtns();
	        		}
	        		break;
		       	case 3://not tested
		       		if (autoDrive(0, 0, 6, 1)){
		       			goVision();
		        	}
		        	break;
	        		
	        	}
	        	break;

	        case 130://rough
	        	switch(stage) {
	        	case 0:
	            	boolean c1 = _intake.intakeMove(Intake.AUTOANGLE);
	        		boolean c2 = (_intake.belowFortyFive()) ? 
	        				_cannon.armMove(TatorCannon.AUTOHEIGHT) : false;
	        			        		
	        		if (c1 && c2) {
	        			rtns();
	        		}
	        		break;
	        	case 1:
	        		if (autoDrive(0.4, 0.4, 5, 4)) {
	        			goVision();
	        		}
	        		break;
	        	}
	        	break;

	        case 140://moat
	        	switch(stage) {
	        	case 0:
	            	boolean c1 = _intake.intakeMove(Intake.AUTOANGLE);
	        		boolean c2 = (_intake.belowFortyFive()) ? 
	        				_cannon.armMove(TatorCannon.AUTOHEIGHT) : false;
	        			        		
	        		if (c1 && c2) {
	        			rtns();
	        		}
	        		break;
	        	case 1:
	        		if (autoDrive(0.65, 0.65, 4, 3)) {
	        			goVision();
	        		}
	        		break;
	        	}
	        	break;
	        	
	        case 40://moat
	        	switch(stage) {
	        	case 0:
	            	boolean c1 = _intake.intakeMove(Intake.AUTOANGLE);
	        		boolean c2 = (_intake.belowFortyFive()) ? 
	        				_cannon.armMove(TatorCannon.AUTOHEIGHT) : false;
	        			        		
	        		if (c1 && c2) {
	        			rtns();
	        		}
	        		break;
	        	case 1:
	        		if (autoDrive(
	        				SmartDashboard.getNumber("DB/Slider 1", 0.65), 
	        				SmartDashboard.getNumber("DB/Slider 1", 0.65),
	        				SmartDashboard.getNumber("DB/Slider 2", 4),
	        				SmartDashboard.getNumber("DB/Slider 3", 3))) {
	        			goVision();
	        		}
	        		break;
	        	}
	        	break;
	        case 200: //moat Zac
	        	if (autoDrive(0.5, 0.5, 3)) {
	        	}
	        	break;
	        case 420: //rockwall Matthew
	        	switch (stage) {
	        		case 1: 
	        			if (autoDrive(0.5, 0.5, 1.5)) {
	        				stage++;
	        			}
	        			break;
	        		case 2:
	        			if (autoDrive(0.9, 0.9, 2)) {
	        				
	        			}
	        			break;
	        	}
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
			if (!GOWITHOUTVISION) {
				_drive.ledToggle(true);
				if (!ImageProcessing.isVisionGone()) {
					if (_cannon.runAs(CannonMode.VISION) && _drive.visionCenter()) {
						
					}
				} else if (ImageProcessing.isVisionGone() && SEEKLEFT){
					autoDrive(-0.6, 0.6, 15);
				} else if (ImageProcessing.isVisionGone() && !SEEKLEFT){
					autoDrive(0.6, -0.6, 15);
				}
			} else {
				autoDrive(0, 0, 10, 15);
			}
		}
	}
	
	private void setChronicReferencePoint() {
		timerReference = _autoTimer.get();		
	}
	
	private void rtns() {//resetTimeNextStage
		timerReference = _autoTimer.get();		
		stage++;
	}
	
	private void goVision() {
		setChronicReferencePoint();
		readyForVision = true;
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
	double[] lt = {0,0.2};
	double[] rt = {0,0.2};
	double[] st = {0,15};
	boolean DB1 = false;
	boolean DB2 = false;
	boolean DB3 = false;
	double N1 = 0;
	double N2 = 0;
	double N3 = 0;
	//==============================================================
	
	public void runSDB() {
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
		if (DB1) {
			if(!init) {
				commandSubStage = 0;
				init = true;
				setChronicReferencePoint();
			}
			runCommands();
		} else {
			resetRun();
		}
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
		double lastLeftSet = _drive.getLeftSpeed();
		double lastRightSet = _drive.getRightSpeed();
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
