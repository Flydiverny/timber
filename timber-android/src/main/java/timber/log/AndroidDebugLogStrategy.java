package timber.log;

import android.util.Log;

/**
 * How most people will want to log on Android when BuildConfig.DEBUG is true.
 */
public class AndroidDebugLogStrategy extends LogStrategy {

  protected static final int MAX_LOG_LENGTH = 4000;
  protected static final int CALL_STACK_INDEX = 4;

  @Override protected void performLog(int priority, String tag, String message, Throwable t) {
    if (message.length() < MAX_LOG_LENGTH) {
      log(priority, tag, message);
      return;
    }

    // Split by line, then ensure each line can fit into Log's maximum length.
    for (int i = 0, length = message.length(); i < length; i++) {
      int newline = message.indexOf('\n', i);
      newline = newline != -1 ? newline : length;
      do {
        int end = Math.min(newline, i + MAX_LOG_LENGTH);
        String part = message.substring(i, end);
        log(priority, tag, part);
        i = end;
      } while (i < newline);
    }
  }

  @Override protected String getTag(String customTag) {
    if (customTag != null) {
      return customTag;
    }

    // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
    // because Robolectric runs them on the JVM but on Android the elements are different.
    StackTraceElement[] stackTrace = new Throwable().getStackTrace();
    if (stackTrace.length <= CALL_STACK_INDEX) {
      throw new IllegalStateException(
          "Synthetic stacktrace didn't have enough elements: are you using proguard?");
    }
    return createStackElementTag(stackTrace[CALL_STACK_INDEX]);
  }

  @Override protected boolean shouldLog(int priority, String message, Throwable t) {
    return true;
  }

  protected void log(int priority, String tag, String message) {
    if (priority == Log.ASSERT) {
      Log.wtf(tag, message);
    } else {
      Log.println(priority, tag, message);
    }
  }
}
