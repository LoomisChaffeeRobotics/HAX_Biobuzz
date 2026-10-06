package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Deadwheels {
    private GoBildaPinpointDriver pinpointDriver;
    private final DistanceUnit du = DistanceUnit.MM;
    private Pose2D position;

    private static final double offsetX = 0.0;
    private static final double offsetY = 0.0;

    Telemetry ddtelemetry;


    public void init(HardwareMap hardwareMap, Telemetry telemetry, double offsetX, double offsetY) {
        ddtelemetry = telemetry;
        pinpointDriver = hardwareMap.get(GoBildaPinpointDriver.class, "dwodom");
        pinpointDriver.recalibrateIMU();
        pinpointDriver.resetPosAndIMU();
        pinpointDriver.setOffsets(offsetX, offsetY, du);
        pinpointDriver.setEncoderResolution(100, du);
        pinpointDriver.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
    }

    public void updateDeadwheelPosition() {
        pinpointDriver.update();
        position = pinpointDriver.getPosition();
    }

    public Pose2D getDeadwheelPosition() {
        return position;
    }

    public void resetPosition(Pose2D positionInput) {
       position = positionInput;
    }
}
