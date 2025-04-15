package com.fodsdk.ui;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fodsdk.core.FodCallback;
import com.fodsdk.net.FodRepository;
import com.fodsdk.net.api.ApiUserAgreement;
import com.fodsdk.net.response.LoginResponse;
import com.fodsdk.utils.ResourceUtil;
import com.fodsdk.utils.ToastUtil;

public class FodLoginDialog extends FodBaseDialog {

    private boolean isRegister = false;     // 登录还是注册
    private RadioGroup radioGroup;
    private RadioButton rbAccount, rbSms;
    private View accountLoginLayout, accountRegisterLayout, smsLoginLayout;
    private EditText etLoginAccount, etLoginPassword, etRegisterAccount, etRegisterPassword, etConfirmPassword, etMobile, etSmsCode;
    private Button btnAccountLogin, btnAccountRegister, btnGetSms, btnSmsLogin;
    private CheckBox cbMobileAgreement, cbRegisterAgreement, cbLoginAgreement;
    private TextView tvAccountRegister, tvAccountLogin;
    private final FodRepository repo;
    private FodCallback<LoginResponse> loginCallback;
    private CountDownTimer timer;

    public FodLoginDialog(Context context, FodRepository repo) {
        super(context);
        this.repo = repo;
    }

    @Override
    protected void initViews(View rootView) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initComponents(rootView);
        initCheckbox();

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
                clearFocus();
            }
        }));
        radioGroup.check(rbAccount.getId());

        btnAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                doAccountLogin();
            }
        });
        tvAccountRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                isRegister = true;
                rbAccount.setText(ResourceUtil.getStringId("fod_account_register"));
                showAccountRegisterLayout();
            }
        });

        btnAccountRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                doAccountRegister();
            }
        });
        tvAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                isRegister = false;
                rbAccount.setText(ResourceUtil.getStringId("fod_account_login"));
                showAccountLoginLayout();
            }
        });

        btnGetSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                getSmsCode();
            }
        });
        btnSmsLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                doSmsLogin();
            }
        });
    }

    private void doAccountLogin() {
        if (!cbLoginAgreement.isChecked()) {
            ToastUtil.show("请先同意用户协议");
            return;
        }
        String account = etLoginAccount.getText().toString();
        String password = etLoginPassword.getText().toString();
        if (account.length() < 6 || password.length() < 6) {
            ToastUtil.show("账号或密码长度不能小于 6 位");
            return;
        }
        repo.accountLogin(account, password, loginCallback);
    }

    private void doAccountRegister() {
        if (!cbRegisterAgreement.isChecked()) {
            ToastUtil.show("请先同意用户协议");
            return;
        }
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
        repo.accountRegister(account, registerPassword, confirmPassword, loginCallback);
    }

    private void getSmsCode() {
        String mobile = etMobile.getText().toString();
        if (mobile.isEmpty()) {
            ToastUtil.show("请输入手机号");
            return;
        }
        repo.getSms(mobile, new FodCallback<Void>() {
            @Override
            public void onValue(Void unused) {
                timer = new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        btnGetSms.setEnabled(false);
                        btnGetSms.setAlpha(0.5f);
                        btnGetSms.setText(getContext().getString(ResourceUtil.getStringId("fod_sms_count_down"), millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {
                        btnGetSms.setEnabled(true);
                        btnGetSms.setAlpha(1);
                        btnGetSms.setText(ResourceUtil.getStringId("fod_get_sms_code"));
                    }
                }.start();
            }
        });
    }

    private void doSmsLogin() {
        if (!cbMobileAgreement.isChecked()) {
            ToastUtil.show("请先同意用户协议");
            return;
        }
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
        repo.mobileLogin(mobile, smsCode);
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

    private void clearFocus() {
        hideSoftKeyBoard(radioGroup);
        etLoginAccount.clearFocus();
        etLoginPassword.clearFocus();
        etRegisterAccount.clearFocus();
        etRegisterPassword.clearFocus();
        etConfirmPassword.clearFocus();
        etMobile.clearFocus();
        etSmsCode.clearFocus();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (timer != null) {
            timer.cancel();
        }
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
        cbLoginAgreement = rootView.findViewById(ResourceUtil.getViewId("cb_login_agreement"));
        tvAccountRegister = rootView.findViewById(ResourceUtil.getViewId("tv_account_register"));

        accountRegisterLayout = rootView.findViewById(ResourceUtil.getViewId("layout_account_register"));
        etRegisterAccount = rootView.findViewById(ResourceUtil.getViewId("et_register_account"));
        etRegisterPassword = rootView.findViewById(ResourceUtil.getViewId("et_register_password"));
        etConfirmPassword = rootView.findViewById(ResourceUtil.getViewId("et_confirm_password"));
        btnAccountRegister = rootView.findViewById(ResourceUtil.getViewId("btn_account_register"));
        cbRegisterAgreement = rootView.findViewById(ResourceUtil.getViewId("cb_register_agreement"));
        tvAccountLogin = rootView.findViewById(ResourceUtil.getViewId("tv_account_login"));

        smsLoginLayout = rootView.findViewById(ResourceUtil.getViewId("layout_sms_login"));
        etMobile = rootView.findViewById(ResourceUtil.getViewId("et_mobile"));
        etSmsCode = rootView.findViewById(ResourceUtil.getViewId("et_sms_code"));
        btnGetSms = rootView.findViewById(ResourceUtil.getViewId("btn_get_sms"));
        btnSmsLogin = rootView.findViewById(ResourceUtil.getViewId("btn_sms_login"));
        cbMobileAgreement = rootView.findViewById(ResourceUtil.getViewId("cb_mobile_agreement"));
    }

    public void setOnLoginCallback(FodCallback<LoginResponse> callback) {
        loginCallback = callback;
    }

    private void initCheckbox() {
        String userAgreement = new ApiUserAgreement(repo.getGameConfig()).getUrl();
        String privacyPolicy = new ApiUserAgreement(repo.getGameConfig()).getUrl();
        Spanned cbText = Html.fromHtml("我已同意《<a href='" + userAgreement + "'>用户协议</a>》和《<a href='" + privacyPolicy + "'>隐私政策</a>》", Html.FROM_HTML_MODE_LEGACY);
        cbLoginAgreement.setText(cbText);
        handleCheckBoxClick(cbLoginAgreement);
        cbMobileAgreement.setText(cbText);
        handleCheckBoxClick(cbMobileAgreement);
        cbRegisterAgreement.setText(cbText);
        handleCheckBoxClick(cbRegisterAgreement);
    }

    private void handleCheckBoxClick(CheckBox checkBox) {
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());
        Spanned text = (Spanned) checkBox.getText();
        URLSpan[] urls = text.getSpans(0, text.length(), URLSpan.class);
        SpannableStringBuilder sb = new SpannableStringBuilder(text);
        sb.clearSpans();
        for (URLSpan span : urls) {
            sb.setSpan(
                    new HyperlinkSpan(span.getURL()),
                    text.getSpanStart(span),
                    text.getSpanEnd(span),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        checkBox.setText(sb);
    }


    private static class HyperlinkSpan extends ClickableSpan {

        private final String url;

        public HyperlinkSpan(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            FodWebDialog dialog = new FodWebDialog(widget.getContext(), url);
            dialog.show();
        }
    }
}
