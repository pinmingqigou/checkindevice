package com.freeme.freemelite.attendance.machinehelper;

public class Config {
    public static class IntentConfig {
        public static final String INTENT_SELECT_WIFI_KEY = "intent_select_wifi_key";
        public static final String INTENT_SPECIFIED_EMPLOYEE_NAME_KEY = "intent_specified_employee_name_key";
        public static final String INTENT_SPECIFIED_EMPLOYEE_ID_KEY = "intent_specified_employee_id_key";
        public static final String INTENT_SPECIFIED_EMPLOYEE_FACE_STATE_KEY = "intent_specified_employee_face_state_key";
        public static final String INTENT_WIFI_CONNECTTED_KEY = "intent_wifi_connected_key";
    }

    public static class FaceState {
        //人脸未注册
        public static final int UNREGISTERED = 0;

        //人脸已注册
        public static final int REGISTERED = 1;
    }

    public static class PermissionRequest {
        public static final int LOCATION_PERMISSION_REQUEST_CODE = 5555;
        public static final int CAMERA_PERMISSION_REQUEST_CODE = 6666;
    }

    public static class CheckInDeviceState {
        public static final String CHECK_IN_DEVICE_NETWORK_CONNECTED_KEY = "check_in_device_network_connected_key";
    }
}
