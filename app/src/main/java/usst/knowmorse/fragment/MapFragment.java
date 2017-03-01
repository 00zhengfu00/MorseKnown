package usst.knowmorse.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import usst.knowmorse.R;

/**
 * 功能：莫尔斯码对照表页面
 * @author cunbao
 * @version  v1.0
 * @time    20160519
 * @修改时间
 * @修改人
 */
public class MapFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		return view;
	}
}
