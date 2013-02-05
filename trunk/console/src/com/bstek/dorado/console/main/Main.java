package com.bstek.dorado.console.main;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bstek.dorado.console.Setting;
import com.bstek.dorado.console.addon.Addon;
import com.bstek.dorado.console.addon.AddonService;
import com.bstek.dorado.console.login.PermissionConsoleLogin;
import com.bstek.dorado.view.View;

public class Main {
	private static final SimpleDateFormat dataFormat = new SimpleDateFormat(
			"yyyy-MM-dd kk:mm");
	private AddonService addonService;

	public AddonService getAddonService() {
		return addonService;
	}

	public void setAddonService(AddonService addonService) {
		this.addonService = addonService;
	}

	public void onViewInit(View view) {
		Map<String, Object> map = new HashMap<String, Object>();
		long startTime = Setting.getStartTime();
		String date = dataFormat.format(startTime);
		boolean buttonLogoutVisible = Setting.getConsoleLogin().getClass()
				.getName().equals(PermissionConsoleLogin.class.getName());
		String core_version = "", console_version = "", name;
		try {
			Collection<Addon> addons = addonService.getAddonList();
			Iterator<Addon> iterator = addons.iterator();
			while (iterator.hasNext()) {
				Addon doradoAddon = (Addon) iterator.next();
				name = doradoAddon.getName();
				if (name.equals("dorado-core")) {
					core_version = doradoAddon.getVersion();
				} else if (name.equals("dorado-console")) {
					console_version = doradoAddon.getVersion();
				}
			}

		} catch (Exception e) {
		}

		map.put("startTime", date);
		map.put("core_version", core_version);
		map.put("console_version", console_version);
		map.put("buttonLogoutVisible", !buttonLogoutVisible);

		view.setUserData(map);
	}

}
