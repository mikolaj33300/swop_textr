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
    protected Layout getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child) {
        LayoutLeaf newSibling = this.getRightNeighbor(child);
        ArrayList<Layout> newChildren;
        if(rotdir == ROT_DIRECTION.CLOCKWISE){
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
     Makes a deepcopy of HorizontalLayoutNode
     The references to this object and its contents will be removed
     */
    @Override
    protected HorizontalLayoutNode clone() {
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : children){
            deepCopyList.add(l.clone());
        }
        HorizontalLayoutNode cloned = new HorizontalLayoutNode(deepCopyList);
        cloned.setContainsActive(this.getContainsActive());
        return cloned;
    }

    @Override
    public void render(int xChild, int yChild, int width, int heightChild){
        int widthChild = width/children.size(); //rounds down

        for(Layout child : children){
            child.render(xChild, yChild, widthChild, heightChild);
            xChild = xChild + widthChild;
        }
    }
}