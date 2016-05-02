package timber.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.jetbrains.annotations.NotNull;

/**
 * A facade for handling logging calls. Install instances via {@link Timber#plant Timber.plant()}.
 */
public class Tree {

  @NotNull protected final LogLevel logLevel;
  @NotNull protected final LogStrategy logStrategy;

  final ThreadLocal<String> explicitTag = new ThreadLocal<String>();

  public Tree(@NotNull LogLevel logLevel, @NotNull LogStrategy logStrategy) {
    this.logLevel = logLevel;
    this.logStrategy = logStrategy;
  }

  /** Log a verbose message with optional format args. */
  public void v(String message, Object... args) {
    v(null, message, args);
  }

  /** Log a verbose exception and a message with optional format args. */
  public void v(Throwable t, String message, Object... args) {
    log(logLevel.verboseLevel(), t, message, args);
  }

  /** Log a debug message with optional format args. */
  public void d(String message, Object... args) {
    d(null, message, args);
  }

  /** Log a debug exception and a message with optional format args. */
  public void d(Throwable t, String message, Object... args) {
    log(logLevel.debugLevel(), t, message, args);
  }

  /** Log an info message with optional format args. */
  public void i(String message, Object... args) {
    i(null, message, args);
  }

  /** Log an info exception and a message with optional format args. */
  public void i(Throwable t, String message, Object... args) {
    log(logLevel.infoLevel(), t, message, args);
  }

  /** Log a warning message with optional format args. */
  public void w(String message, Object... args) {
    w(null, message, args);
  }

  /** Log a warning exception and a message with optional format args. */
  public void w(Throwable t, String message, Object... args) {
    log(logLevel.warnLevel(), t, message, args);
  }

  /** Log an error message with optional format args. */
  public void e(String message, Object... args) {
    e(null, message, args);
  }

  /** Log an error exception and a message with optional format args. */
  public void e(Throwable t, String message, Object... args) {
    log(logLevel.errorLevel(), t, message, args);
  }

  /** Log an assert message with optional format args. */
  public void wtf(String message, Object... args) {
    wtf(null, message, args);
  }

  /** Log an assert exception and a message with optional format args. */
  public void wtf(Throwable t, String message, Object... args) {
    log(logLevel.wtfLevel(), t, message, args);
  }


  void log(int priority, Throwable t, String message, Object... args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }
    if (!logStrategy.shouldLog(priority, message, t)) {
      return;
    }
    if (message != null && message.length() == 0) {
      message = null;
    }
    if (message == null) {
      if (t == null) {
        return; // Swallow message if it's null and there's no throwable.
      }
      message = getStackTraceString(t);
    } else {
      if (t != null) {
        message += "\n" + getStackTraceString(t);
      }
    }
    logStrategy.performLog(priority, logStrategy.getTag(getTag()), message, t);
  }

  private String getTag() {
    String tag = explicitTag.get();
    if (tag != null) {
      explicitTag.remove();
    }
    return tag;
  }

  private String getStackTraceString(Throwable t) {
    // Don't replace this with Log.getStackTraceString() - it hides
    // UnknownHostException, which is not what we want.
    StringWriter sw = new StringWriter(256);
    PrintWriter pw = new PrintWriter(sw, false);
    t.printStackTrace(pw);
    pw.flush();
    return sw.toString();
  }
}
