package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Shooter RPM Test", group = "Test")
public class shooterTest extends OpMode {
    // Match the encoder conversion used by the shooter subsystem (28 ticks/rev).
    private static final double tpr2rpm = 60.0 / 28.0;
    private static final double RPM_STEP = 0.025;

    private DcMotorEx front;
    private DcMotorEx back;
    private double frontTargetRpm = 0.0;
    private double backTargetRpm = 0.0;

    @Override
    public void init() {
        front = hardwareMap.get(DcMotorEx.class, "front");
        back = hardwareMap.get(DcMotorEx.class, "back");
        telemetry.addLine("Front: D-pad up/down +/-5 RPM, x resets");
        telemetry.addLine("Back: Y/a +/-5 RPM, B resets");
        telemetry.addData("Motors", "front, back");
        telemetry.update();
    }

    @Override
    public void loop() {
        if (gamepad1.dpadUpWasPressed()) {
            frontTargetRpm += RPM_STEP;
        }
        if (gamepad1.dpadDownWasPressed()) {
            frontTargetRpm = frontTargetRpm - RPM_STEP;
        }
        if (gamepad1.xWasPressed()) {
            frontTargetRpm = 0.0;
        }

        if (gamepad1.yWasPressed()) {
            backTargetRpm += RPM_STEP;
        }
        if (gamepad1.aWasPressed()) {
            backTargetRpm = backTargetRpm - RPM_STEP;
        }
        if (gamepad1.bWasPressed()) {
            backTargetRpm = 0.0;
        }

        // DcMotorEx.setVelocity expects encoder ticks per second.
        front.setPower(frontTargetRpm);
        back.setPower(backTargetRpm);

        telemetry.addData("Front target RPM", frontTargetRpm);
        telemetry.addData("Front measured RPM", front.getVelocity() * tpr2rpm);
        telemetry.addData("Back target RPM", backTargetRpm);
        telemetry.addData("Back measured RPM", back.getVelocity() * tpr2rpm);
        telemetry.update();
    }

    @Override
    public void stop() {
        front.setVelocity(0.0);
        back.setVelocity(0.0);
    }
}
