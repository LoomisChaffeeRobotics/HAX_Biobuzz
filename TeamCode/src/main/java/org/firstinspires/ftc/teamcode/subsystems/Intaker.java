package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robot.Robot;

import static com.pedropathing.ivy.commands.Commands.*;
@Config
public class Intaker {
    private Mode mode = Mode.OFF;
    private hingeMode HINGEMODE = hingeMode.UP;
    public static double onPower = 1;
    public static double offPower = 0;
    public static double reversePower = -1;
    public static double upPos = 0;
    public static double downPos = 1;
    private final DcMotorEx intakeMotor;
    private final Servo hinge;
    private final Telemetry telemetry;

    public Intaker(Robot robot) {
        intakeMotor = robot.hardwareMap.get(DcMotorEx.class, "intake");
        hinge = robot.hardwareMap.get(Servo.class, "hinge");
        telemetry = robot.telemetry;
    }

    public Command on() {
        return instant(() -> mode = Mode.ON).requiring(intakeMotor);
    }

    public Command off() {
        return instant(() -> mode = Mode.OFF).requiring(intakeMotor);
    }

    public Command reverse() {
        return instant(() -> mode = Mode.REVERSE).requiring(intakeMotor);
    }

    public Command hingeDown() { return instant(() -> HINGEMODE = hingeMode.DOWN); }
    public Command hingeUp() { return instant(() -> HINGEMODE = hingeMode.UP); }
    public boolean isHingeDown(){ return (HINGEMODE == hingeMode.DOWN); }

    public Command periodic() {
        return infinite(() -> {
            switch (mode) {
                case ON:
                    intakeMotor.setPower(onPower);
                    break;
                case OFF:
                    intakeMotor.setPower(offPower);
                    break;
                case REVERSE:
                    intakeMotor.setPower(reversePower);
                    break;
            }
            switch (HINGEMODE){
                case UP:
                    hinge.setPosition(upPos);
                case DOWN:
                    hinge.setPosition(downPos);
            }

            telemetry.addData("Intake Current", intakeMotor.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Intake Velocity", intakeMotor.getVelocity());
        });
    }

    enum Mode {
        ON,
        OFF,
        REVERSE
    }
    enum hingeMode {
        UP,
        DOWN
    }
}