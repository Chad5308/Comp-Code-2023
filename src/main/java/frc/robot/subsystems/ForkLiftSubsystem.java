package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController; 
import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;

public class ForkLiftSubsystem extends SubsystemBase {
    
public SparkMaxPIDController forkLiftPIDController;
public CANSparkMax forkLiftMotor;


private static final double RAMP_RATE = 5;



public ForkLiftSubsystem(int forkMotor) {

forkLiftMotor = new CANSparkMax(forkMotor, MotorType.kBrushless);
forkLiftMotor.setIdleMode(IdleMode.kBrake);
forkLiftMotor.restoreFactoryDefaults();
forkLiftMotor.setOpenLoopRampRate(RAMP_RATE);


forkLiftPIDController = forkLiftMotor.getPIDController();
forkLiftPIDController.setP(0.005);

stopForkLift();
}

public void stopForkLift() {
forkLiftMotor.set(0);
}





public CommandBase dropForkCommand() {
    return run( 
        () -> {
            forkLiftMotor.set(-0.75);
    });
}

public CommandBase liftForkCommand() {
    return run( 
        () -> {
            forkLiftMotor.set(0.75);
    });
}

public CommandBase stopForkLiftCommand() {
    return runOnce( 
        () -> {
            forkLiftMotor.set(0);
    });
}


}
