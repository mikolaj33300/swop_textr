package files;

/**
 * Suggestie om File en FileBuffer samen te voegen.
 *
 * Onze File klasse zou een java IO File bevatten...
 *
 * Dit is enkel een suggestie je kan dit makkelijk verwijderen.
 * Voor de zekerheid heb ik de andere klassen geïmplementeerd volgens het schema
 * Ook dat is maar een suggestie en mogen volledig herschreven worden naar eigen
 * wil.
 */

public class 
FileBuffer 
{

    /**
     * file reference
     */
  private FileHolder file;
  /**
   * check if buffer modified
   */
  private boolean dirty = false;
    /**
     * Holds the 'in memory' model from the file.
     *
     * @representationObject
     */
  private String content;

  /**
   * Creates FileBuffer object with given path;
   * @param path
   */
  public 
  FileBuffer(String path) 
  {
    this.file = new FileHolder(path);
    this.content = this.file.getContent();
  }

  /**
   * Updates the content of the FileBuffer
   */
  public final void 
  update (String updatedContents) 
  {
      this.content = updatedContents;
      dirty = true;
  }

  /**
   * Saves the buffer contents to disk
   */
  public final void 
  save () 
  {
      if(!dirty) return;
      this.file.save(this.content);
      this.dirty = false;
  }

  /**
   * Clones this object
   * @return
   */
  public FileBuffer 
  clone () 
  {
      FileBuffer copy = new FileBuffer(this.file.getPath());
      copy.dirty = this.dirty;
      copy.content = new String(this.content);
      return copy;
  }
  public final String
  getContent ()
  {
    return this.file.getContent();
  }
}
