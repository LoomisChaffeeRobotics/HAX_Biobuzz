package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static com.pedropathing.ivy.Scheduler.schedule;

public abstract class RobotOpMode extends OpMode {
    protected Robot robot;

    @Override
    public void init() {
        Scheduler.reset();
        robot = new Robot(this);

        schedule(
                robot.drive.periodic(),
                robot.shooterN.periodic(),
                robot.shooterP.periodic(),
                robot.intake.periodic()
        );
    }

    @Override
    public void init_loop() {
        Scheduler.execute();
    }

    @Override
    public void loop() {
        Scheduler.execute();
        robot.telemetry.update();
    }
}