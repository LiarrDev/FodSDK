package com.fodsdk.net.response;

public class InitResult {
    private boolean success = false;
    private boolean showFloat = false;
    private boolean showMobileLogin = false;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isShowFloat() {
        return showFloat;
    }

    public void setShowFloat(boolean showFloat) {
        this.showFloat = showFloat;
    }

    public boolean isShowMobileLogin() {
        return showMobileLogin;
    }

    public void setShowMobileLogin(boolean showMobileLogin) {
        this.showMobileLogin = showMobileLogin;
    }

    @Override
    public String toString() {
        return "InitResult{" +
                "success=" + success +
                ", showFloat=" + showFloat +
                ", showMobileLogin=" + showMobileLogin +
                '}';
    }
}
