package timber.log;

import android.util.Log;

/**
 * A provided implementation of {@link LogLevel} for {@link android.util.Log}. Can be used when
 * constructing any new {@link Tree} instances on Android that don't need any special log-levels.
 */
public class AndroidLogLevel implements LogLevel {

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
}
