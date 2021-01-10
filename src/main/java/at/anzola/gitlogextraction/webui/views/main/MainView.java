package at.anzola.gitlogextraction.webui.views.main;

import at.anzola.gitlogextraction.webui.views.about.AboutView;
import at.anzola.gitlogextraction.webui.views.charts.ChartsView;
import at.anzola.gitlogextraction.webui.views.list.ListView;
import at.anzola.gitlogextraction.webui.views.upload.UploadView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 *
 * @author fabioanzola
 */
@CssImport("./styles/views/main/main-view.css")
@PWA(name = "GITLE", shortName = "GITLE", enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
public class MainView extends AppLayout {

    /**
     * Contains the Main Menu
     */
    private final Tabs menu;

    /**
     * Tht Title of the Project
     */
    private H1 viewTitle;

    /**
     * Constructor, sets private references
     */
    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    /**
     * Sets the Header contents
     *
     * @return The Header
     */
    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        layout.add(new Image("images/user.svg", "Avatar"));
        return layout;
    }

    /**
     * Sets the Drawer contents
     *
     * @param menu The private reference for the menu
     * @return The Drawer
     */
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "GITLE logo"));
        logoLayout.add(new H1("GITLE"));

        logoLayout.add(new Button(new Icon(VaadinIcon.LIGHTBULB), click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        }));

        layout.add(logoLayout, menu);
        return layout;
    }

    /**
     * Creates the menu
     *
     * @return The menu
     */
    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    /**
     * Creates the menu items
     *
     * @return The Menu-items
     */
    private Component[] createMenuItems() {
        return new Tab[]{createTab("Upload", UploadView.class), createTab("List", ListView.class),
                createTab("Charts", ChartsView.class), createTab("About", AboutView.class)};
    }

    /**
     * Creates the tabs
     *
     * @param text             The text to be set
     * @param navigationTarget The navigation target for the tab
     * @return The Tab
     */
    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    /**
     * Sets what should happen after navigating
     */
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    /**
     * Collector for the component of the tab
     *
     * @param component The selected component
     * @return The Tab for the component
     */
    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(
            tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    /**
     * Gets current page title
     *
     * @return The page title of the current page
     */
    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
