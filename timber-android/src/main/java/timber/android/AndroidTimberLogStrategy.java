package timber.android;

import android.util.Log;
import timber.log.Timber;

public class AndroidTimberLogStrategy implements Timber.LogStrategy {
  @Override public int verboseLevel() {
    return Log.VERBOSE;
  }

  @Override public int debugLevel() {
    return Log.DEBUG;
  }

  @Override public int infoLevel() {
    return Log.INFO;
  }

  @Override public int warnLevel() {
    return Log.WARN;
  }

  @Override public int errorLevel() {
    return Log.ERROR;
  }

  @Override public int wtfLevel() {
    return Log.ASSERT;
  }

  @Override public void performLog(int priority, String tag, String message) {
    if (priority == wtfLevel()) {
      Log.wtf(tag, message);
    } else {
      Log.println(priority, tag, message);
    }
  }
}
