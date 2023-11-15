package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PneumaticsSubsystem;

public class PneumaticsCommands extends CommandBase {


public PneumaticsSubsystem pneumaticsSubsystem;
public boolean isDone2;
    
public PneumaticsCommands(PneumaticsSubsystem pneumaticsSubsystem) {
    this.pneumaticsSubsystem = pneumaticsSubsystem;
    addRequirements(pneumaticsSubsystem);
}




@Override
public void execute() {
    isDone2 = pneumaticsSubsystem.isDone;

    if(isDone2 = true && isScheduled()) {
       // isFinished();
        //System.out.print("Finished Pneumatics Command");
    }


}

@Override
public boolean isFinished() {
return false;
}

@Override
public void end(boolean interrupted) {

isDone2 = false;


}



}
