package com.freeme.freemelite.attendance.datapush.push.response;

public class PushResponse {

    /**
     * result : ok
     * taskid : RASS_0109_3ed7bcb9904a2d8208261d0c78a1e999
     * status : successed_offline
     */

    private String result;
    private String taskid;
    private String status;
    private String desc;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "PushResponse{" +
                "result='" + result + '\'' +
                ", taskid='" + taskid + '\'' +
                ", status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
