package com.gymkhana.studio.updater;

import android.app.Activity;
import com.gymkhana.studio.updater.tools.Loader;
import com.gymkhana.studio.updater.tools.NotificationUtils;

public class Updater {
	private Activity context;
	private Loader loader;
	public Updater(Activity context) {
		this.context = context;
		loader = new Loader(this.context);
	}
	public void setUrl(String url) {
		loader.setUrl(url);
	}
	public void setClass(Class cls) {
		loader.setClass(cls);
	}
	public void setOldVersion(String old) {
		loader.setOldVersion(old);
	}
	public void start() {
		loader.getJSON();
	}
}