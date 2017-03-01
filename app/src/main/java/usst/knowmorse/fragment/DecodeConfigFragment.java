package usst.knowmorse.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import usst.knowmorse.R;
import usst.knowmorse.activity.MainActivity;
import usst.knowmorse.morsecode.C;

/**
 * 功能：配置阈值并保存
 * @author cunbao
 * @version  v1.0
 * @time    20160510
 * @修改时间  20160619
 * @修改人   cunbao
 * @修改内容  增加了特征参数设置部分
 */
public class DecodeConfigFragment extends Fragment {

    public static final String MY_PREFS = "SharedPreferences";
    public static final String save_PREFS = "SharedPreferences";
    public static final String save_PREFS1 = "otherParam";
    private Button SaveButton;
    private Button SaveButton1;//保存特征参数
    private EditText threshold_brightness;
    private EditText threshold_dot;
    private EditText threshold_letters;
    private EditText threshold_words;
    private TextView get_brightness;
    private EditText set_speed;//单位时间长度
    CheckBox autoSetThreshold; //自适应阈值
    private boolean killme = false;
    Handler mHandler = new Handler();

    private Runnable update = new Runnable() {
        @Override
        public void run() {

            updateBrightnessValue();
            if (!killme)
                mHandler.postDelayed(update, 20);

        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decode_config, container, false);

        get_brightness = (TextView) view.findViewById(R.id.get_brightness);
        threshold_brightness = (EditText) view.findViewById(R.id.con_brightness);
        threshold_dot = (EditText) view.findViewById(R.id.con_dot);
        threshold_letters = (EditText) view.findViewById(R.id.con_leters);
        threshold_words = (EditText) view.findViewById(R.id.conf_words);
        set_speed = (EditText) view.findViewById(R.id.conf_Speed);
        autoSetThreshold = (CheckBox) view.findViewById(R.id.auto_th);

        SharedPreferences prefs = getActivity().getSharedPreferences(save_PREFS, 0);
        threshold_brightness.setText(prefs.getString("brg", ""));
        threshold_dot.setText(prefs.getString("dot", "350"));
        threshold_letters.setText(prefs.getString("letters", "350"));
        threshold_words.setText(prefs.getString("words", "850"));
       // get_brightness.setText("采集区亮度为" + prefs.getString("get_brg", ""));
        SharedPreferences prefs1 = getActivity().getSharedPreferences(save_PREFS1, 0);
        set_speed.setText(prefs1.getString("speed", "250"));
        autoSetThreshold.setChecked(prefs1.getBoolean("autoTh_checked", false));
        SaveButton = (Button) view.findViewById(R.id.btn_ThresholdSet);
        SaveButton1 = (Button) view.findViewById(R.id.send_ThresholdSet);
        SaveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save(v);
                //Toast.makeText(getActivity(), "已保存", Toast.LENGTH_LONG);

            }
        });

        SaveButton1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveOtherPara(v);
                //Toast.makeText(getActivity(), "已保存", Toast.LENGTH_LONG);

            }
        });
        //Toast.makeText(getActivity(), "已保存", Toast.LENGTH_LONG);

        return view;
    }


    private void Save(View v) {
        //Get the username and password
        String brightness = threshold_brightness.getText().toString();
        String dot = threshold_dot.getText().toString();
        String letters = threshold_letters.getText().toString();
        String words = threshold_words.getText().toString();


        SharedPreferences prefs = getActivity().getSharedPreferences(save_PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("brg", brightness);
        editor.putString("letters", letters);
        editor.putString("words", words);
        editor.putString("dot", dot);

        editor.commit();

        //Toast.makeText(getActivity(), "已保存", Toast.LENGTH_LONG);
        Log.d("ddff", "saved");
        Toast.makeText(getActivity(), "已保存", Toast.LENGTH_SHORT).show();

    }

    private void SaveOtherPara(View v) {
        //Get the username and password

        String speed = set_speed.getText().toString();

        SharedPreferences prefs1 = getActivity().getSharedPreferences(save_PREFS1, 0);
        SharedPreferences.Editor editor = prefs1.edit();
        editor.putString("speed", speed);

        if (autoSetThreshold.isChecked()) {
            editor.putBoolean("autoTh_checked", true);
        }
        else {
            editor.putBoolean("autoTh_checked", false);
        }
        editor.commit();

        //Toast.makeText(getActivity(), "已保存", Toast.LENGTH_LONG);
        Log.d("ddff", "saved");
        Toast.makeText(getActivity(), "已保存特征参数", Toast.LENGTH_SHORT).show();


    }
    private void updateBrightnessValue() {
        if (!killme) {
            SharedPreferences prefs = getActivity().getSharedPreferences(save_PREFS, 0);
            if (prefs.getString("get_brg", "") != "")
                get_brightness.setText("采集区亮度为" + prefs.getString("get_brg", ""));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        killme = false;
        mHandler.postDelayed(update, 20);
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