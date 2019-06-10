package com.gymkhana.studio.updater.tools;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class Loader {
	public Activity context;
	public Class cls;
	private NotificationUtils notify;
	public String old;
	private Intent intent = new Intent();
	public static String _urlFile;
	public Loader(Activity context) {
		this.context = context;
		Prefer.init(this.context);
		notify = new NotificationUtils(this.context);
		try {
			PackageManager manager = this.context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.context.getPackageName(), PackageManager.GET_ACTIVITIES);
			old = info.versionName;
		} catch (android.content.pm.PackageManager.NameNotFoundException e){
			e.printStackTrace();
		}
	}
	public void setClass(Class cls) {
		this.cls = cls;
	}
	public void setOldVersion(String old) {
		this.old = old;
	}
	public static void setUrl(String urls) {
		_urlFile = urls;
	}
	public void getJSON() {
		new JConnections(this.context).execute();
	}
	public class JConnections extends AsyncTask<Void, Void, Void> {
		private Activity context;
		public JConnections(Activity context) {
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			HttpHandler sh = new HttpHandler();
			String jsonStr = sh.makeServiceCall(_urlFile);
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					JSONObject obj = jsonObj.getJSONObject("Version");
					String pkg_v = obj.getString("Pkg");
					String new_v = obj.getString("New");
					String info_v = obj.getString("Info");
					String link_v = obj.getString("Link");
					Prefer.set("Pkg", pkg_v);
					Prefer.set("New", new_v);
					Prefer.set("Info", info_v);
					Prefer.set("Link", link_v);
				} catch (final JSONException e) {
					Toast.makeText(this.context, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
					this.context.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(context, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			} else {
				Toast.makeText(this.context, "get Json error: ", Toast.LENGTH_LONG).show();
				this.context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(context, "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
					}
				});
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (Prefer.get("New", "").equals(old)) {} else {
				intent.setClass(this.context, cls);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				notify.setMsg(2, 102, "New Update Availibe", "New Version " + Prefer.get("New", "") + " Are availible!.\nYour old version are: " + old, android.R.drawable.stat_sys_download_done, intent);
			}
		}
	}
}