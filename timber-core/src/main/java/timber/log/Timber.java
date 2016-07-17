package timber.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NonNls;

import static java.util.Collections.unmodifiableList;

/** Logging for lazy people. */
public final class Timber {
  /** Log a verbose message with optional format args. */
  public static void v(@NonNls String message, Object... args) {
    TREE_OF_SOULS.v(null, message, args);
  }

  /** Log a verbose exception and a message with optional format args. */
  public static void v(Throwable t, @NonNls String message, Object... args) {
    TREE_OF_SOULS.v(t, message, args);
  }

  /** Log a debug message with optional format args. */
  public static void d(@NonNls String message, Object... args) {
    TREE_OF_SOULS.d(null, message, args);
  }

  /** Log a debug exception and a message with optional format args. */
  public static void d(Throwable t, @NonNls String message, Object... args) {
    TREE_OF_SOULS.d(t, message, args);
  }

  /** Log an info message with optional format args. */
  public static void i(@NonNls String message, Object... args) {
    TREE_OF_SOULS.i(null, message, args);
  }

  /** Log an info exception and a message with optional format args. */
  public static void i(Throwable t, @NonNls String message, Object... args) {
    TREE_OF_SOULS.i(t, message, args);
  }

  /** Log a warning message with optional format args. */
  public static void w(@NonNls String message, Object... args) {
    TREE_OF_SOULS.w(null, message, args);
  }

  /** Log a warning exception and a message with optional format args. */
  public static void w(Throwable t, @NonNls String message, Object... args) {
    TREE_OF_SOULS.w(t, message, args);
  }

  /** Log an error message with optional format args. */
  public static void e(@NonNls String message, Object... args) {
    TREE_OF_SOULS.e(null, message, args);
  }

  /** Log an error exception and a message with optional format args. */
  public static void e(Throwable t, @NonNls String message, Object... args) {
    TREE_OF_SOULS.e(t, message, args);
  }

  /** Log an assert message with optional format args. */
  public static void wtf(@NonNls String message, Object... args) {
    TREE_OF_SOULS.wtf(null, message, args);
  }

  /** Log an assert exception and a message with optional format args. */
  public static void wtf(Throwable t, @NonNls String message, Object... args) {
    TREE_OF_SOULS.wtf(t, message, args);
  }

  /** Log at {@code priority} a message with optional format args. */
  public static void log(int priority, @NonNls String message, Object... args) {
    TREE_OF_SOULS.log(priority, null, message, args);
  }

  /** Log at {@code priority} an exception and a message with optional format args. */
  public static void log(int priority, Throwable t, @NonNls String message, Object... args) {
    TREE_OF_SOULS.log(priority, t, message, args);
  }

  /**
   * A view into Timber's planted trees as a tree itself. This can be used for injecting a logger
   * instance rather than using static methods or to facilitate testing.
   */
  public static Tree asTree() {
    return TREE_OF_SOULS;
  }

