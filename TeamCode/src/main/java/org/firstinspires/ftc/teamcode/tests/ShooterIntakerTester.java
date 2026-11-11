package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.data.LUT;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Config
@TeleOp
public class ShooterIntakerTester extends OpMode {
    // Match the encoder conversion used by the shooter subsystem (28 ticks/rev).

    FtcDashboard dash = FtcDashboard.getInstance();
    Telemetry t2 = dash.getTelemetry();
//    private static final double tpr2rpm = 60.0 / 28.0;
//    private static final double RPM_STEP = 0.025;

    private DcMotorEx feeder;
    private DcMotorEx shooter;
    private Servo flicker;
    private double shooterRpm = 0;
    private double intakePower = 0;
    private double FEEDER_RPM = 4500;
    private static final double tpr2rpm = 60.0 / 28.0;
    private DcMotor intake;

    public static LUT lut;

//    FancyPID pid = new FancyPID();

    @Override
    public void init() {
        feeder = hardwareMap.get(DcMotorEx.class, "feeder");
        shooter = hardwareMap.get(DcMotorEx.class, "shooterP");
        flicker = hardwareMap.get(Servo.class, "flicker");
        intake = hardwareMap.get(DcMotor.class, "intake");
        telemetry.addLine("Front: D-pad up/down +/-1 power, x resets");
        telemetry.addLine("Back: Y/a +/-5 RPM, B resets");
        telemetry.addData("Motors", "front, back");
        telemetry.update();
    }

    @Override
    public void loop() {


        // DcMotorEx.setVelocity expects encoder ticks per second.
        if (gamepad1.aWasPressed()){
            shooterRpm = (shooterRpm == 0 ? 2500.0: 0);
        }

        if (gamepad1.bWasPressed()){
            intakePower = (shooterRpm == 0 ? -0.6: 0);
        }



        intake.setPower(intakePower);
        feeder.setVelocity(FEEDER_RPM / tpr2rpm * (shooterRpm == 0? 0:1));
        shooter.setVelocity(shooterRpm / tpr2rpm);

        if (gamepad1.right_trigger > 0.5){
            flicker.setPosition(0.4); //down
        } else {
            flicker.setPosition(0.8); //up
        }





        telemetry.addLine("toggle shooter: a");
        telemetry.addLine("toggle intake: b");
        telemetry.addLine("shoot: RT");
        telemetry.update();

    }

    @Override
    public void stop() {
        feeder.setVelocity(0.0);
        shooter.setVelocity(0.0);
    }
}
