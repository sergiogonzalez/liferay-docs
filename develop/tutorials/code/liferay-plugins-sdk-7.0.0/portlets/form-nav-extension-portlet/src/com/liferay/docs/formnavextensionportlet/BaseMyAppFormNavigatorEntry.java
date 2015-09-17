package com.liferay.docs.formnavextensionportlet;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.servlet.taglib.ui.BaseJSPFormNavigatorEntry;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

public abstract class BaseMyAppFormNavigatorEntry
	extends BaseJSPFormNavigatorEntry<Object>
	implements FormNavigatorEntry<Object> {

	@Override
	public String getKey() {
		return "my-app";
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
				"content.Language", locale, getClass());

			return resourceBundle.getString(getKey());
	}

}