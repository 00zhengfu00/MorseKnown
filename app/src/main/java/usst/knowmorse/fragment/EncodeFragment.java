package usst.knowmorse.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import usst.knowmorse.R;
import usst.knowmorse.flashlight.FlashExecutor;
import usst.knowmorse.morsecode.C;
import usst.knowmorse.morsecode.Encoder;


/**
 * 功能：闪光灯编码功能界面
 * @author cunbao   <1063994955@qq.com>
 * @version  v2.0
 * @time    20160618
 * @修改时间 20160618
 * @修改人    cunbao
 */
public class EncodeFragment extends android.support.v4.app.Fragment {

    Encoder encoder;
    EditText editText;
    Button encodeMessageButton;
    Button stopExecutionButton;
    CheckBox flashToggle;
    CheckBox repeatFlashToggle;
    FlashExecutor executor;
    TextView textOutput;
    private boolean killme = false;
    private boolean start_torepeatFlash=false;
    Handler mHandler = new Handler();
    private Runnable update = new Runnable() {
        @Override
        public void run() {

            updateRepeatFlashMessage();
            if (!killme)
                mHandler.postDelayed(update, 1000);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encode, container, false);
        encoder = new Encoder();
        editText = (EditText) view.findViewById(R.id.editText);
        textOutput = (TextView) view.findViewById(R.id.text_output);
        encodeMessageButton = (Button) view.findViewById(R.id.playButton);
        stopExecutionButton = (Button) view.findViewById(R.id.stopButton);
        flashToggle = (CheckBox) view.findViewById(R.id.flashMessageButton);
        repeatFlashToggle = (CheckBox) view.findViewById(R.id.flashRepeatButton);
        executor = new FlashExecutor();

        if(!deviceHasFlash(getContext())){
            flashToggle.setVisibility(View.INVISIBLE);
        }

        encodeMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encode();
            }
        });

        stopExecutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopExecution();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        killme = false;
        mHandler.postDelayed(update, 20);
    }

    private void encode() {

        String text = editText.getText().toString().toLowerCase();

        String encoded = encoder.encode(text);

        textOutput.setText(encoded);

        if(deviceShouldFlash()){
           flashMessage();
        }

       if(deviceShouldVibrate()) {
           updateRepeatFlashMessage();
           start_torepeatFlash=true;
        }

    }


    private void flashMessage() {
        SharedPreferences prefs = getActivity().getSharedPreferences("otherParam", 0);
        C.DOT_TIME_INTERVAL=Integer.parseInt(prefs.getString("speed", "250"));
        executor.setStringToEncode(editText.getText().toString().trim());
        executor.start();

    }


    private void stopExecution(){

        executor.stop();
        start_torepeatFlash=false;
    }


    private void vibrateMessage() {
        flashMessage();

    }
    private void updateRepeatFlashMessage(){
        if(deviceShouldVibrate()&&start_torepeatFlash) {
            vibrateMessage();
        }
    }


    private boolean deviceShouldFlash() {
      return !flashToggle.isChecked() && deviceHasFlash(getContext());
    }

    private boolean deviceShouldVibrate() {
        return !repeatFlashToggle.isChecked()&& deviceHasFlash(getContext());
    }


    private boolean deviceHasFlash(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onPause() {
        super.onPause();
        killme = true;

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
