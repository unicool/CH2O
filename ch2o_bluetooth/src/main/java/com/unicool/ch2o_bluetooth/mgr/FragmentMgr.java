package com.unicool.ch2o_bluetooth.mgr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.unicool.ch2o_bluetooth.BuildConfig;

import java.util.List;

public class FragmentMgr {
    private static final String TAG = "FragmentMgr";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private List<Fragment> mFragmentList = null;

    public FragmentMgr(FragmentManager fm, List<Fragment> list) {
        mFragmentManager = fm;
        mFragmentList = list;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    public void showFragment(ViewGroup container, int position) {
        if (position > mFragmentList.size()) {
            if (DEBUG)
                Log.e(TAG, "IndexOutOfBoundsException when ShowFragment item #" + position + ": size=" + mFragmentList.size());
            return;
        }
        if (isShowing(position)) {
            if (DEBUG) Log.d(TAG, "mCurrentPrimaryItem is showing");
            return;
        }
        startUpdate(container);
        instantiateItem(container, position);
        if (mCurrentPrimaryItem != null) {
            destroyItem(container, mFragmentList.indexOf(mCurrentPrimaryItem), mCurrentPrimaryItem);
        }
        setPrimaryItem(container, position, getItem(position));
        finishUpdate(container);
    }

    public boolean isShowing(int position) {
        return mFragmentList.get(position) == mCurrentPrimaryItem;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    private Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    private void startUpdate(ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("FragmentMgr with container " + this + " requires a view id");
        }
    }

    private Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            if (DEBUG) Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            mCurTransaction.attach(fragment);
            mCurTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            mCurTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//            mCurTransaction.setCustomAnimations(android.R.anim.overshoot_interpolator, android.R.anim.slide_out_right);
        } else {
            fragment = getItem(position);
            if (DEBUG) Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
            mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));
            mCurTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            mCurTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//            mCurTransaction.setCustomAnimations(android.R.anim.overshoot_interpolator, android.R.anim.slide_out_right);
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    private void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG)
            Log.v(TAG, "Detaching item #" + getItemId(position) + ": f=" + object + " v=" + ((Fragment) object).getView());
        mCurTransaction.detach((Fragment) object);
    }

    private void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    private void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }
    }

    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    /**
     * Return a unique identifier for the item at the given position.
     * <p>
     * <p>The default implementation returns the given position.
     * Subclasses should override this method if the positions of items can change.</p>
     *
     * @param position Position within this adapter
     * @return Unique identifier for the item at position
     */
    private long getItemId(int position) {
        return position;
    }
}
