package org.firstinspires.ftc.teamcode.autoMode;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.sun.tools.javac.code.Attribute;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.deadline;
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

    private final Path toShootOne = Paths.line(startPose, shootPose).constant(startPose);
    private final Path toGardenPrep = Paths.line(shootPose, prepGardenPose).linear(shootPose, prepGardenPose)
            .with(Constants.foresightConfig.brakeAggression.at(1.5));
    private final Path intoGarden = Paths.line(prepGardenPose, inGardenPose).constant(inGardenPose)
            .with(Constants.foresightConfig.maxPathSpeed.at(0.4));
    private final Path backOffGarden = Paths.line(inGardenPose, prepGardenPose).constant(inGardenPose)
            .with(Constants.foresightConfig.brakeAggression.at(2.0));
    private final Path intoGardenTwo = Paths.line(robot.drive.getPose(), inGardenPose)
            .linear(robot.drive.getPose(), inGardenPose)
            .with(Constants.foresightConfig.maxPathSpeed.at(0.4));
    private final Path toShootTwo = Paths.line(inGardenPose, shootPose).linear(inGardenPose, shootPose);
    private final Path toFlowerOne = Paths.line(shootPose, flower1Pose).linear(startPose, flower1Pose);
    private final Path flowerOneIn = Paths.line(flower1Pose, flower1InPose).constant(flower1InPose)
            .with(Constants.foresightConfig.maxPathSpeed.at(0.5));
    private final Path toShootThree = Paths.line(flower1InPose, shootPose).constant(shootPose);
    private final Path toPark = Paths.line(robot.drive.getPose(), parkingPose).constant(robot.drive.getPose())
            .with(Constants.foresightConfig.brakeAggression.at(2.0));

    private final double delayMs = 0;
    private final double teammateMs = 1000;
    private final double forceParkTime = 26000;

    @Override
    public void init() {
        super.init();
        robot.drive.setPose(startPose);
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    private Command autoRoutine() {
        return sequential(
                ///FIRST SHOOTING
                deadline(
                waitMs(forceParkTime),
                ///SET DEADLINE FOR PARKING
                        sequential(
                instant(() -> {
                    robot.shooterP.setRPMByHand(1000);
                }),
                instant(() -> {
                    robot.shooterP.spinUp();
                }),
                robot.drive.followPath(toShootOne),
                waitMs(delayMs),
                //waitUntil(() -> robot.shooterP.getSpunUp() && robot.shooterN.getSpunUp()),
                instant(robot.shooterP::shoot),
                waitMs(4000),

                ///DRIVE TO GARDEN
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
                                }),
                                instant(() -> {
                                    robot.shooterP.spinUp();
                                }),
                                waitMs(1000),
                                ///WAIT FOR TIP
                                instant(robot.shooterP::shoot),
                                waitMs(4000),
                                parallel(
                                        robot.drive.followPath(toFlowerOne),
                                        instant(robot.shooterP::stopShoot),
                                        robot.intake.on()
                                ),
                                robot.intake.hingeDown(),
                                robot.drive.followPath(flowerOneIn),
                                waitMs(3000),
                                robot.drive.followPath(toShootThree),
                                instant(robot.shooterP::shoot),
                                waitMs(4000)

                )),
                parallel(
                        robot.drive.followPath(Paths.line(robot.drive.getPose(), parkingPose).constant(robot.drive.getPose())
                                .with(Constants.foresightConfig.brakeAggression.at(2.0))),
                        instant(robot.shooterP::stopShoot),
                        robot.intake.off()
                )
                ///GO PARK

        );
    }
}