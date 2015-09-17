# Adding New Sections and Categories to Existing Form Navigation

Liferay Portal provides the taglib `form-navigator` that is useful to organize a
big form in categories and sections that can be easily browsed and filled by the
user.

Since Liferay 7.0, Liferay offers a mechanism to dynamically add new sections to
form navigator taglibs via OSGi modules. Developers can take advantage of this
mechanism to create their own OSGi plugins and add new categories or sections in
those form navigators.

![Figure 1: The Form Navigation Framework lets you add your app's forms to existing form navigators, like the one used in Portal Settings.](../../images/form-navigator-portal-setting-extension.png)

The `form-navigator` tag has an attribute `id` that indentifies that particular
form-navigator taglib instance. This attributed is used by the sections and
categories to identify in which form navigator they will be included. 

This tutorial demonstrates adding a new navigation section (entry) to an
existing list of navigation sections. Source code from an example portlet called
the Form Nav Extension portlet demonstrates extending form navigation. You can
find the code referenced by this tutorial in a complete project on Github here:
<https://github.com/jhinkey/liferay-docs/tree/pr256-form-navigator/develop/tutorials/code/liferay-plugins-sdk-7.0.0/portlets/form-nav-extension-portlet>.
You can also download the portlet's bundle from the following link (click *View
Raw* to download it):
<https://github.com/jhinkey/liferay-docs/blob/pr256-form-navigator/develop/tutorials/code/liferay-plugins-sdk-7.0.0/osgi/modules/com.liferay.docs.formnavportlet.jar>

To add a new entry to an existing form navigation, follow these steps: 

1.  Create a class that  implements the [`FormNavigatorEntry`](https://github.com/liferay/liferay-portal/blob/master/portal-service/src/com/liferay/portal/kernel/servlet/taglib/ui/FormNavigatorEntry.java)
    interface. If you're creating several navigation entries, you might want to
    create a base class to implement common methods to leverage across your
    entries. You can otherwise implement all the interface's method in each new
    entry you implement. 

    Two methods from the `FormNavigatorEntry` interface that you can implement
    in a base class are `setSerlectContext` and `getLabel`. The Form Nav
    Extension portlet's `BaseMyAppFormNavigatorEntry` class, for example,
    implements these methods. 

        public abstract class BaseMyAppFormNavigatorEntry
            extends BaseJSPFormNavigatorEntry<Object>
            implements FormNavigatorEntry<Object> {

            @Override
            public String getLabel(Locale locale) {
                ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
                        "content.Language", locale, getClass());

                    return resourceBundle.getString(getKey());
            }

            @Override
            @Reference(target = "(osgi.web.symbolicname=com.liferay.docs.formnavportlet)", unbind = "-")
            public void setServletContext(ServletContext servletContext) {
                super.setServletContext(servletContext);
            }
        }

    For the entry to apply to it's app, its servlet context must target it's
    OSGi bundle. The `target` and `unbind` properties in the
    `setServlectContext` method's `@Reference` annotation accomplish this. The
    `getLabel` method translates an entry's key, by locale, as a label for the
    navigation entry. It's convienent, but optional, to use the key value as the
    entry's label. 

2.  In your `FormNavigatorEntry` implementation, you must also identify the
    existing form navigation you're extending. To do so, implement method
    `getFormNavigatorId` to return that navigation's form navigator ID. All of
    Liferay Portal's form navigator and form navigatory category identifiers are
    specified in class
    [`FormNavigatorConstants`](https://github.com/liferay/liferay-portal/blob/master/portal-service/src/com/liferay/portal/kernel/servlet/taglib/ui/FormNavigatorConstants.java). 

        @Override
        public String getFormNavigatorId() {
            return FormNavigatorConstants.FORM_NAVIGATOR_ID_COMPANY_SETTINGS;
        }

3.  Implement method `getCategoryKey` to return the ID of the form navigator
    category to which each is to be added. The class `FormNavigatorConstants`
    specifies Liferay Portal's category IDs. 

        @Override
        public String getCategoryKey() {
            return FormNavigatorConstants.CATEGORY_KEY_COMPANY_SETTINGS_MISCELLANEOUS;
        }

4.  Implement a `getKey` method to return a key that uniquely identifies your
    entry within the form navigator.  

5.  To map your entry class to your form, implement method `getJspPath` to
    return the path to your form's JSP. 

        @Override
        protected String getJspPath() {
            return "/portal_settings/my_app.jsp";
        }

6.  Your form navigation entry implementation needs to be registered in the OSGi
    registry so the form navigator tag can retrieve it.  To make sure your class
    registers, declare your entry class as a `FormNavigatorEntry.class` service by
    adding, above your class declaration, a `@Component` annotation like this:

        @Component(immediate = true, service = FormNavigatorEntry.class)

7.  You can optionally specify where your entry should be displayed among the
    category's existing entries.  You do this by specifing a `service.ranking`
    OSGi property in the entry class' `@Component` annotation. The higher the
    entry's relative service ranking, the higher it's displayed within the
    category's list of entries. Here's an example component annotation that
    includes a service ranking: 

            @Component(
            immediate = true, property = {"service.ranking:Integer=20"},
            service = FormNavigatorEntry.class
        )

8.  Create a JSP for your form navigation entry's form. The Form Nav Extension
    portlet's JSP provides a checkbox input to enable or disable a feature in
    the example application. 

        <%@ include file="../init.jsp" %>

        <h3>My App Feature<h3>

        <%
        boolean companyMyAppFeatureEnabled = GetterUtil.getBoolean(request.getAttribute(MyAppWebKeys.COMPANY_MY_APP_FEATURE_ENABLED));
        %>

        <aui:input checked="<%= companyMyAppFeatureEnabled %>" label="My App Feature Enabled" name="settings--myAppFeatureEnabled--" type="checkbox" value="<%= companyMyAppFeatureEnabled %>" />

9.  Add to your project's `bnd.bnd` file a unique web context path for your
    application. The context path supports referencing your the form pages from
    your Java classes. Here's the `bnd.bnd` file entry that the Form Nav
    Extension portlet uses:

        Web-ContextPath: /formnavportlet

That's all there is to adding section entries to Liferay's form navigators.
Adding categories to existing navigators is even easier. 

To include a new category in a existing form navigator, you must implement the
[`FormNavigatorCategory`](https://github.com/liferay/liferay-portal/blob/master/portal-service/src/com/liferay/portal/kernel/servlet/taglib/ui/FormNavigatorCategory.java)
interface. You must identify the form navigator in which the category is to be
included and implement method `getFormNavigatorId` to return that form
navigator's ID. Values of existing form navigator IDs used in the portal can be
found in the class `FormNavigatorConstants.java`. 

Category implementations also need to be registered in the OSGi registry so
the form navigator taglib can retrieve them. The order in which the categories
will be displayed is based on the `service.ranking` OSGi property. The higher
the service ranking is, the higher the category appears in the form navigation. 

There you have it! You know what it takes to extend Liferay form navigators with
new categories and section entries.
