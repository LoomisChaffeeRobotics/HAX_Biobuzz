package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Deadwheels {
    private GoBildaPinpointDriver pinpointDriver;
    private final DistanceUnit du = DistanceUnit.MM;
    private Pose2D position;

    Telemetry telemetry;

    public void init(float offsetX, float offsetY, int telemetryFrequency, Telemetry telemetryInput) {
        telemetry = telemetryInput;
        pinpointDriver.recalibrateIMU();
        pinpointDriver.resetPosAndIMU();
        pinpointDriver.setOffsets(offsetX, offsetY, du);
        pinpointDriver.setEncoderResolution(100, du);
        pinpointDriver.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        telemetry.setMsTransmissionInterval(telemetryFrequency);
    }

    public void updateDeadwheelPosition() {
        pinpointDriver.update();
        position = pinpointDriver.getPosition();
        telemetry.addData("Position", position.toString(), "\n");
    }

    public Pose2D getDeadwheelPosition() {
        return position;
    }
}
