package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Auto {
	private TankDrive _drive;
	
	/*private JoystickButton _controller2;
		_controller2 = Button; */
	
	private int obstacle /*= button.get*/;
	
	public Auto(TankDrive drive) {
        _drive = drive;
        
        switch (obstacle) {
        case 0: //LowBar
            
        	//Drive(Fast)
        	
            break;
        case 1: //Portcullis
        
        	
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
	}
}
