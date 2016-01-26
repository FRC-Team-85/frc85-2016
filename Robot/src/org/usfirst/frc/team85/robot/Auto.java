package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Auto {
	private TankDrive _drive;
	
	/* private JoystickButton _controller2;
		_controller2 = Button; */
	
	public Auto(TankDrive drive) {
        _drive = drive;
        switch (obstacle) {
        case 0://LowBar
            
            break;
        case 1://Portcullis
        
            break;
        case 2://Cheval de Frise
            
            break;
        case 3://Moat
        
            break;
            
        case 4://Ramparts
        
            break;
            
        case 5://Drawbridge
            
            break;
        case 6://Sally Port
            
            break;
        case 7://Rock Wall
            
            break;
        case 8://Rough Terrain
            
            break;
        
        switch(_procedure) {
        case 1 {
        	_leftFrontMotor.set(fast);
        	_rightFrontMotor.set(fast);
        	_leftBackMotor.set(fast);
        	_rightBackMotor.set(fast);
        }
        }
        
        
        
        
        
        // all of this is wrong and you should feel ashamed. senpai disapproves
        /* if (controller2.getRawButton(1) == true) {
        	LowBar.AutonomousLowBar();
		} else if (controller2.getRawButton(2) == true) {
			Portcullis.main();
		} else if (controller2.getRawButton(3) == true) {
			ChevalDeFrise.main();
		} else if (controller2.getRawButton(4) == true) {
			Moat.main();
		} else if (controller2.getRawButton(5) == true) {
			Ramparts.main();
		} else if (controller2.getRawButton(6) == true) {
			Drawbridge.main();
		} else if (controller2.getRawButton(7) == true) {
			SallyPort.main();
		} else if (controller2.getRawButton(8) == true) {
			RockWall.main();
		} else if (controller2.getRawButton(9) == true) {
			RoughTerrain.main(); 
		} */
	}
}
