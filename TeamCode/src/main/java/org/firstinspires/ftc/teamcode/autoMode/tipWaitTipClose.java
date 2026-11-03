package org.firstinspires.ftc.teamcode.autoMode;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import org.firstinspires.ftc.teamcode.pedro.Constants;


@Autonomous(name = "Tip Wait Tip Close", group = "Competition")
public class tipWaitTipClose extends RobotOpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose shootPose = poseFactory.of(57, 20, 180);
    private final Pose startPose = poseFactory.of(56, 8, 180);
    private final Pose prepGardenPose = poseFactory.of(27, 9, 185);
    private final Pose inGardenPose = poseFactory.of(9, 8, 185);
    private final Pose flower1Pose = poseFactory.of(17, 48, 180);
    private final Pose flower1InPose = poseFactory.of(10, 48, 180);
    private final Pose parkingPose = poseFactory.of(9, 94,180);
    private final Pose parkThroughPose = poseFactory.of(26, 39, 180);

    private final Path toShootOne = Paths.line(startPose, shootPose).constant(startPose);
    private final Path toGardenPrep = Paths.line(shootPose, prepGardenPose).linear(shootPose, prepGardenPose)
            .with(Constants.foresightConfig.brakeAggression.at(1.5));
    private final Path intoGarden = Paths.line(prepGardenPose, inGardenPose).constant(inGardenPose)
            .with(Constants.foresightConfig.maxPathSpeed.at(0.4));
    private final Path backOffGarden = Paths.line(inGardenPose, prepGardenPose).constant(inGardenPose)
            .with(Constants.foresightConfig.brakeAggression.at(2.0));
    private final Path toShootTwo = Paths.line(inGardenPose, shootPose).linear(inGardenPose, shootPose);
    private final Path toFlowerOne = Paths.line(shootPose, flower1Pose).linear(startPose, flower1Pose);
    private final Path flowerOneIn = Paths.line(flower1Pose, flower1InPose).constant(flower1InPose)
            .with(Constants.foresightConfig.maxPathSpeed.at(0.5));

    private final double delayMs = 0;
    private final double forceParkTime = 27500;
    private final double flowerEntryTimeoutMs = 500;

    @Override
    public void init() {
        super.init();
        robot.drive.setPose(startPose);
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    private void stopDrive() {
        robot.drive.follower.stop();
        robot.drive.follower.drivetrain.stop();
    }

    private Command followFromCurrentPose(Pose target) {
        // Build when this leg starts, not when the entire routine is scheduled.
        return lazy(() -> {
            Pose current = robot.drive.getPose();
            if (current.distance(target) < 1e-6) {
                return instant(() -> robot.drive.follower.hold(target));
            }
            return robot.drive.followPath(Paths.line(current, target)
                    .linear(current, target));
        });
    }

    private Command driveIntoFlower(Path path) {
        // Stop the flower entry after 500 ms, even if the wall blocks the endpoint.
        return race(
                waitMs(flowerEntryTimeoutMs),
                Command.build()
                        .setStart(() -> robot.drive.follower.follow(path))
                        .setDone(robot.drive.follower::atParametricEnd)
                        .setEnd(condition -> stopDrive())
        );
    }

    private Command autoRoutine() {
        return sequential(
                // Park when scoring finishes OR at 26 seconds, whichever comes first.
                race(
                        waitMs(forceParkTime),
                        sequential(
                                instant(() -> {
                                    robot.shooterP.setRPMByHand(1000);
                                    robot.shooterP.spinUp();
                                }),
                                robot.drive.followPath(toShootOne),
                                waitMs(delayMs),
                                instant(robot.shooterP::shoot),
                                waitMs(4000),

                                parallel(
                                        robot.drive.followPath(toGardenPrep),
                                        instant(robot.shooterP::stopShoot),
                                        robot.intake.on()
                                ),
                                robot.drive.followPath(intoGarden),
                                waitMs(1000),
                                robot.drive.followPath(backOffGarden),
                                robot.drive.followPath(Paths.line(robot.drive.getPose(), inGardenPose)
                                        .linear(robot.drive.getPose(), inGardenPose)
                                        .with(Constants.foresightConfig.maxPathSpeed.at(0.4))),
                                waitMs(1000),
                                robot.drive.followPath(toShootTwo),
                                instant(() -> {
                                    robot.shooterP.setRPMByHand(1000);
                                    robot.shooterP.spinUp();
                                }),
                                waitMs(1000),
                                instant(robot.shooterP::shoot),
                                waitMs(4000),

                                parallel(
                                        robot.drive.followPath(toFlowerOne),
                                        instant(robot.shooterP::stopShoot),
                                        robot.intake.on()
                                ),
                                robot.intake.hingeDown(),
                                driveIntoFlower(flowerOneIn),
                                // Separate intake dwell; the 500 ms limit only caps driving into the wall.
                                waitMs(3000),
                                followFromCurrentPose(shootPose),
                                instant(robot.shooterP::shoot),
                                waitMs(4000)
                        )
                ),
                instant(this::stopDrive),
                parallel(
                        lazy(() -> {
                            Pose current = robot.drive.getPose();
                            if (current.distance(parkingPose) < 1e-6) {
                                return instant(this::stopDrive);
                            }
                            return robot.drive.followPath(Paths.through(current, parkThroughPose, parkingPose).constant(current)
                                    .with(Constants.foresightConfig.brakeAggression.at(2.0)));
                        }),
                        instant(robot.shooterP::stopShoot),
                        robot.intake.off()
                ),
                instant(this::stopDrive)
        );
    }
}
