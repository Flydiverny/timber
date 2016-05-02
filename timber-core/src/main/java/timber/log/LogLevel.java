package timber.log;

public interface LogLevel {
  int verboseLevel();

  int debugLevel();

  int infoLevel();

  int warnLevel();

  int errorLevel();

  int wtfLevel();
}
