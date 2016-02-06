
package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;

import org.usfirst.frc.team85.robot.Addresses.*;

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
    private Intake _intake;
    private TatorCannon _tatorCannon;

    private NetworkTable _table;

    ImageProcessing _imageProcessing;

    private AnalogInput a;
    
    int i = 0;

    private final static String[] GRIP_ARGS = new String[] {
            "/usr/local/frc/JRE/bin/java", "-jar",
            "/home/lvuser/grip.jar", "/home/lvuser/project.grip" };

        private final NetworkTable grip = NetworkTable.getTable("grip");
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	//Sets up left controller

        _driveStick = new Joystick(CONTROLLERS.DRIVESTICK);
        _operatorStick = new Joystick(CONTROLLERS.OPERATORSTICK);

        _table = NetworkTable.getTable("GRIP/myContoursReport");

        _drive = new TankDrive(_driveStick);
        _intake = new Intake(_operatorStick);
        _tatorCannon = new TatorCannon(_operatorStick, _intake);

        a = new AnalogInput(0);
        
        /* Run GRIP in a new process */
        try {
            Runtime.getRuntime().exec(GRIP_ARGS);
        } catch (IOException e) {
            e.printStackTrace();
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
    	//add the getpos for quadRatureEncoder here? -Matthew
    }

    public void teleopInit() {
        _drive.setVoltageRamp(0.0); //Removes voltage ramp limit
        _drive.setBrakeMode(false);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	//_tatorCannon.armCheck();

    	_drive.drive();
    	//_tatorCannon.run();

    	//_imageProcessing.process();
    	
    	i=(++i)%100;


    	SmartDashboard.putNumber("Value: ", a.getValue());
    	SmartDashboard.putNumber("Voltage: ", a.getVoltage());
    	SmartDashboard.putNumber("Wat:",  i);

    	System.out.println("Value: " + a.getValue());
    	System.out.println("Voltage: " + a.getVoltage());
    	System.out.println();

    }

}
