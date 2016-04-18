package com.anybeen.mark.imageeditor.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by maidou on 2016/4/18.
 */
public class FragmentFactory {
    public static Fragment create(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                // fragment = new TypeOneFragment();
                break;
        }
        return fragment;
    }

}
