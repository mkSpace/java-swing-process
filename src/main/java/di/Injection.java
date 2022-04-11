package di;

import ui.MainView;
import ui.View;

public class Injection {

    public static View provideView() {
        return new MainView();
    }

}
