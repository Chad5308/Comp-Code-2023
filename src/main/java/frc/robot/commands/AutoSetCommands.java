/*package frc.robot.commands;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.PulleySubsystem;
import frc.robot.Constants;
import frc.robot.subsystems.PneumaticsSubsystem;

public class AutoSetCommands extends CommandBase {

    private final PulleySubsystem pulleySubsystem;
    private final PneumaticsSubsystem pneumaticsSubsystem;
    private CommandXboxController opController;
    DigitalInput extendLimitSwitch;
    DigitalInput retractLimitSwitch;


    public AutoSetCommands(CommandXboxController opController, PulleySubsystem pulleySubsystem, PneumaticsSubsystem pneumaticsSubsystem) {
        this.opController = opController;
        this.pulleySubsystem = pulleySubsystem;
        this.pneumaticsSubsystem = pneumaticsSubsystem;
        addRequirements(pulleySubsystem);
        addRequirements(pneumaticsSubsystem);
    }    


    @Override
    public void execute() {
        extendLimitSwitch = new DigitalInput(Constants.PullyConstants.extendSwitchstatePort);
        retractLimitSwitch = new DigitalInput(Constants.PullyConstants.retractSwitchstatePort);
        boolean retractSwitchstate = retractLimitSwitch.get();
        boolean extendSwitchstate = extendLimitSwitch.get();


        //full close reset
       opController.leftTrigger().onTrue(
            pulleySubsystem.homeCommand().
            alongWith(pneumaticsSubsystem.extensionRetractCommand().
            alongWith(pneumaticsSubsystem.intakeCloseCommand())));

       //auto mid shelf then reset
       opController.a().onTrue((
            pneumaticsSubsystem.extensionOutCommand().
            andThen(pulleySubsystem.midShelfCommand().
            andThen(pneumaticsSubsystem.intakeOpenCommand().
            andThen(pulleySubsystem.homeCommand().
            alongWith(pneumaticsSubsystem.extensionRetractCommand().
            alongWith(pneumaticsSubsystem.intakeCloseCommand())))))));

       //mid pole command
       opController.b().onTrue((
            pneumaticsSubsystem.extensionOutCommand().
            andThen(pulleySubsystem.midPoleCommand().
            andThen(pneumaticsSubsystem.intakeOpenCommand().
            andThen(pulleySubsystem.homeCommand().
            alongWith(pneumaticsSubsystem.extensionRetractCommand().
            alongWith(pneumaticsSubsystem.intakeCloseCommand())))))));

        opController.x().onTrue((
            pneumaticsSubsystem.extensionOutCommand().
            andThen(pulleySubsystem.topShelfCommand().
            andThen(pneumaticsSubsystem.intakeOpenCommand().
            andThen(pulleySubsystem.homeCommand().
            alongWith(pneumaticsSubsystem.extensionRetractCommand().
            alongWith(pneumaticsSubsystem.intakeCloseCommand())))))));

        opController.y().onTrue((
            pneumaticsSubsystem.extensionOutCommand().
            andThen(pulleySubsystem.topPoleCommand().
            andThen(pneumaticsSubsystem.intakeOpenCommand().
            andThen(pulleySubsystem.homeCommand().
            alongWith(pneumaticsSubsystem.extensionRetractCommand().
            alongWith(pneumaticsSubsystem.intakeCloseCommand())))))));


        if((extendSwitchstate = true) || (retractSwitchstate = true)) {
            pulleySubsystem.stopMotor();
        }

    
    }


}*/