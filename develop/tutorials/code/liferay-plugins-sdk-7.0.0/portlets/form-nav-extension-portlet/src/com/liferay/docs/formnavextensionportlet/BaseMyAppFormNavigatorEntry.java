package com.liferay.docs.formnavextensionportlet;

import java.util.Locale;

import com.liferay.portal.kernel.servlet.taglib.ui.BaseJSPFormNavigatorEntry;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;

public abstract class BaseMyAppFormNavigatorEntry
	extends BaseJSPFormNavigatorEntry<Object>
	implements FormNavigatorEntry<Object> {

	@Override
	public String getKey() {
		return "myApp";
	}

	@Override
	public String getLabel(Locale locale) {
		return "My App";
	}

}