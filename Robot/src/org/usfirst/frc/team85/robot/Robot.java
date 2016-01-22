
package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    
	//Defines three controllers: Two for tank drive, one for operator
    private Joystick _driveStickLeft;
    private Joystick _driveStickRight;
    
    private Joystick _operatorStick;
    
    private TankDrive _drive;
    private TatorCannon _tatorCannon;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	//Sets up left controller
        _driveStickLeft = new Joystick(Addresses.DRIVESTICKLEFT);
        _driveStickRight = new Joystick(Addresses.DRIVESTICKRIGHT);
        _operatorStick = new Joystick(Addresses.OPERATORSTICK);
        
        _drive = new TankDrive(_driveStickLeft, _driveStickRight);
        _tatorCannon = new TatorCannon(_operatorStick);
        
        try {	//camera stream
        	CameraServer server = CameraServer.getInstance();
        	server.setQuality(50);
        	server.startAutomaticCapture();
    	} catch (Exception ex) {
    		System.out.println(ex);
    	}
    }
    
    public void autonomousInit() {
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	_tatorCannon.armCheck();
    }
    
    public void teleopInit() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	_tatorCannon.armCheck();
    	
    	_drive.drive(/*lol*/);
        
    }
    
}
