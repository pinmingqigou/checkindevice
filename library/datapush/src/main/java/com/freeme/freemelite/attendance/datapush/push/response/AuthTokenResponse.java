package com.freeme.freemelite.attendance.datapush.push.response;

public class AuthTokenResponse {

    /**
     * result : ok
     * auth_token : xxxxx
     */

    private String result;
    private String auth_token;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    @Override
    public String toString() {
        return "AuthTokenResponse{" +
                "result='" + result + '\'' +
                ", auth_token='" + auth_token + '\'' +
                '}';
    }
}
