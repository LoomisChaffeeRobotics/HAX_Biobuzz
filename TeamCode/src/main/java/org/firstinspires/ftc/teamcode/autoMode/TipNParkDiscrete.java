package org.firstinspires.ftc.teamcode.autoMode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@Autonomous
public class TipNParkDiscrete extends RobotOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(50.7, 8, 180);
    private final Pose path1 = poseFactory.of(11.5916, 94.2349, 180);
    private final Pose path1Control1 = poseFactory.of(15.149, 26.089, 180);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                instant(() -> {
                    robot.shooterP.setRPMByHand(2200); //FAR value
                }),
                waitMs(3500),
                instant(() -> {
                    robot.shooterP.flicker.setPosition(0.4);
                }),
                waitMs(100),
                instant(() -> {
                    robot.shooterP.flicker.setPosition(0.8);
                }),
                waitMs(1000),
                instant(() -> {
                    robot.shooterP.flicker.setPosition(0.4);
                }),
                waitMs(100),
                instant(() -> {
                    robot.shooterP.flicker.setPosition(0.8);                }),
                waitMs(1200),
                instant(() -> {
                    robot.shooterP.flicker.setPosition(0.4);
                }),
                waitMs(100),
                instant(() -> {
                    robot.shooterP.flicker.setPosition(0.8);
                }),
                waitMs(1200),
                instant(() -> {
                    robot.shooterP.flicker.setPosition(0.4);
                }),
                waitMs(100),
                instant(() -> {
                    robot.shooterP.flicker.setPosition(0.8);
                }),
                instant(() -> {
                    robot.shooterP.setRPMByHand(0);
                }),
                follow(follower, path1())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        super.init();
        robot.shooterP.manualOveride = true;
    }
    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop(){
        follower.update();
        Scheduler.execute();

        telemetry.addData("x", follower.pose().x());
        telemetry.addData("y", follower.pose().y());
        telemetry.addData("heading", follower.pose().heading());

        if (follower.currentPath() != null) {
            telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
            telemetry.addData("Path number", follower.pathIndex());
        }

        telemetry.update();
        super.loop();
    }


    public Path path1() {
        return Paths.curve(start, path1Control1, path1).constant(path1);
    }
}
