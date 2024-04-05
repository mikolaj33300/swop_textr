package ui;

import java.util.ArrayList;
import java.io.IOException;
import io.github.btj.termios.Terminal;


public class Render {
    ArrayList<View> Views;
    
  Render(int[] hashes, Terminal Terminal) throws IOException {
    for (int i = 0; i < hashes.length; i++) {
        Views.add(new FileBufferView(Terminal));
    }
  }

  public void appendView(View view){
    this.Views.add(view);
  }

  public int getHash(int i) {
    return this.Views.get(i).getHash();
  }
}
