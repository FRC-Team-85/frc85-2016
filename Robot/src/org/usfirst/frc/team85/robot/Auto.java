package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Auto {
	private Drive _drive;
	private double lSpeed, rSpeed;
    
    public Auto(Drive drive) {        
        _drive = drive;
        lSpeed = 0;
        rSpeed = 0;
    }
}
