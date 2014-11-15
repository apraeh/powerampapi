package com.maxmpz.poweramp.skinlib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

public class ResourceWrappingActivity extends Activity {
	private static final String TAG = "ResourceWrappingActivity";
	private static final int PACKAGE_GROUP_ID = 0x02;
	
	static {
		rewriteRValues(ResourceWrappingActivity.class.getClassLoader(), ResourceWrappingActivity.class.getPackage().getName(), PACKAGE_GROUP_ID);
	}

	private boolean mRewritten;
	
	@Override
	public Resources getResources() {
		if(!mRewritten) {
			rewriteRValues(this.getClass().getClassLoader(), getPackageName(), PACKAGE_GROUP_ID);
			mRewritten = true;			
		}
		return super.getResources();
	}
	
    private static void rewriteRValues(ClassLoader cl, String packageName, int id) {
        final Class<?> rClazz;
        try {
            rClazz = cl.loadClass(packageName + ".R");
        } catch (ClassNotFoundException e) {
            return;
        }

        final Method callback;
        try {
            callback = rClazz.getMethod("onResourcesLoaded", int.class);
        } catch (NoSuchMethodException e) {
            return;
        }

        Throwable cause;
        try {
            callback.invoke(null, id);
            return;
        } catch (IllegalAccessException e) {
            cause = e;
        } catch (InvocationTargetException e) {
            cause = e.getCause();
        }

        Log.e(TAG, "Failed to rewrite resource references for " + packageName, cause);
    }
	
}
