
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
	 */
	
	//Defines two controllers: One for tank drive, one for operator
    private Joystick _driveStick;
    private Joystick _operatorStick;

    private TankDrive _drive;
    private Intake _intake;
    private TatorCannon _tatorCannon;

    ImageProcessing _imageProcessing;

    private final static String[] GRIP_ARGS = new String[] {
            "/usr/local/frc/JRE/bin/java", "-jar",
            "/home/lvuser/grip.jar", "/home/lvuser/project.grip" };

    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        _driveStick = new Joystick(CONTROLLERS.DRIVESTICK);
        _operatorStick = new Joystick(CONTROLLERS.OPERATORSTICK);

        _drive = new TankDrive(_driveStick);
        _intake = new Intake(_operatorStick);
        _tatorCannon = new TatorCannon(_operatorStick,_driveStick, _intake);

        /* Run GRIP in a new process */
/*        try {
            Runtime.getRuntime().exec(GRIP_ARGS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Setup NetworkTable\
        try {
        	NetworkTable.setClientMode();
        	NetworkTable.setIPAddress("roborio-85-frc.local");
        	_table = NetworkTable.getTable("grip");
        } catch(Exception ex) {
        	System.out.println(ex.toString());
        }
*/    }

    public void autonomousInit() {
    	_drive.setVoltageRamp(2.0); //Limits controllers to 2V/sec
        _drive.setBrakeMode(true);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
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
    	//_imageProcessing.process();
  		_drive.drive();
    	_intake.run(true);
    	_tatorCannon.run(false);
    }
    
    public void disabledInit() {
    	//System.out.println("Robot Init was disabled!!!");    	
    }
    
    public void disabledPeriodic() {
    	//System.out.println("Robot Periodic was disabled!!!"); 
    }

}
