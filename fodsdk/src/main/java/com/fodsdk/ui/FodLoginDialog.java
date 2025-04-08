package com.fodsdk.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fodsdk.net.FodRepository;
import com.fodsdk.utils.ResourceUtil;
import com.fodsdk.utils.ToastUtil;

public class FodLoginDialog extends FodBaseDialog {

    private boolean isRegister = false;     // 登录还是注册
    private RadioGroup radioGroup;
    private RadioButton rbAccount, rbSms;
    private View accountLoginLayout, accountRegisterLayout, smsLoginLayout;
    private EditText etLoginAccount, etLoginPassword, etRegisterAccount, etRegisterPassword, etConfirmPassword, etMobile, etSmsCode;
    private Button btnAccountLogin, btnAccountRegister, btnGetSms, btnSmsLogin;
    private TextView tvAccountRegister, tvAccountLogin;
    private final FodRepository repo;

    public FodLoginDialog(Context context, FodRepository repo) {
        super(context);
        this.repo = repo;
    }

    @Override
    protected void initViews(View rootView) {
        initComponents(rootView);
        radioGroup.setOnCheckedChangeListener((new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbAccount.getId()) {
                    if (isRegister) {
                        showAccountRegisterLayout();
                    } else {
                        showAccountLoginLayout();
                    }
                } else if (checkedId == rbSms.getId()) {
                    showSmsLoginLayout();
                }
            }
        }));
        radioGroup.check(rbAccount.getId());

        btnAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAccountLogin();
            }
        });
        tvAccountRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRegister = true;
                rbAccount.setText(ResourceUtil.getStringId("fod_account_register"));
                showAccountRegisterLayout();
            }
        });

        btnAccountRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etRegisterAccount.clearFocus();
                etConfirmPassword.clearFocus();
                etRegisterPassword.clearFocus();
                doAccountRegister();
            }
        });
        tvAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRegister = false;
                rbAccount.setText(ResourceUtil.getStringId("fod_account_login"));
                showAccountLoginLayout();
            }
        });

        btnGetSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSmsCode();
            }
        });
        btnSmsLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSmsLogin();
            }
        });
    }

    private void doAccountLogin() {
        String account = etLoginAccount.getText().toString();
        String password = etLoginPassword.getText().toString();
        if (account.length() < 6 || password.length() < 6) {
            ToastUtil.show("账号或密码长度不能小于 6 位");
            return;
        }
        repo.accountLogin(account, password);
    }

    private void doAccountRegister() {
        String account = etRegisterAccount.getText().toString();
        String registerPassword = etRegisterPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        if (account.length() < 6 || registerPassword.length() < 6) {
            ToastUtil.show("账号或密码长度不能小于 6 位");
            return;
        }
        if (!registerPassword.equals(confirmPassword)) {
            ToastUtil.show("两次输入的密码不一致");
            return;
        }
        repo.accountRegister(account, registerPassword, confirmPassword);
    }

    private void getSmsCode() {
        String mobile = etMobile.getText().toString();
        if (mobile.isEmpty()) {
            ToastUtil.show("请输入手机号");
            return;
        }
        repo.getSms(mobile);
    }

    private void doSmsLogin() {
        String mobile = etMobile.getText().toString();
        String smsCode = etSmsCode.getText().toString();
        if (mobile.isEmpty()) {
            ToastUtil.show("请输入手机号");
            return;
        }
        if (smsCode.isEmpty()) {
            ToastUtil.show("请输入验证码");
            return;
        }
        repo.mobileRegister(mobile, smsCode);
    }

    private void showAccountLoginLayout() {
        accountLoginLayout.setVisibility(View.VISIBLE);
        accountRegisterLayout.setVisibility(View.GONE);
        smsLoginLayout.setVisibility(View.GONE);
    }

    private void showSmsLoginLayout() {
        accountLoginLayout.setVisibility(View.GONE);
        accountRegisterLayout.setVisibility(View.GONE);
        smsLoginLayout.setVisibility(View.VISIBLE);
    }

    private void showAccountRegisterLayout() {
        accountLoginLayout.setVisibility(View.GONE);
        accountRegisterLayout.setVisibility(View.VISIBLE);
        smsLoginLayout.setVisibility(View.GONE);
    }

    @Override
    protected String getLayoutName() {
        return "fod_dialog_login";
    }

    private void initComponents(View rootView) {
        radioGroup = rootView.findViewById(ResourceUtil.getViewId("radio_group"));
        rbAccount = rootView.findViewById(ResourceUtil.getViewId("rb_account"));
        rbSms = rootView.findViewById(ResourceUtil.getViewId("rb_sms"));

        accountLoginLayout = rootView.findViewById(ResourceUtil.getViewId("layout_account_login"));
        etLoginAccount = rootView.findViewById(ResourceUtil.getViewId("et_login_account"));
        etLoginPassword = rootView.findViewById(ResourceUtil.getViewId("et_login_password"));
        btnAccountLogin = rootView.findViewById(ResourceUtil.getViewId("btn_account_login"));
        tvAccountRegister = rootView.findViewById(ResourceUtil.getViewId("tv_account_register"));

        accountRegisterLayout = rootView.findViewById(ResourceUtil.getViewId("layout_account_register"));
        etRegisterAccount = rootView.findViewById(ResourceUtil.getViewId("et_register_account"));
        etRegisterPassword = rootView.findViewById(ResourceUtil.getViewId("et_register_password"));
        etConfirmPassword = rootView.findViewById(ResourceUtil.getViewId("et_confirm_password"));
        btnAccountRegister = rootView.findViewById(ResourceUtil.getViewId("btn_account_register"));
        tvAccountLogin = rootView.findViewById(ResourceUtil.getViewId("tv_account_login"));

        smsLoginLayout = rootView.findViewById(ResourceUtil.getViewId("layout_sms_login"));
        etMobile = rootView.findViewById(ResourceUtil.getViewId("et_mobile"));
        etSmsCode = rootView.findViewById(ResourceUtil.getViewId("et_sms_code"));
        btnGetSms = rootView.findViewById(ResourceUtil.getViewId("btn_get_sms"));
        btnSmsLogin = rootView.findViewById(ResourceUtil.getViewId("btn_sms_login"));
    }
}
