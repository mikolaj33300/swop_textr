package layouttree;

import util.RotationDirection;
import util.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class VerticalLayoutNode extends LayoutNode{

    /**
     * Constructor for VerticalLayoutNode
     * The given newChildren will  be cloned before setting the children of this HorizontalLayoutNode
     * There must be atleast 2 Layouts in newChildren for the VerticalLayoutNode to be instantiated correctly
     */
    public VerticalLayoutNode(ArrayList<Layout> newChildren) {
        super(newChildren);
    }

    public VerticalLayoutNode(int hashes[]) {
      super(hashes);
    }

    /**
     * Returns the VERTICAL orientation since this is a VerticalLayoutNode
     */
    @Override
    public Orientation getOrientation() {
        return Orientation.VERTICAL;
    }

    /**
     * Returns a LayoutNode in which the given newSibling LayoutLeaf will be rotated around the given child-layout
     * clockwise or counterclockwise, based on the given rotdir
     */
    @Override
    protected LayoutNode getNewMergedRotatedChild(RotationDirection rotdir, Layout child, LayoutLeaf newSibling) {
        if(newSibling == null){
            throw new NullPointerException();
        }
        ArrayList<Layout> newChildren;
        if(rotdir == RotationDirection.CLOCKWISE){
            newChildren = new ArrayList<>(Arrays.asList(newSibling, child));
        } else {
            newChildren = new ArrayList<>(Arrays.asList(child, newSibling));
        }
        return new HorizontalLayoutNode(newChildren);
    }

    /**
     * Checks a given LayoutNode can be a child of this VerticalLayoutNode
     * A child should have a different orientation than its parent
     */
    @Override
    protected boolean isAllowedToBeChildOf(LayoutNode layoutNode) {
        return layoutNode.getOrientation() != Orientation.VERTICAL;
    }

    /**
     * Returns HashMap of coords connected to the containedHashCodes of the LayoutLeafs
     * The rectangle defines which part of the terminal, this LayoutLeaf will fill up
     * The result is scaled by the given rectangle uiCoordsScaled
     */
    @Override
    public HashMap<Integer, Rectangle> getCoordsList(Rectangle uiCoordsScaled) {
        double heightChild = uiCoordsScaled.height / ((double) children.size()); //rounds down
        double yChild = uiCoordsScaled.startY;
        HashMap<Integer, Rectangle> resultMap = new HashMap<Integer, Rectangle>();
        for (Layout child : children) {
            resultMap.putAll(child.getCoordsList(new Rectangle(uiCoordsScaled.startX, yChild, uiCoordsScaled.width, heightChild)));
            yChild = yChild + heightChild;
        }
        return resultMap;
    }

    /**
     * Returns a deepcopy of this VerticalLayoutNode
     * The reference to this VerticalLayoutNode will be lost and
     * Every Layout-child of this VerticalLayoutNode will also be cloned
     * The parent of this VerticalLayoutNode will not be brought over to the clone
     */
    @Override
    public VerticalLayoutNode clone() {
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : children){
            deepCopyList.add(l.clone());
        }
        return new VerticalLayoutNode(deepCopyList);
    }

    /**
     * Returns true if this VerticalLayoutNode and the given Object are equal content-wise, false otherwise
     * They are equal content-wise when the object is a VerticalLayoutNode and the equals-function succeeds for each
     * of their children
     */
    @Override
    public boolean equals(Object node) {
        if(node instanceof VerticalLayoutNode layoutNode) {
            // Return early when the amount of children don't match.
            if(layoutNode.children.size() != this.children.size()) return false;
            // Loop over the children of both
            for(int i = 0; i < layoutNode.children.size(); i++) {
                // We keep checking if they are equal, if not we return early.
                if(!layoutNode.children.get(i).equals(this.children.get(i))) return false;
            }
            // If the process didn't find disparities, the layouts are equal.
            return true;
        }
        return false;
    }
}
