
package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	//Defines three controllers: Two for tank drive, one for operator
    private Joystick _driveStick;

    private Joystick _operatorStick;

    private TankDrive _drive;
    private TatorCannon _tatorCannon;

    private NetworkTable _table;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	//Sets up left controller
        _driveStick = new Joystick(Addresses.DRIVESTICK);
        _operatorStick = new Joystick(Addresses.OPERATORSTICK);

        _table = NetworkTable.getTable("GRIP/myContoursReport");

        _drive = new TankDrive(_driveStick);
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
    	_drive.setVoltageRamp(2.0); //Limits controllers to 2V/sec
        _drive.setBrakeMode(true);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	_tatorCannon.armCheck();
    }

    public void teleopInit() {
        _drive.setVoltageRamp(0.0); //Removes voltage ramp limit
        _drive.setBrakeMode(false);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	_tatorCannon.armCheck();

    	_drive.drive();

    	try {
			double area = _table.getNumber("area", 0);
			if (area>0) {
				System.out.println("Area: " + area);
			} else {
				System.out.println("No Contour Found");
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
    }

}
