package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.OI;
import frc.robot.Constants;
import frc.robot.subsystems.ForkLiftSubsystem;
import frc.robot.subsystems.PulleySubsystem;
import frc.robot.subsystems.SwerveSubsystem;

public class AutoBalanceCommand extends DriveCommand{
    
public SwerveSubsystem swerveSubsystem;
public PulleySubsystem pulleySubsystem;
public static ForkLiftSubsystem forkLiftSubsystem;
public OI driveController;
public static OI pulleyStopController;
public CommandXboxController opController;
public static CommandXboxController miscController;
public double xSpeed;
public double ySpeed;
public double turningSpeed;
PIDController balanceController;
public boolean balanceDone = false;



public AutoBalanceCommand(SwerveSubsystem swerveSubsystem, OI driveController, CommandXboxController opController, PulleySubsystem pulleySubsystem, OI pulleyStopController) {
        super(swerveSubsystem, driveController, opController, pulleySubsystem, forkLiftSubsystem, miscController, pulleyStopController);
        this.swerveSubsystem = swerveSubsystem;
        this.pulleySubsystem = pulleySubsystem;


}



@Override
public void initialize() {

    balanceController = new PIDController(0.0375, 0, 0.02);
  

}

@Override
public void execute() {


swerveSubsystem.getRoll();

xSpeed = 0;
turningSpeed = 0;

ySpeed = (balanceController.calculate(swerveSubsystem.getRoll(), 0));
balanceController.setTolerance(1);



ChassisSpeeds chassisSpeeds;

chassisSpeeds = new ChassisSpeeds(ySpeed, xSpeed, turningSpeed);
                    swerveSubsystem.lights.set(0.57);


                    SwerveModuleState[] moduleStates = Constants.DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);
                    swerveSubsystem.setModuleStates(moduleStates);





if(swerveSubsystem.getRoll() < -50 || swerveSubsystem.getRoll() > 50) {
    swerveSubsystem.stopModules();
}

if(balanceController.atSetpoint()) {
    balanceDone = true;
    //lock wheels
    // swerveSubsystem.stopModules();
}






    }


   

     public void autoBalance() {
       initialize();
        execute();
        System.out.print("Execute ran");
    }

    


@Override
public boolean isFinished() {
    return balanceDone;
    // return false;
} 

@Override
public void end(boolean interrupted) {
    swerveSubsystem.stopModules();
    swerveSubsystem.lockWheels();
}


}
