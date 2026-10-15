package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static MecanumConfig driveConfig = new MecanumConfig(
            c -> {
                c.frontLeftName.set("frontLeft");
                c.backLeftName.set("rearLeft");
                c.frontRightName.set("frontRight");
                c.backRightName.set("rearRight");

                c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
                c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
                c.manualBrakeMode.set(true);
            }
    );
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("dwodom");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(2.262948704516794);
        c.yPodOffset.set(1.2724199069766546);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.16444449899957225);
                Controller secondaryTranslationalForward = Controller.proportional(0.06075784834240253);
                Controller primaryTranslationalLateral = Controller.proportional(0.27273049323979537);
                Controller secondaryTranslationalLateral = Controller.proportional(0.10076662975910936);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.015796224112433813));
                c.brake.set(Controller.proportionalFeedforward(0.013426790495568742));

                c.headingFeedback.set(Controller.proportional(2.0803192899502343));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04169480185215761, 0.009399329283993157));

                c.linearBrakeCoefficients.set(Matrix.diag(0.04006002641554749, 0.08832397767305793));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0027473804605869, 0.0012784796635546783));

                c.maxAchievableForwardVelocity.set(64.1974785369564);
                c.maxAchievableStrafeVelocity.set(49.19595823508814);
                c.naturalForwardDeceleration.set(78.67256950679399);
                c.naturalStrafeDeceleration.set(91.57875916865756);
            }
    );
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, driveConfig),
                new Foresight(foresightConfig)
        );
    }

}