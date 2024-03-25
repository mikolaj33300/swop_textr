package layouttree;

import java.util.ArrayList;
import java.util.Arrays;

public class HorizontalLayoutNode extends LayoutNode {

    /**
     * Constructor for HorizontalLayoutNode, clones its arguments to prevent representation exposure
     */
    public HorizontalLayoutNode(ArrayList<Layout> newChildren) {
        super(newChildren);
    }

    /**
     * Returns the starting x-coordinate of this HorizontalLayoutNode
     */
    @Override
    public int getStartX(Layout l, int terminalWidth, int terminalHeight) {
        if(parent != null){
            int thisX = parent.getStartX(this, terminalWidth, terminalHeight);
            int thisWidth = parent.getWidth(this, terminalWidth, terminalHeight);
            return thisX+(thisWidth/ children.size())*children.indexOf(l);
        }
        return (terminalWidth/ children.size())*children.indexOf(l);
    }

    /**
     * Returns the starting y-coordinate of this HorizontalLayoutNode
     */
    @Override
    public int getStartY(Layout l, int terminalWidth, int terminalHeight) {
        if(parent != null){
            return parent.getStartY(this, terminalWidth, terminalHeight);
        }
        return 0;
    }

    /**
     * Returns the width y-coordinate of this HorizontalLayoutNode
     */
    @Override
    public int getWidth(Layout l, int terminalWidth, int terminalHeight) {
        if(parent != null){
            return parent.getWidth(this, terminalWidth, terminalHeight)/this.children.size();
        }
        return terminalWidth / children.size();
    }

    /**
     * Returns the starting height of this HorizontalLayoutNode
     */
    @Override
    public int getHeight(Layout l, int terminalWidth, int terminalHeight) {
        if(parent != null){
            return parent.getHeight(this, terminalWidth, terminalHeight);
        }
        return terminalWidth;

    }

    /**
     * Returns the orientation of the HorizontalLayoutNode
     */
    @Override
    public Orientation getOrientation() {
        return Orientation.HORIZONTAL;
    }

    /**
     * Rotates the HorizontalLayoutNode's child and its neighbour on the right
     */
    @Override
    protected LayoutNode getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child, LayoutLeaf newSibling) {
        if (newSibling == null) {
            throw new NullPointerException();
        }
        ArrayList<Layout> newChildren;
        if (rotdir == ROT_DIRECTION.CLOCKWISE) {
            newChildren = new ArrayList<>(Arrays.asList(child, newSibling));
        } else {
            newChildren = new ArrayList<>(Arrays.asList(newSibling, child));
        }
        return new VerticalLayoutNode(newChildren);
    }

    /**
     * Checks a given LayoutNode can be a child of this HorizontalLayoutNode
     * A child should have a different orientation than its parent
     */
    @Override
    protected boolean isAllowedToBeChildOf(LayoutNode layoutNode) {
        return layoutNode.getOrientation() != Orientation.HORIZONTAL;
    }

    /**
     * Makes a deepcopy of HorizontalLayoutNode
     * The references to this object and its contents will be removed
     */
    @Override
    public HorizontalLayoutNode clone() {
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for (Layout l : children) {
            deepCopyList.add(l.clone());
        }
        HorizontalLayoutNode cloned = new HorizontalLayoutNode(deepCopyList);
        cloned.setContainsActiveView(this.getContainsActiveView());
        return cloned;
    }

    /**
     * Returns whether this HorizontalLayoutNode and the given object are equals in contents
     */
    @Override
    public boolean equals(Object node) {
        if (node instanceof HorizontalLayoutNode layoutNode) {
            //Check objects for same activity-status
            if (this.getContainsActiveView() != layoutNode.getContainsActiveView()) {
                return false;
            }
            // Return early when the amount of children don't match.
            if (layoutNode.children.size() != this.children.size()) return false;
            // Loop over the children of both
            for (int i = 0; i < layoutNode.children.size(); i++) {
                // We keep checking if they are equal, if not we return early.
                if (!layoutNode.children.get(i).equals(this.children.get(i))) return false;
            }
            // If the process didn't find disparities, the layouts are equal.
            return true;
        }
        return false;
    }

}