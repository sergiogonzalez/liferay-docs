package com.liferay.docs.formnavextensionportlet;

import java.io.IOException;

import javax.portlet.PortletPreferences;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorConstants;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

@Component(immediate = true, service = FormNavigatorEntry.class)
public class MyAppCompanySettingsFormNavigatorEntry
	extends BaseMyAppFormNavigatorEntry {

@Override
public String getCategoryKey() {
	return FormNavigatorConstants.
		CATEGORY_KEY_COMPANY_SETTINGS_MISCELLANEOUS;
}

@Override
public String getFormNavigatorId() {
	return FormNavigatorConstants.FORM_NAVIGATOR_ID_COMPANY_SETTINGS;
}

@Override
public void include(
		HttpServletRequest request, HttpServletResponse response)
	throws IOException {

	ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
		WebKeys.THEME_DISPLAY);

	PortletPreferences companyPortletPreferences =
		PrefsPropsUtil.getPreferences(themeDisplay.getCompanyId(), true);

	boolean companyMentionsEnabled = PrefsParamUtil.getBoolean(
		companyPortletPreferences, request, "mentionsEnabled", true);

	request.setAttribute(
		MentionsWebKeys.COMPANY_MENTIONS_ENABLED, companyMentionsEnabled);

	super.include(request, response);
}

@Override
@Reference(
	target = "(osgi.web.symbolicname=com.liferay.docs.formnavextensionportlet)",
	unbind = "-"
)
public void setServletContext(ServletContext servletContext) {
	super.setServletContext(servletContext);
}

@Override
protected String getJspPath() {
	return "/portal_settings/my_app.jsp";
}

}