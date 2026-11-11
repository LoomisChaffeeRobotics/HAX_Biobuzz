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

@Config
@TeleOp(name = "Shooter RPM Test", group = "Test")
public class ShooterTestWithRPM extends OpMode {
    // Match the encoder conversion used by the shooter subsystem (28 ticks/rev).

    FtcDashboard dash = FtcDashboard.getInstance();
    Telemetry t2 = dash.getTelemetry();
//    private static final double tpr2rpm = 60.0 / 28.0;
//    private static final double RPM_STEP = 0.025;

    private DcMotorEx feeder;
    private DcMotorEx shooterP;
    private Servo flicker;
    private double shooterRpm = 0;
    private double FEEDER_RPM = 4500;
    private static final double tpr2rpm = 60.0 / 28.0;

    public static double KP = 0;
    public static double ki = 0;
    public static double kd = 0;
    public static double kf = 0;

    public static LUT lut;

//    FancyPID pid = new FancyPID();

    @Override
    public void init() {
        feeder = hardwareMap.get(DcMotorEx.class, "feeder");
        shooterP = hardwareMap.get(DcMotorEx.class, "shooterP");
        flicker = hardwareMap.get(Servo.class, "flicker");
        telemetry.addLine("Front: D-pad up/down +/-1 power, x resets");
        telemetry.addLine("Back: Y/a +/-5 RPM, B resets");
        telemetry.addData("Motors", "front, back");
        telemetry.update();
    }

    @Override
    public void loop() {


        // DcMotorEx.setVelocity expects encoder ticks per second.
        shooterRpm += (gamepad1.dpadDownWasPressed() ? -100:0) + (gamepad1.dpadUpWasPressed() ? 100:0);

        if (gamepad1.xWasPressed()){
            shooterRpm = 0.0;
        }


        feeder.setVelocity(FEEDER_RPM / tpr2rpm* (gamepad1.b? 1:0));
        shooterP.setVelocity(shooterRpm / tpr2rpm * (gamepad1.b? 1:0));

        if (gamepad1.a){
            flicker.setPosition(0.4);
        }
        if (gamepad1.y){
            flicker.setPosition(0.8);
        }




        telemetry.addLine("distance: D-pad up/down +/- .1 meters, x resets");
        telemetry.addData("Motors", "front, back");
        telemetry.addLine("Back Target Velocity: 4500");
        telemetry.addData("target shooter RPM: ", shooterRpm);
        telemetry.addData("Front Speed: ", feeder.getVelocity()*tpr2rpm);
        telemetry.addData("Back Speed: ", shooterP.getVelocity()*tpr2rpm);
        telemetry.update();

        t2.addData("Front Speed: ", feeder.getVelocity()*tpr2rpm);
        t2.addData("Back Speed: ", shooterP.getVelocity()*tpr2rpm);
        t2.addLine("Back Target Velocity: 4500");
        t2.addData("back power: ", shooterP.getPower());
//        t2.addData("pid p: ", KP);
        t2.addData("pidp motor: ", shooterP.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).p);
        t2.update();
    }

    @Override
    public void stop() {
        feeder.setVelocity(0.0);
        shooterP.setVelocity(0.0);
    }
}
