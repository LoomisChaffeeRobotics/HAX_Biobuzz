package org.firstinspires.ftc.teamcode.autoMode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@Autonomous
public class CoopFar extends RobotOpMode {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(28.0694, 132.9327, 0);
    private final Pose path1 = poseFactory.of(36.2686, 128.4537, 27);
    private final Pose point2Start = poseFactory.of(36.2686, 128.4537, 27);
    private final Pose point2 = poseFactory.of(47.7709, 127.0008, 90);
    private final Pose point3 = poseFactory.of(47.1403, 133.0736, 90);
    private final Pose point4Start = poseFactory.of(47.1403, 133.0736, 90);
    private final Pose point4 = poseFactory.of(58.6035, 117.8195, 0);
    private final Pose point4Control1 = poseFactory.of(45.3014, 112.6388, 0);
    private final Pose point5 = poseFactory.of(10.5782, 120.7538, 0);

    public Path readyToShoot1() {
        return Paths.line(start, path1).linear(start, path1);
    }

    public Path readyToIntake() {
        return Paths.line(point2Start, point2).linear(point2Start, point2);
    }

    public Path intakeIn() {
        return Paths.line(point2, point3).constant(point3);
    }

    public Path readyToShoot2() {
        return Paths.curve(point4Start, point4Control1, point4).linear(point4Start, point4);
    }

    public Path park() {
        return Paths.line(point4, point5).constant(point5);
    }


    boolean onRed = true;

    @Override
    public void init() {
        super.init();
        robot.drive.setPose(start);
    }
    @Override
    public void start() {
        schedule(autoRoutine());
    }

    private Command autoRoutine() {
        return sequential(
                robot.drive.followPath(readyToShoot1()),
                instant(() -> {
                    robot.shooterP.setRPMByHand(1100); //FAR value
                    robot.shooterP.spinUp();
                }),
                waitUntil(() -> robot.localization.seesTag(onRed))
                        .with(waitMs(3000)),//spin up time
                instant(() -> robot.shooterP.shoot()),
                waitMs(5000), //shoot time
                instant(() -> robot.shooterP.spinDown()),
                robot.drive.followPath(readyToIntake())
                        .with(instant(() -> robot.intake.hingeDown()))
                        .with(robot.intake.on()),
                robot.drive.followPath(intakeIn()),
                waitMs(2000), //time to intake
                robot.drive.followPath(readyToShoot2())
                        .with(instant(() -> {
                    robot.shooterP.setRPMByHand(1000); //CLOSE value
                    robot.shooterP.spinUp();
                })),
                instant(() -> robot.shooterP.shoot()),
                waitMs(5000), //shoot time
                instant(() -> robot.shooterP.spinDown()),
                robot.drive.followPath(park()).with()
        );
    }
}
