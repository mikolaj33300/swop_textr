package ui;

import java.util.ArrayList;


public class Render {
    ArrayList<View> Views;
    
  Render(int[] hashes) {
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
