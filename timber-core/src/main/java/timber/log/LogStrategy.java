package timber.log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

public abstract class LogStrategy {
  protected static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

  /**
   * Write a log message to its destination. Called for all level-specific methods by default.
   *
   * @param priority the priority as returned from one of the methods in {@link LogLevel}.
   * @param tag the tag to attach to this log message.
   * @param message the log message itself.
   * @param t the {@link Throwable} to log.
   */
  protected abstract void performLog(int priority, String tag, String message, Throwable t);

  /**
   * The tag to use for {@link #performLog(int, String, String, Throwable)}.
   *
   * @param customTag the tag that the user set using {@link Timber#tag(String)}, or {@code null}
   * if the user did not set a tag before this log call.
   */
  protected abstract String getTag(@Nullable String customTag);

  /**
   * A check that is run before all log operations to determine if this log should actually be
   * performed.
   *
   * @param priority the priority as returned from one of the methods in {@link LogLevel}.
   * @param message the log message itself.
   * @param t the {@link Throwable} to log.
   * @return true if a log with this priority should go through, false if not
   */
  protected abstract boolean shouldLog(int priority, String message, Throwable t);

  /**
   * Extract the tag which should be used for the message from the {@code element}. By default
   * this will use the class name without any anonymous class suffixes (e.g., {@code Foo$1}
   * becomes {@code Foo}).
   * <p>
   * Note: This will not be called if a {@linkplain Timber#tag(String) manual tag} was specified.
   */
  protected String createStackElementTag(StackTraceElement element) {
    String tag = element.getClassName();
    Matcher m = ANONYMOUS_CLASS.matcher(tag);
    if (m.find()) {
      tag = m.replaceAll("");
    }
    return tag.substring(tag.lastIndexOf('.') + 1);
  }
}
