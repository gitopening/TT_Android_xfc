package com.mogujie.tt.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.protobuf.CodedInputStream;
import com.mogujie.tt.DB.sp.ConfigurationSp;
import com.mogujie.tt.R;
import com.mogujie.tt.config.SysConstant;
import com.mogujie.tt.imservice.callback.Packetlistener;
import com.mogujie.tt.imservice.event.LoginEvent;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.protobuf.IMBaseDefine;
import com.mogujie.tt.protobuf.IMLogin;
import com.mogujie.tt.ui.base.TTBaseFragment;
import com.mogujie.tt.ui.helper.CheckboxConfigHelper;

import java.io.IOException;

/**
 * 设置页面
 */
public class ChangePwdFragment extends TTBaseFragment{
	private View curView = null;
	private EditText password;
	private EditText passwordNew;
	private View btnChange;
	CheckboxConfigHelper checkBoxConfiger = new CheckboxConfigHelper();



    private IMServiceConnector imServiceConnector = new IMServiceConnector(){
        @Override
        public void onIMServiceConnected() {
            logger.d("config#onIMServiceConnected");
            IMService imService = imServiceConnector.getIMService();
            if (imService != null) {
                checkBoxConfiger.init(imService.getConfigSp());
                initOptions();
            }
        }

        @Override
        public void onServiceDisconnected() {
        }
    };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imServiceConnector.connect(this.getActivity());
		if (null != curView) {
			((ViewGroup) curView.getParent()).removeView(curView);
			return curView;
		}
		curView = inflater.inflate(R.layout.tt_fragment_changpwd, topContentView);

		initRes();
		return curView;
	}

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        imServiceConnector.disconnect(getActivity());
    }

    private void initOptions() {
		password = (EditText) curView.findViewById(R.id.edit_password);
		passwordNew = (EditText) curView.findViewById(R.id.edit_newpassword);

//		saveTrafficModeCheckBox = (CheckBox) curView.findViewById(R.id.saveTrafficCheckBox);
		
//		checkBoxConfiger.initCheckBox(saveTrafficModeCheckBox, ConfigDefs.SETTING_GLOBAL, ConfigDefs.KEY_SAVE_TRAFFIC_MODE, ConfigDefs.DEF_VALUE_SAVE_TRAFFIC_MODE);
	}

	@Override
	public void onResume() {

		super.onResume();
	}

	/**
	 * @Description 初始化资源
	 */
	private void initRes() {
		// 设置标题栏
		setTopTitle(getActivity().getString(R.string.changepwd_page_name));
		setTopLeftButton(R.drawable.tt_top_back);

		btnChange = curView.findViewById(R.id.btn_changepwd);


		topLeftContainerLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
		});
		setTopLeftText(getResources().getString(R.string.top_left_back));

		btnChange.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				intputManager.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
				HideKeyboard(btnChange);
//				logger.d("--d-changepwdfragment");
				logger.i("-i--changepwdfragment");
				gotoReg();
			}
		});
	}

	@Override
	protected void initHandler() {

	}


	public static void HideKeyboard(View v)
	{
		InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
		if ( imm.isActive( ) ) {
			imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

		}
	}


	protected void gotoReg() {

		String desPassword = password.getText().toString();
		String desPasswordNew = password.getText().toString();

		desPassword = new String(com.mogujie.tt.Security.getInstance().EncryptPass(desPassword));
		desPasswordNew = new String(com.mogujie.tt.Security.getInstance().EncryptPass(desPasswordNew));


//
//		IMLogin.IMModifyPasswordReq imLoginReq = IMLogin.IMLoginReq.newBuilder()
//				.setUserName(desPassword)
//				.setPassword(desPwd)
//				.setOnlineStatus(IMBaseDefine.UserStatType.USER_STATUS_ONLINE)
//				.setClientType(IMBaseDefine.ClientType.CLIENT_TYPE_ANDROID)
//				.setClientVersion("1.0.0").build();
//
//		int sid = IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
//		int cid = IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE;
//		imSocketManager.sendRequest(imLoginReq,sid,cid,new Packetlistener() {
//			@Override
//			public void onSuccess(Object response) {
//				try {
//					IMLogin.IMLoginRes  imLoginRes = IMLogin.IMLoginRes.parseFrom((CodedInputStream)response);
//					onRepMsgServerLogin(imLoginRes);
//				} catch (IOException e) {
//					triggerEvent(LoginEvent.LOGIN_INNER_FAILED);
//					logger.e("login failed,cause by %s",e.getCause());
//				}
//			}
//
//			@Override
//			public void onFaild() {
//				triggerEvent(LoginEvent.LOGIN_INNER_FAILED);
//			}
//
//			@Override
//			public void onTimeout() {
//				triggerEvent(LoginEvent.LOGIN_INNER_FAILED);
//			}
//		});
	}
}
