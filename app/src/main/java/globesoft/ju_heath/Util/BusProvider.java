package globesoft.ju_heath.Util;

import com.squareup.otto.Bus;

/**
 * Created by system777 on 2018-02-05.
 */

public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {

    }
}
