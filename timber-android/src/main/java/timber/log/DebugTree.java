package timber.log;

/**
 * A predefined tree for Android that uses the {@link AndroidLogLevel} and
 * {@link AndroidDebugLogStrategy}.
 */
public class DebugTree extends Tree {
  public DebugTree() {
    super(new AndroidLogLevel(), new AndroidDebugLogStrategy());
  }
}
