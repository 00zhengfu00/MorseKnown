package usst.knowmorse.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Rect;
//import android.hardware.Camera;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;

import usst.knowmorse.R;
import usst.knowmorse.activity.MainActivity;
import usst.knowmorse.morsecode.C;
import usst.knowmorse.morsecode.Decoder;
import usst.knowmorse.morsecode.FocusArea;


/**
 * 功能：实现摄像头对莫尔斯码灯的识别
 * @author cunbao
 * @version  v1.0
 * @time    20160510
 * @修改时间
 * @修改人
 */
public class DecodeFragment extends Fragment {

    Decoder decoder;

    TextView morseInput;
    TextView textOutput;
    TextView  OnOffTime;//亮灭时间
   // Button dotButton;
   // Button dashButton;
   // Button deleteButton;
   // Button nextWordButton;
   // Button nextCharacterButton;
    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceView graph = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;

    private static PowerManager.WakeLock wakeLock = null;

    private static long startTime = 0;
    private boolean killme = false;
    Handler mHandler = new Handler();
    private Timer timer = new Timer();
    static int imgAvg;//摄像头实时获得的亮度信息
    static int brignessAvg=0;//平均亮度信息
    private static final int averageArraySize = 4;
    private static int beatsIndex = 0;
    private static final int beatsArraySize = 1;
    private static final int[] beatsArray = new int[beatsArraySize];
    private int camId = 1;
    private boolean IsFistGet=true;//获取亮度的阈值
    private boolean checked=true;//检测灯之前有没有亮过
    public static final String save_PREFS = "SharedPreferences";
    private  int Threshold_brightness;//亮度阈值  单位 lux
    private int threshold_dot=350;//点的阈值  单位 ms
    private int threshold_char=350;//字母阈值
    private int threshold_words=850;//单词阈值
    private Runnable update = new Runnable() {
        @Override
        public void run() {
//
          //  updatedata();
            if (!killme)
                mHandler.postDelayed(update, 20);
//
//


        }
//
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decode, container, false);
        decoder = new Decoder();

        morseInput = (TextView) view.findViewById(R.id.morse_input);
        textOutput = (TextView) view.findViewById(R.id.text_output);
        OnOffTime  =(TextView)  view.findViewById(R.id.OnOffState_out);
        OnOffTime.setMaxHeight(30);
        morseInput.setMaxHeight(50);
        OnOffTime.setMovementMethod(ScrollingMovementMethod.getInstance());
        morseInput.setMovementMethod(ScrollingMovementMethod.getInstance());
        //​​OnOffTime.setMovementMethod(ScrollingMovementMethod.getInstance());
        //​​OnOffTime.setMovementMethod(ScrollingMovementMethod.getInstance());
        //dotButton = (Button)view.findViewById(R.id.dotButton);

       /* dotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dot(v);
            }
        });*/

        //dashButton = (Button) view.findViewById(R.id.dashButton);

       /* dashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dash(v);
            }
        });*/

      //  deleteButton = (Button) view.findViewById(R.id.delete);

//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delete(v);
//            }
//        });

       // nextCharacterButton = (Button) view.findViewById(R.id.nextCharacterButton);

