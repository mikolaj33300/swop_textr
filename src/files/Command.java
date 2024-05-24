package files;

public interface Command{

  /**
   * Undo the command
   */
  void undo();

  /**
   * Execute the command
   */
  void execute();
}
