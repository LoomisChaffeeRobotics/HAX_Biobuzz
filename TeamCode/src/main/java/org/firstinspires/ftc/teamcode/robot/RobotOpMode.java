package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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

    @Override
    public void stop() {
        // Only autonomous should replace the position handed off to teleop.
        // Initialization can fail before robot is assigned.
        if (getClass().isAnnotationPresent(Autonomous.class) && robot != null) {
            OpModeStorage.autonomousEndPose = robot.drive.getPose();
        }
    }
}
