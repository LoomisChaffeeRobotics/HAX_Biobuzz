package org.firstinspires.ftc.teamcode.autoMode;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
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
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.Shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class twoPlusTwoRedClose extends OpMode {
    //--------------ROBOT IMPORTS-----------------------
    Shooter shooterPollen;
    Shooter shooterNectar;
    Intake intake;
    //---------------------ROBOT FUNCTIONS-----------------------
    private Command spinBoth(){
        shooterPollen.toggleSpinUp();
        shooterNectar.toggleSpinUp();
        return null;
    }
    private Command waitForSpin(){
        while (!shooterPollen.getSpinUp() || !shooterNectar.getSpinUp()){
            waitMs(1);
        }
        return null;
    }
    private Command setRPMS(double rpmP, double rpmN){
        shooterNectar.setRPMByHand(rpmN);
        shooterNectar.setRPMByHand(rpmP);
        return null;
    }
    private Command updateFuncs(){
        shooterPollen.update();
        shooterNectar.update();
        return null;
    }
    //-------------------PEDROPATHING CONSTANTS----------------------
    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees(); // add this

    private final Pose startPose = p.of(56, 8, 90);
    private final Pose parkPose = p.of(10.6, 96.2 ,90);
    private final Pose flower1Pose = p.of(17, 48, 180);
    private final Pose flower1INPose = p.of(10, 48, 180);
    private final Pose shootTwo = p.of(57, 123, 270);
    private final Pose shootTwoCnt1 = p.of(42, 45, 0);
    private final Pose shootTwoCnt2 = p.of(18, 99, 0);
    private final Pose flower2Pose = p.of(48, 123, 90);
    private final Pose flower2INPose = p.of(48, 132, 90);
    private final double delayMs = 0;
    //---------------------INDIVIDUAL PATHS--------------------
    private final Path toFlowerOne = Paths.line(startPose, flower1Pose).linear(startPose, flower1Pose);
    private final Path flower1In = Paths.line(flower1Pose, flower1INPose).constant(flower1INPose);
    private final Path shootTwoPt = Paths.curve(flower1INPose, shootTwoCnt1, shootTwoCnt2, shootTwo).linear(flower1INPose, shootTwo);
    private final Path toFlowerTwo = Paths.line(shootTwo, flower2Pose).linear(shootTwo, flower2Pose);
    private final Path flower2In = Paths.line(flower2Pose, flower2INPose).constant(flower2INPose);
    //----------------------AUTO ROUTE---------------------------

    Command auto = sequential(
            infinite(() -> updateFuncs()),

            instant(() -> setRPMS(1000, 1000)),
            instant(() -> spinBoth()),
            waitMs(delayMs),

            waitForSpin(),
            instant(() -> shooterPollen.shoot()),
            waitMs(4000),

            parallel(
                    follow(follower, toFlowerOne),
                    instant(() -> shooterPollen.setBlock()),
                    instant(() -> intake.spinIn())
            ),

            instant(() -> intake.toggleHinge()),
            follow(follower, flower1In),
            waitMs(3000),

            parallel(
                    follow(follower, shootTwoPt),
                    instant(() -> setRPMS(1000, 1000))
            ),

            waitForSpin(),
            instant(() -> shooterPollen.shoot()),
            waitMs(4000),

            parallel(
                    follow(follower, toFlowerTwo),
                    instant(() -> shooterPollen.setBlock()),
                    instant(() -> intake.spinIn())
            ),

            instant(() -> intake.toggleHinge()),
            follow(follower, flower1In),
            waitMs(3000)









    );

    @Override
    public void init() {
        //-----------------ROBOT ACTIONS-------------------
        shooterPollen = new Shooter();
        shooterNectar = new Shooter();
        intake = new Intake();

        shooterPollen.init(hardwareMap, "flickerP", "flywheelP");
        shooterNectar.init(hardwareMap, "flickerN", "flywheelN");

        //--------------------PEDRO ACTIONS-------------------------
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
    }
    @Override
    public void start() {
        schedule(auto);
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }
}

