package ui;

import java.util.ArrayList;
import java.io.IOException;


public class Render {
    ArrayList<View> Views;
    
  Render(int[] hashes) throws IOException {
    for (int i = 0; i < hashes.length; i++) {
        Views.add(new FileBufferView());
    }
  }

  public void appendView(View view){
    this.Views.add(view);
  }

  public int getHash(int i) {
    return this.Views.get(i).getHash();
  }
}