  /** Set a one-time tag for use on the next logging call. */
  public static Tree tag(String tag) {
    Tree[] forest = forestAsArray;
    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, count = forest.length; i < count; i++) {
      forest[i].explicitTag.set(tag);
    }
    return TREE_OF_SOULS;
  }

  /** Add a new logging tree. */
  public static void plant(Tree tree) {
    if (tree == null) {
      throw new NullPointerException("tree == null");
    }
    if (tree == TREE_OF_SOULS) {
      throw new IllegalArgumentException("Cannot plant Timber into itself.");
    }
    synchronized (FOREST) {
      FOREST.add(tree);
      forestAsArray = FOREST.toArray(new Tree[FOREST.size()]);
    }
  }

  /** Adds new logging trees. */
  public static void plant(Tree... trees) {
    if (trees == null) {
      throw new NullPointerException("trees == null");
    }
    for (Tree tree : trees) {
      if (tree == null) {
        throw new NullPointerException("trees contains null");
      }
      if (tree == TREE_OF_SOULS) {
        throw new IllegalArgumentException("Cannot plant Timber into itself.");
      }
    }
    synchronized (FOREST) {
      Collections.addAll(FOREST, trees);
      forestAsArray = FOREST.toArray(new Tree[FOREST.size()]);
    }
  }

  /** Remove a planted tree. */
  public static void uproot(Tree tree) {
    synchronized (FOREST) {
      if (!FOREST.remove(tree)) {
        throw new IllegalArgumentException("Cannot uproot tree which is not planted: " + tree);
      }
      forestAsArray = FOREST.toArray(new Tree[FOREST.size()]);
    }
  }

  /** Remove all planted trees. */
  public static void uprootAll() {
    synchronized (FOREST) {
      FOREST.clear();
      forestAsArray = TREE_ARRAY_EMPTY;
    }
  }

  /** Return a copy of all planted {@linkplain Tree trees}. */
  public static List<Tree> forest() {
    synchronized (FOREST) {
      return unmodifiableList(new ArrayList<Tree>(FOREST));
    }
  }

  public static int treeCount() {
    synchronized (FOREST) {
      return FOREST.size();
    }
  }

  private static final Tree[] TREE_ARRAY_EMPTY = new Tree[0];
  // Both fields guarded by 'FOREST'.
  private static final List<Tree> FOREST = new ArrayList<Tree>();
  static volatile Tree[] forestAsArray = TREE_ARRAY_EMPTY;

  /** A {@link Tree} that delegates to all planted trees in the {@linkplain #FOREST forest}. */
  private static final Tree TREE_OF_SOULS = new Tree(null, null) {

    @Override public void v(Throwable t, String message, Object... args) {
      Tree[] forest = forestAsArray;
      //noinspection ForLoopReplaceableByForEach
      for (int i = 0, count = forest.length; i < count; i++) {
        forest[i].log(forest[i].logLevel.verboseLevel(), t, message, args);
      }
    }

    @Override public void d(Throwable t, String message, Object... args) {
      Tree[] forest = forestAsArray;
      //noinspection ForLoopReplaceableByForEach
      for (int i = 0, count = forest.length; i < count; i++) {
        forest[i].log(forest[i].logLevel.debugLevel(), t, message, args);
      }
    }

    @Override public void i(Throwable t, String message, Object... args) {
      Tree[] forest = forestAsArray;
      //noinspection ForLoopReplaceableByForEach
      for (int i = 0, count = forest.length; i < count; i++) {
        forest[i].log(forest[i].logLevel.infoLevel(), t, message, args);
      }
    }

    @Override public void w(Throwable t, String message, Object... args) {
      Tree[] forest = forestAsArray;
      //noinspection ForLoopReplaceableByForEach
      for (int i = 0, count = forest.length; i < count; i++) {
        forest[i].log(forest[i].logLevel.warnLevel(), t, message, args);
      }
    }

    @Override public void e(Throwable t, String message, Object... args) {
      Tree[] forest = forestAsArray;
      //noinspection ForLoopReplaceableByForEach
      for (int i = 0, count = forest.length; i < count; i++) {
        forest[i].log(forest[i].logLevel.errorLevel(), t, message, args);
      }
    }

    @Override public void wtf(Throwable t, String message, Object... args) {
      Tree[] forest = forestAsArray;
      //noinspection ForLoopReplaceableByForEach
      for (int i = 0, count = forest.length; i < count; i++) {
        forest[i].log(forest[i].logLevel.wtfLevel(), t, message, args);
      }
    }

    @Override void log(int priority, Throwable t, String message, Object... args) {
      Tree[] forest = forestAsArray;
      //noinspection ForLoopReplaceableByForEach
      for (int i = 0, count = forest.length; i < count; i++) {
        forest[i].log(priority, t, message, args);
      }
    }
  };

  private Timber() {
    throw new AssertionError("No instances.");
  }

}
