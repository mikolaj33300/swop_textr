package controller;

public class TSMediator {
    private final TextR c;
    private final int index;

    TSMediator(TextR c, int index){
	this.c = c;
	this.index = index;
    }

    public void gainFocus(){
	c.setAdapter(index);
    }

    public void loseFocus() {
	c.setAdapter(0);
    }
}
