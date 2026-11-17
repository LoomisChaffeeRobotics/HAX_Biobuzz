package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.data.LUT;

@Config
@TeleOp
public class ShooterPIDTest extends OpMode {
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

    double kP = 0;
    double kI = 0;
    double kD = 0;
    double kF = 0;
    private double value = 1.0;

    public static LUT lut;

//    FancyPID pid = new FancyPID();

    @Override
    public void init() {
        feeder = hardwareMap.get(DcMotorEx.class, "feeder");
        shooterP = hardwareMap.get(DcMotorEx.class, "shooterP");
        flicker = hardwareMap.get(Servo.class, "flicker");
        telemetry.addLine("left/right bumpers: change go down/up decimal place");
        telemetry.addLine("left trigger: toggle negative change");
        telemetry.addLine("up/right/doewn/left = kF, kP, kD, kI");
        telemetry.update();
    }

    @Override
    public void loop() {

        if (gamepad1.leftBumperWasPressed()){
            value /= 10;
        }
        if (gamepad1.rightBumperWasPressed()){
            value *= 10;
        }
        if (gamepad1.leftTriggerWasPressed()){
            value *= -1;
        }
        if (gamepad1.dpadUpWasPressed()){
            kF += value;
        }
        if (gamepad1.dpadRightWasPressed()){
            kP += value;
        }
        if (gamepad1.dpadDownWasPressed()){
            kI += value;
        }
        if (gamepad1.dpadLeftWasPressed()){
            kD += value;
        }




        shooterP.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,new PIDFCoefficients(kP,kI,kD,kF));
        feeder.setVelocity(4500 / tpr2rpm* (gamepad1.b? 1:0));
        shooterP.setVelocity(2200 / tpr2rpm * (gamepad1.b? 1:0));

        if (gamepad1.a){
            flicker.setPosition(0.4);
        }
        else {
            flicker.setPosition(.8);
        }





        telemetry.addData("Shooter Speed: ", shooterP.getVelocity()*tpr2rpm);
        telemetry.addData("Shooter target vel: ", 2200);
        telemetry.addData("f: ", kF);
        telemetry.addData("p: ", kP);
        telemetry.addData("i: ", kI);
        telemetry.addData("d: ", kD);
        telemetry.update();


        t2.addData("Shooter Speed: ", shooterP.getVelocity()*tpr2rpm);
        t2.addData("Shooter target vel: ", 2200);
        t2.addData("f: ", kF);
        t2.addData("p: ", kP);
        t2.addData("i: ", kI);
        t2.addData("d: ", kD);
        t2.update();
    }

    @Override
    public void stop() {
        feeder.setVelocity(0.0);
        shooterP.setVelocity(0.0);
    }

}

