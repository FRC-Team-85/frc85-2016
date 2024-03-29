
package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;
//import java.util.logging.Level;

import org.usfirst.frc.team85.robot.Addresses.*;

//import com.sun.istack.internal.logging.Logger;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	/**
	 * https://www.ctr-electronics.com/Talon%20SRX%20Software%20Reference%20Manual.pdf
	 * Page 49
	 * This references the magnetic encoders we are using
	 * 4096 steps per rotation, 12 bits per rotation 
	 * From Brian: 162.974661726 steps per inch
	 * 			  1955.69594071 steps per ft
	 * From Nathan:  25.132741232 inches per rotation, or 2.094395102666666666 repeating ft/rotation
	 */
	
	//Defines two controllers: One for tank drive, one for operator
    private Joystick _driveStick;
    private Joystick _operatorStick;
    
    private Auto _auto;
    
    private TankDrive _drive;
    private Intake _intake;
    private TatorCannon _tatorCannon;

    private ImageProcessing _imageProcessing;
    
    private boolean _visionCentered;
    
    private CameraServer _server;

    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        _driveStick = new Joystick(CONTROLLERS.DRIVESTICK);
        _operatorStick = new Joystick(CONTROLLERS.OPERATORSTICK);

        _drive = new TankDrive(_driveStick);
        _intake = new Intake(_operatorStick);
        _tatorCannon = new TatorCannon(_operatorStick, _driveStick, _intake);
        
        _imageProcessing = new ImageProcessing();
        //
        _drive.initSafeCoding();
        _imageProcessing.initSafeCoding();
        //_tatorCannon.initSafeCoding();
        //
                
        try {
        	_server = CameraServer.getInstance();
        	_server.startAutomaticCapture();
        }
        catch(Exception ex) {
        	System.out.println(ex.toString());
        }
        
		if (true) {
			SmartDashboard.putNumber("Cannon Adjustment", 0);
			SmartDashboard.putNumber("autocase", 0);
			SmartDashboard.putBoolean("gwv", true);
			SmartDashboard.putBoolean("seekleft", false);				
		}
		if (true) {
			SmartDashboard.putNumber("autoMod 1", 0);
			SmartDashboard.putNumber("autoMod 2", 0);
			SmartDashboard.putNumber("autoMod 3", 0);
			SmartDashboard.putNumber("autoMod 4", 0);
			SmartDashboard.putNumber("autoMod 5", 0);
		}
        
    }

    public void autonomousInit() {
    	_auto = new Auto(_drive, _intake, _tatorCannon);
    	_intake.bootInit();
    	_tatorCannon.bootInit();
    	_drive.resetBothEncoders();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	if (_tatorCannon.init() && _intake.init()) {
    		_imageProcessing.process();
    		_auto.run();
    		_drive.showEncoderStuff();
    	}
    }

    public void teleopInit() {
        _drive.setVoltageRamp(0.0); //Removes voltage ramp limit
        _drive.setBrakeMode(false);
        _tatorCannon.runAs(CannonMode.MANUAL);
        _drive.manualDrive();
        _drive.resetBothEncoders();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
//    	_auto.checkSDB();
//	/*	
    	PowerMonitoring.Monitor();
    	_imageProcessing.process();
    	/*
    	if (_operatorStick.getRawButton(99)) {
    		if (_tatorCannon.armMove(TatorCannon.CLOSEFIRE) && _intake.intakeMove(Intake.HOME)) {
    			if (_drive.visionCenter()){
    				_tatorCannon.runAs(CannonMode.JUSTFIRE);
    			}
    		}
    		return;
    	}
    	*/
    	
    	if(_driveStick.getRawButton(2)) {
        	//Auto aim
        	if (_visionCentered || _drive.visionCenter()) {
        		_visionCentered = true;
        		_tatorCannon.runAs(CannonMode.JUSTFIRE);
        	} else {
        		_tatorCannon.runAs(CannonMode.MANUAL);
        	}
        } else  {
        	_visionCentered = false;
        	_drive.manualDrive();
        	_intake.run(true);
        	_tatorCannon.run(false);	//Always last, has priority control over intake
        }

		_drive.showEncoderStuff();

		_imageProcessing.muchSafeCoding();
    	_drive.muchSafeCoding();
    	_tatorCannon.muchSafeCoding();
    	
    	/*if (_driveStick.getRawButton(9)) {
        	_drive.visionCenter();
    		_tatorCannon.runAs(CannonMode.VISION);
    	}*/
    	
//	*/	
    }
    
    public void disabledInit() {
    	//System.out.println("Robot Init was disabled!!!");    	
    }
    
    public void disabledPeriodic() {
    	//System.out.println("Robot Periodic was disabled!!!"); 
    	_imageProcessing.process();
    }
    

}
