package layouttree;

import util.RotationDirection;
import util.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HorizontalLayoutNode extends LayoutNode {

    /**
     * Constructor for HorizontalLayoutNode
     * @param newChildren will thus be cloned before setting the children of this HorizontalLayoutNode
     * There must be atleast 2 Layouts in newChildren for the HorizontalLayoutNode to be instantiated correctly
     */
    public HorizontalLayoutNode(ArrayList<Layout> newChildren) {
        super(newChildren);
    }

    /**
     * @return the HORIZONTAL orientation since this is a HorizontalLayoutNode
     */
    @Override
    public Orientation getOrientation() {
        return Orientation.HORIZONTAL;
    }

    /**
     * @return a LayoutNode in which the given newSibling LayoutLeaf will be rotated around the given child-layout
     * @param rotdir clockwise or counterclockwise
     * @param child the new child
     * @param newSibling the new layoutnode sibling
     */
    @Override
    protected LayoutNode getNewMergedRotatedChild(RotationDirection rotdir, Layout child, LayoutLeaf newSibling) {
        if (newSibling == null) {
            throw new NullPointerException();
        }
        ArrayList<Layout> newChildren;
        if (rotdir == RotationDirection.CLOCKWISE) {
            newChildren = new ArrayList<>(Arrays.asList(child, newSibling));
        } else {
            newChildren = new ArrayList<>(Arrays.asList(newSibling, child));
        }
        return new VerticalLayoutNode(newChildren);
    }

    /**
     * Checks a given LayoutNode can be a child of this HorizontalLayoutNode
     * A child should have a different orientation than its parent
     * @return if the given layoutnode can be child of this
     */
    @Override
    protected boolean isAllowedToBeChildOf(LayoutNode layoutNode) {
        return layoutNode.getOrientation() != Orientation.HORIZONTAL;
    }


    /**
     * Returns HashMap of coords connected to the containedHashCodes of the LayoutLeafs
     * The rectangle defines which part of the terminal, this LayoutLeaf will fill up
     * The result is scaled by the given rectangle uiCoordsScaled
     */
    @Override
    public HashMap<Integer, Rectangle> getCoordsList(Rectangle uiCoordsScaled) {
        double widthChild = uiCoordsScaled.width / ((double) children.size()); //rounds down
        double xChild = uiCoordsScaled.startX;
        HashMap<Integer, Rectangle> resultMap = new HashMap<Integer, Rectangle>();
        for (Layout child : children) {
            resultMap.putAll(child.getCoordsList(new Rectangle(xChild, uiCoordsScaled.startY, widthChild, uiCoordsScaled.height)));
            xChild = xChild + widthChild;
        }
        return resultMap;
    }

    /**
     * @return a deepcopy of this HorizontalLayoutNode
     * The reference to this HorizontalLayoutNode will be lost and
     * Every Layout-child of this HorizontalLayoutNode will also be cloned
     * The parent of this HorizontalLayoutNode will not be brought over to the clone
     */
    @Override
    public HorizontalLayoutNode clone() {
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for (Layout l : children) {
            deepCopyList.add(l.clone());
        }
        return new HorizontalLayoutNode(deepCopyList);
    }

    /**
     * @return true if this HorizontalLayoutNode and the given Object are equal content-wise, false otherwise
     * They are equal content-wise when the object is a HorizontalLayoutNode and the equals-function succeeds for each
     * of their children
     */
    @Override
    public boolean equals(Object node) {
        if (node instanceof HorizontalLayoutNode layoutNode) {
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
