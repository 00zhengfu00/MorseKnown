package usst.knowmorse.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import usst.knowmorse.R;

/**
 * 功能：关于我们页面
 * @author cunbao
 * @version  v1.0
 * @time    20160520
 * @修改时间
 * @修改人
 */
public class AbouFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }
}
