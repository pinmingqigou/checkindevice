package com.freeme.freemelite.attendance.datapush.push.request;

public class SinglePushRequestBody {

    /**
     * message : {"appkey":"GVvUv4M8FZAF7u5a9H79m6","is_offline":true,"offline_expire_time":10000000,"msgtype":"notification"}
     * notification : {"style":{"type":0,"text":"请填写通知内容","title":"请填写通知标题"},"transmission_type":true,"transmission_content":"透传内容"}
     * cid : 7d3fd7a8b1db11b830321fbe511848b3
     * requestid : 12111111111111111111111
     */

    private MessageBean message;
    private NotificationBean notification;
    private String cid;
    private String requestid;

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public NotificationBean getNotification() {
        return notification;
    }

    public void setNotification(NotificationBean notification) {
        this.notification = notification;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public static class MessageBean {
        /**
         * appkey : GVvUv4M8FZAF7u5a9H79m6
         * is_offline : true
         * offline_expire_time : 10000000
         * msgtype : notification
         */

        private String appkey;
        private boolean is_offline;
        private int offline_expire_time;
        private String msgtype;

        public String getAppkey() {
            return appkey;
        }

        public void setAppkey(String appkey) {
            this.appkey = appkey;
        }

        public boolean isIs_offline() {
            return is_offline;
        }

        public void setIs_offline(boolean is_offline) {
            this.is_offline = is_offline;
        }

        public int getOffline_expire_time() {
            return offline_expire_time;
        }

        public void setOffline_expire_time(int offline_expire_time) {
            this.offline_expire_time = offline_expire_time;
        }

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }
    }

    public static class NotificationBean {
        /**
         * style : {"type":0,"text":"请填写通知内容","title":"请填写通知标题"}
         * transmission_type : true
         * transmission_content : 透传内容
         */

        private StyleBean style;
        private boolean transmission_type;
        private String transmission_content;

        public StyleBean getStyle() {
            return style;
        }

        public void setStyle(StyleBean style) {
            this.style = style;
        }

        public boolean isTransmission_type() {
            return transmission_type;
        }

        public void setTransmission_type(boolean transmission_type) {
            this.transmission_type = transmission_type;
        }

        public String getTransmission_content() {
            return transmission_content;
        }

        public void setTransmission_content(String transmission_content) {
            this.transmission_content = transmission_content;
        }

        public static class StyleBean {
            /**
             * type : 0
             * text : 请填写通知内容
             * title : 请填写通知标题
             */

            private int type;
            private String text;
            private String title;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
