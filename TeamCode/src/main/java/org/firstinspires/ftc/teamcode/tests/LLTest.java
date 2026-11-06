package org.firstinspires.ftc.teamcode.tests;

import org.firstinspires.ftc.teamcode.robot.RobotOpMode;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

public class LLTest extends RobotOpMode {
    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        super.loop();
        robot.drive.limelight.turnErrorWithTelemetry(Limelight.TagSet.RED_FRONT);
    }
}
