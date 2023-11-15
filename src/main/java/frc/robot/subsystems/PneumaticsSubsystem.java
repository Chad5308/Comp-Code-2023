package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;


public class PneumaticsSubsystem extends SubsystemBase {
    

    //Grabber pneumatics
    private static DoubleSolenoid _intakeLeft = new DoubleSolenoid(1, PneumaticsModuleType.CTREPCM, Constants.PneumaticsConstants.L_INTAKE_IN, Constants.PneumaticsConstants.L_INTAKE_OUT);
    //Extension pneumatics
    private static DoubleSolenoid _extension = new DoubleSolenoid(1, PneumaticsModuleType.CTREPCM, Constants.PneumaticsConstants.EXTENSION_IN, Constants.PneumaticsConstants.EXTENSION_OUT);
    //FLipper pneumatics
   
    private Compressor mainCompressor = new Compressor(1, PneumaticsModuleType.CTREPCM);
    /** Creates a new Pnuematics. */
    public PneumaticsSubsystem() {
      _intakeLeft.set(Value.kForward);
      _extension.set(Value.kReverse);
      
    }
  
    
    public boolean intakeState = true;
    public boolean isDone = false;

  
    public void intakeOpen(){
      _intakeLeft.set(Value.kReverse);
      intakeState = false;
    }
  
    public void intakeClose(){
      _intakeLeft.set(Value.kForward);
      intakeState = true;
    }

    public boolean extensionState = false;

    public void extensionRetract(){
      _extension.set(Value.kReverse);
      extensionState = false;
    }

    public void extensionOut(){
       _extension.set(Value.kForward);
       extensionState = true;
    }



//all commands for the pneumatic subsystem

public CommandBase intakeOpenCommand() {
  return runOnce(
    () -> {
      isDone = false;
      intakeOpen();
      System.out.print("Intake Open Command Ran");
      isDone = true;
    }
  );
}

public CommandBase intakeCloseCommand() {
  return runOnce(
    () -> {
      isDone = false;
      intakeClose();
      System.out.print("Intake Close Command Ran");
      isDone = true;
    }
  );
}   


public CommandBase extensionRetractCommand() {
  return runOnce(
    () -> {
      isDone = false;
      extensionRetract();
      System.out.print("Extension Retract Command Ran");
      isDone = true;
    }
  );
}

public CommandBase extensionOutCommand() {
  return runOnce(
    () -> {
      isDone = false;
      extensionOut();
      System.out.print("Extension Out Command Ran");
      Commands.waitSeconds(2);
      isDone = true;
    }
  );
}




@Override
public void periodic() {


}


  }
