package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsysOrig.FancyPID;
@Config
@TeleOp(name = "Shooter RPM Test", group = "Test")
public class shooterTest extends OpMode {
    // Match the encoder conversion used by the shooter subsystem (28 ticks/rev).

    FtcDashboard dash = FtcDashboard.getInstance();
    Telemetry t2 = dash.getTelemetry();
//    private static final double tpr2rpm = 60.0 / 28.0;
//    private static final double RPM_STEP = 0.025;

    private DcMotorEx front;
    private DcMotorEx back;
    private double frontTargetPower = 0.0;
    private double backTargetTps = 0.0;

    public static double KP = 0;
    public static double ki = 0;
    public static double kd = 0;
    public static double kf = 0;

    FancyPID pid = new FancyPID();

    @Override
    public void init() {
        front = hardwareMap.get(DcMotorEx.class, "front");
        back = hardwareMap.get(DcMotorEx.class, "back");
        telemetry.addLine("Front: D-pad up/down +/-1 power, x resets");
        telemetry.addLine("Back: Y/a +/-5 RPM, B resets");
        telemetry.addData("Motors", "front, back");
        telemetry.update();
    }

    @Override
    public void loop() {


        // DcMotorEx.setVelocity expects encoder ticks per second.
        frontTargetPower += (gamepad1.dpadDownWasPressed() ? -.1:0) + (gamepad1.dpadUpWasPressed() ? .1:0);

        if (gamepad1.xWasPressed()){
            frontTargetPower = 0.0;
        }

        backTargetTps += (gamepad1.yWasPressed() ? -50:0) + (gamepad1.aWasPressed() ? 50:0);

        if (gamepad1.bWasPressed()){
            backTargetTps = 0.0;
        }



        front.setPower(frontTargetPower);

        pid.setTarget(backTargetTps);
        pid.setCoefficients(KP,ki,kd);
        pid.update(back.getVelocity());
        back.setPower(pid.getVelocity());

        telemetry.addLine("Front: D-pad up/down +/-.1 power, x resets");
        telemetry.addLine("Back: Y/a +/-5 RPM, B resets");
        telemetry.addData("Motors", "front, back");
        telemetry.update();

        t2.addData("Front Power: ", front.getPower());
        t2.addData("Back Speed: ", back.getVelocity());
        t2.addData("Back Target Velocity: ", backTargetTps);
        t2.addData("back power: ", back.getPower());
        t2.addData("pid p: ", KP);
        t2.addData("pidp motor: ",back.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).p);
        t2.update();
    }

    @Override
    public void stop() {
        front.setVelocity(0.0);
        back.setVelocity(0.0);
    }
}
