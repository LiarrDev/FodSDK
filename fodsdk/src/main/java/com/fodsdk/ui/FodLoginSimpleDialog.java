package com.fodsdk.ui;

import android.content.Context;
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

/**
 * TODO: remove
 */
public class FodLoginSimpleDialog extends FodBaseDialog {
    private RadioGroup radioGroup;
    private RadioButton rbLogin, rbRegister;
    private View accountLoginLayout, accountRegisterLayout;
    private EditText etLoginAccount, etLoginPassword, etRegisterAccount, etRegisterPassword, etConfirmPassword;
    private Button btnAccountLogin, btnAccountRegister;
    private CheckBox cbRegisterAgreement, cbLoginAgreement;
    private TextView tvAccountRegister, tvAccountLogin;
    private final FodRepository repo;
    private FodCallback<LoginResponse> loginCallback;

    public FodLoginSimpleDialog(Context context, FodRepository repo) {
        super(context);
        this.repo = repo;
    }

    @Override
    protected void initViews(View rootView) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initComponents(rootView);
        initCheckbox();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbLogin.getId()) {
                    showAccountLoginLayout();
                } else if (checkedId == rbRegister.getId()) {
                    showAccountRegisterLayout();
                }
                clearFocus();
            }
        });
        radioGroup.check(rbLogin.getId());

        btnAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                doAccountLogin();
            }
        });
        btnAccountRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                doAccountRegister();
            }
        });

        tvAccountLogin.setVisibility(View.GONE);
        tvAccountRegister.setVisibility(View.GONE);
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

    private void showAccountLoginLayout() {
        accountLoginLayout.setVisibility(View.VISIBLE);
        accountRegisterLayout.setVisibility(View.GONE);
    }

    private void showAccountRegisterLayout() {
        accountLoginLayout.setVisibility(View.GONE);
        accountRegisterLayout.setVisibility(View.VISIBLE);
    }

    private void clearFocus() {
        hideSoftKeyBoard(radioGroup);
        etLoginAccount.clearFocus();
        etLoginPassword.clearFocus();
        etRegisterAccount.clearFocus();
        etConfirmPassword.clearFocus();
        etRegisterPassword.clearFocus();
    }

    @Override
    protected String getLayoutName() {
        return "fod_dialog_login_simple";
    }

    private void initComponents(View rootView) {
        radioGroup = rootView.findViewById(ResourceUtil.getViewId("radio_group"));
        rbLogin = rootView.findViewById(ResourceUtil.getViewId("rb_login"));
        rbRegister = rootView.findViewById(ResourceUtil.getViewId("rb_register"));

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
    }

    private void initCheckbox() {
        String userAgreement = new ApiUserAgreement(repo.getGameConfig()).getUrl();
        String privacyPolicy = new ApiUserAgreement(repo.getGameConfig()).getUrl();
        Spanned cbText = Html.fromHtml("我已同意《<a href='" + userAgreement + "'>用户协议</a>》和《<a href='" + privacyPolicy + "'>隐私政策</a>》", Html.FROM_HTML_MODE_LEGACY);
        cbLoginAgreement.setText(cbText);
        handleCheckBoxClick(cbLoginAgreement);
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

    public void setOnLoginCallback(FodCallback<LoginResponse> callback) {
        loginCallback = callback;
    }
}
