package ui;

import java.util.ArrayList;
import java.io.IOException;


public class Render {
    ArrayList<View> Views;
    
  public Render() throws IOException {
    this.Views = new ArrayList<View>();
  }

  public void appendFileBufferView(int hash) throws IOException {
    this.Views.add(new FileBufferView(hash));
  }
}
