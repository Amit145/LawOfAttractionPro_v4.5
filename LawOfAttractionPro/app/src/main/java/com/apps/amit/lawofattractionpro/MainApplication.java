package com.apps.amit.lawofattractionpro;

import android.app.Application;
import android.content.Context;

import com.apps.amit.lawofattractionpro.helper.LocaleHelper;

public class MainApplication extends Application {
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
	}
}
