package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Deadwheels {
    private GoBildaPinpointDriver pinpointDriver;
    private final DistanceUnit du = DistanceUnit.INCH;
    private Pose2D position;

    private static final double offsetX = 0.0;
    private static final double offsetY = 0.0;


    HardwareMap hardwareMap;
    Telemetry telemetry;

    public Deadwheels(HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void init(double offsetX, double offsetY) {
        pinpointDriver.recalibrateIMU();
        pinpointDriver.resetPosAndIMU();
        pinpointDriver.setOffsets(offsetX, offsetY, du);
        pinpointDriver.setEncoderResolution(100, du);
        pinpointDriver.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
    }

    public void updateDeadwheelPosition() {
        pinpointDriver.update();
        position = pinpointDriver.getPosition();
        telemetry.addData("Position", position.toString(), "\n");
    }

    public Pose2D getDeadwheelPosition() {
        return position;
    }

    public void resetPosition(Pose2D positionInput) {
       position = positionInput;
    }
}