       /* nextCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextCharacterButton(v);
            }
        });

        nextWordButton = (Button) view.findViewById(R.id.nextWordButton);

        nextWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWord(v);
            }
        });



*/
        SharedPreferences prefs = getActivity().getSharedPreferences(save_PREFS, 0);
        if(prefs.getString("dot","")!=""&&prefs.getString("words","")!=""
                &&prefs.getString("letters","")!=""&&
        prefs.getString("brg","")!="") {
            Threshold_brightness = Integer.parseInt(prefs.getString("brg", ""));
            threshold_char = Integer.parseInt(prefs.getString("letters", ""));
            threshold_words = Integer.parseInt(prefs.getString("words", ""));
            threshold_dot = Integer.parseInt(prefs.getString("dot", ""));
            IsFistGet=false;
        }
        else
        {   IsFistGet=true;
            Toast.makeText(getActivity(),"请设置阈值否则执行默认值",Toast.LENGTH_LONG).show();
        }
        preview = (SurfaceView) view.findViewById(R.id.preview);
        // graph = (SurfaceView) findViewById(R.id.Beats_Graph);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        wakeLock.acquire();
        try {

            if (camId == 0) {
                //camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            else {
               camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }
        //camera = Camera.open();

        //startTime = System.currentTimeMillis();

        killme = false;
        mHandler.postDelayed(update, 20);
    }
    /**
     * @function  onPause()
     * 重写父类 onPause()方法
     */
    @Override
    public void onPause() {
        super.onPause();

         wakeLock.release();

        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();

        mHandler.removeCallbacks(null);
        killme = true;
    }
    @Override
    public void onDestroy() {
        //当结束程序时关掉Timer
        timer.cancel();
        super.onDestroy();

    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        /**
         * @method onPreviewFrame(byte[] data, Camera cam)
         * @function 预览摄像头 获取亮度信息
         * 实现莫尔斯码灯的识别
         *重写 父类camera的onPreviewFrame（）方法
         * @para
         * byte[]data  代表了一个yuv420sp图像数据矩阵
         *
         */
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;
            int w = width / 2;
            int h = height / 2;
            float f = 20.0F * 2.5F / 2.0F;
            int k = (int) (w - f);
            int m = (int) (f + w);
            int h1 = (int) (h - f);
            int h2 = (int) (f + h);
            int width1 = m - k;
            int height1 = h2 - h1;
            // Rect localRect = new Rect(k, j - 10, m, j + 10);
            Log.d(TAG, "width1=" + width1 + " height=" + height1);
            imgAvg = Decoder.decodeYUV420SPtoBrightnessAvg(data.clone(), k, h1, m, h2, width1, height1);
            Log.d("brightness", "img="+imgAvg);
            if (IsFistGet) {

//               beatsArray[beatsIndex] = imgAvg;
//               beatsIndex++;
//
//               if (beatsIndex == beatsArraySize) {
//                   int brightnessSum = 0;
//                   for (int i = 0; i < beatsArraySize; i++)
//                   {
//                       if (beatsArray[i] > 0) {
//                           brightnessSum += beatsArray[i];
//
//                                               }
//                   }
//                       beatsIndex = 0;
//                       brignessAvg = brightnessSum /beatsArraySize;
//                       IsFistGet = false;
//                   }
                Threshold_brightness=GetBritnessOnce(data.clone(), k, h1,m,h2,width1,height1);
                IsFistGet = false;
            }

            if (imgAvg == 0) {
                processing.set(false);
                return;
            }

       //      Toast.makeText(getActivity(),"dddddddddd" , Toast.LENGTH_SHORT).show();

            /**
             * 因为第一次开始亮灯到下一次开始亮灯的时间差就是灭灯时间
             * 同理 从第一次灭灯开始到下一次灭灯开始的时间差就是亮灯时间
             * 因此亮灯状态实际是检测灭灯时间
             * 灭灯状态实际是监测亮灯时间
             */
            if (!IsFistGet) {
                send_GetBritnessOnce(data.clone(), k, h1, m, h2, width1, height1);

                // Toast.makeText(getActivity(),"brightness"+String.valueOf(brignessAvg) , Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(),"img"+String.valueOf(imgAvg) , Toast.LENGTH_SHORT).show();
                //led on
               // Toast.makeText(getActivity(),"brightness"+String.valueOf(imgAvg) , Toast.LENGTH_SHORT).show();
                if (imgAvg > Threshold_brightness) {
                    if (checked) {
                        if (!processing.compareAndSet(false, true)) {
                            long endTime1 = System.currentTimeMillis();
                           double totalTimeInMillion = (endTime1 - startTime);
                            Log.d("offTime", "time="+totalTimeInMillion);
                            //打印亮的时间
                            String currentOnTime = OnOffTime.getText().toString();
                            String updatedOnTime = currentOnTime.concat("\n"+"offtime="+totalTimeInMillion+"ms");
                            OnOffTime.setText(updatedOnTime);
                            refreshOnOffTimeTextView();
                            if (totalTimeInMillion > threshold_char && totalTimeInMillion < threshold_words) {
                                String current = morseInput.getText().toString();
                                String updated = current.concat(" ");
                                refreshMorseInputTextView();//防止超出边界  使其滚动到第一行
                                morseInput.setText(updated);
                                updateOutput(updated);


                            } else if (totalTimeInMillion > threshold_words) {
                                String current = morseInput.getText().toString();
                                String updated = current.concat(C.WORD_SEPERATOR);
                                refreshMorseInputTextView();
                                morseInput.setText(updated);
                                updateOutput(updated);

                            }
                            startTime = System.currentTimeMillis();
                            processing.set(true);

                        }
                        checked = false;
                    }
                }
                    //led off
                    else {
                        if (!checked) {
                            if (!processing.compareAndSet(false, true)) {
                                long endTime2 = System.currentTimeMillis();
                                double totalTimeInMillion = (endTime2 - startTime);
                                Log.d("onTime", "time="+totalTimeInMillion);
                                //打印灭的时间
                                String currentOffTime = OnOffTime.getText().toString();
                                String updatedOffTime = currentOffTime.concat("\n"+"ontime="+totalTimeInMillion+"ms");
                                OnOffTime.setText(updatedOffTime);
                                refreshOnOffTimeTextView();
                                if (totalTimeInMillion < threshold_dot) {
                                    String current = morseInput.getText().toString();
                                    String updated = current.concat(".");
                                    refreshMorseInputTextView();
                                    morseInput.setText(updated);
                                    updateOutput(updated);


                                } else {
                                    String current = morseInput.getText().toString();
                                    String updated = current.concat("-");
                                    refreshMorseInputTextView();
                                    morseInput.setText(updated);
                                    updateOutput(updated);


                                }
                                startTime = System.currentTimeMillis();
                                processing.set(true);
                            }
                            checked = true;
                        }
                    }

            }
            processing.set(false);

        }
    };
    /**
     * Created by cunbao on 2016/05/20.
     * 获取采集区域内的亮度值
     * @author cunbao
     *@param
     * w1:取一正方形识别区域   其左上点(下)的横坐标
     * w2:其右上点（下点）的横坐标
     * h1:其左（右）上点的纵坐标
     * h2:其左（右）下点的纵坐标
     *   width=w2-w1
     *   height=h2-h1
     *   revised by cunbao 20160523
     */
    private void send_GetBritnessOnce(byte[] data,int w1,int h1,int w2,int h2,int width,int height )
    {

       int Nowget_brignessAvg = Decoder.decodeYUV420SPtoBrightnessAvg(data.clone(),w1,h1,w2,h2, 50, 50);

        SharedPreferences prefs = getActivity().getSharedPreferences(save_PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("get_brg", String.valueOf(Nowget_brignessAvg));
        editor.commit();

    }
/*************************************************************************************/



    /**
     * Created by cunbao on 2016/05/20.
     * 获取采集区域内的亮度值
     * @author cunbao
     *@param
     * w1:取一正方形识别区域   其左上点(下)的横坐标
     * w2:其右上点（下点）的横坐标
     * h1:其左（右）上点的纵坐标
     * h2:其左（右）下点的纵坐标
     *   width=w2-w1
     *   height=h2-h1
     *   revised by cunbao 20160523
     */
    private int  GetBritnessOnce(byte[] data,int w1,int h1,int w2,int h2,int width,int height )
    {

        brignessAvg = Decoder.decodeYUV420SPtoBrightnessAvg(data.clone(),w1,h1,w2,h2, 50, 50);

        SharedPreferences prefs = getActivity().getSharedPreferences(save_PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("get_brg", String.valueOf(brignessAvg));
        editor.commit();
   return brignessAvg;
    }
    /*************************************************************************************/





    private  SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * @method  surfaceCreated(SurfaceHolder holder)
         * @function  重写父类surfaceCreated 方法
         * 实现预览和回调接口
         *
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);

            } catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@实现接口}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
            }
            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);
            camera.startPreview();


        }

        /**
         * {@实现接口}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
               //camera.setPreviewCallback(null);
           // camera.stopPreview();
            //camera.release();
           //camera = null;
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }



        }

        return result;
    }

    /**
     * @method  updateOutput(String text)
     * @function  更新输出的莫尔斯码
     *
     */
    public void updateOutput(String text){
        //Update output every character or every deleted character

        textOutput.setText(decoder.decode(text));
    }
    //把超出边界的OnOffTime打印项重新定位到第一行，实现滚动刷新显示
    /**
     * @method  refreshOnOffTimeTextView()
     * @function
     * 定位到打印OnOffTime的TextView
     *把超出边界的OnOffTime打印项重新定位到第一行，实现滚动刷新显示
     @author cunabo
     @revised
     @time 20160522
     */
    void refreshOnOffTimeTextView(){

            int offset=OnOffTime.getLineCount()*OnOffTime.getLineHeight();
            if(offset>OnOffTime.getHeight()){
                    OnOffTime.scrollTo(0,offset-OnOffTime.getHeight());
                }
         }
    /*********************************************************/
    /**
     * @method  refreshMorseInputTextView()
     * @function
     * 定位到打印MorseInput的TextView
     *把超出边界的MorseInput打印项重新定位到第一行，实现滚动刷新显示
     @author cunabo
     @revised
     @time 20160522
     */
    void refreshMorseInputTextView(){

        int offset=morseInput.getLineCount()*morseInput.getLineHeight();
        if(offset>morseInput.getHeight()){
            morseInput.scrollTo(0,offset-morseInput.getHeight());
        }
    }
    /*********************************************************/
    public void delete(View view) {
        String current = morseInput.getText().toString();
        String updated = "";
        if(current.length() <= 1) {
            morseInput.setText("");
            return;
        } else {
             updated = morseInput.getText().toString().substring(0, current.length()-1);
        }
        updated = updated.trim();
        morseInput.setText(updated);

        updateOutput(updated);
    }
}
