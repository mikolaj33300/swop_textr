package ui;

import java.util.ArrayList;

class ViewWId {
    public final View view;
    public final int hash;
}


public class Render {
    ArrayList<ViewWId> Views;
    
    Render(int[] hashes) {
	for (int i = 0; i < hashes.length; i++) {
	    Views.add(null);
	}
    }
}
