package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Auto {
	private TankDrive _drive;
	private double lSpeed, rSpeed;
    
    public Auto(TankDrive drive) {        
        _drive = drive;
        lSpeed = 0;
        rSpeed = 0;
    }
}