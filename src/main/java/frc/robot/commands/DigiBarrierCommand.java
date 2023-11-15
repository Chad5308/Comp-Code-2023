package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.OI;
import frc.robot.subsystems.ForkLiftSubsystem;
import frc.robot.subsystems.PulleySubsystem;
import frc.robot.subsystems.SwerveSubsystem;

public class DigiBarrierCommand extends CommandBase{
    
    private final SwerveSubsystem swerveSubsystem;
    private final OI driveController;
    public PulleySubsystem pulleySubsystem;
    public ForkLiftSubsystem forkLiftSubsystem;
    public DriveCommand driveCommand;

    public final CommandXboxController opController;
    public final CommandXboxController miscController;

    public boolean DigitalBarrierOnOff = false;

    
   

   

public DigiBarrierCommand(SwerveSubsystem swerveSubsystem, OI driveController, PulleySubsystem pulleySubsystem, ForkLiftSubsystem forkLiftSubsystem, CommandXboxController opController, CommandXboxController miscController, DriveCommand driveCommand) {
    this.swerveSubsystem = swerveSubsystem;
    this.pulleySubsystem = pulleySubsystem;
    this.driveCommand = driveCommand;
    addRequirements(swerveSubsystem);
    
    this.miscController = miscController;
    this.driveController = driveController;
    this.opController = opController;


}

@Override 
public void initialize(){

}

@Override
public void execute() {

if(DigitalBarrierOnOff = true) {

swerveSubsystem.getPose();

//Slow Downs (Meters)
if(swerveSubsystem.getPose().getX() > 1.8288 || swerveSubsystem.getPose().getX() < -1.8288) {
driveCommand.xSpeed = Math.abs(driveCommand.xSpeed) < 0.5 ? driveCommand.xSpeed : 0.5;
}

if(swerveSubsystem.getPose().getY() > 0.6096 || swerveSubsystem.getPose().getY() < -0.6096) {
    driveCommand.xSpeed = Math.abs(driveCommand.ySpeed) < 0.3 ? driveCommand.ySpeed : 0.3;
}
//End


//Max Values (Meters)
    //Y Zone
if(swerveSubsystem.getPose().getY() > 0.762) {
    driveCommand.ySpeed = driveCommand.ySpeed < 0 ? driveCommand.ySpeed : 0;
}
if(swerveSubsystem.getPose().getY() < -0.762) {
    driveCommand.ySpeed = driveCommand.ySpeed > 0 ? driveCommand.ySpeed : 0;
}

    //X Zone
if(swerveSubsystem.getPose().getX() > 2.286) {
    driveCommand.xSpeed = driveCommand.xSpeed < 0 ? driveCommand.xSpeed : 0;
}
if(swerveSubsystem.getPose().getX() < -2.286) {
    driveCommand.xSpeed = driveCommand.xSpeed > 0 ? driveCommand.xSpeed : 0;

}
//End

} else {
    System.out.println("DIGI BARRIER OFF");
}



}


@Override
    public void end(boolean interrupted) {
    
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}