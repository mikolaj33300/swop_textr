package layouttree;

import ui.UICoords;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VerticalLayoutNode extends LayoutNode{


    /**
     * Constructor for VerticalLayoutNode, clones its arguments to prevent representation exposure
     */
    public VerticalLayoutNode(ArrayList<Layout> newChildren) {
        super(newChildren);
    }

    /**
     * Returns the orientation of the VerticalLayoutNode
     */
    @Override
    public Orientation getOrientation() {
        return Orientation.VERTICAL;
    }

    /**
     * Rotates the VerticalLayoutNode's child and its neighbour on the right
     */
    @Override
    protected LayoutNode getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child, LayoutLeaf newSibling) {
        if(newSibling == null){
            throw new NullPointerException();
        }
        ArrayList<Layout> newChildren;
        if(rotdir == ROT_DIRECTION.CLOCKWISE){
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
     Makes a deepcopy of VerticalLayoutNode
     The references to this object and its contents will be removed
     */
    @Override
    public VerticalLayoutNode clone() {
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : children){
            deepCopyList.add(l.clone());
        }
        VerticalLayoutNode cloned = new VerticalLayoutNode(deepCopyList);
        return cloned;
    }

    /**
     * Returns whether this HorizontalLayoutNode and the given object are equals in contents
     */
    @Override
    public boolean equals(Object node) {
        if(node instanceof VerticalLayoutNode layoutNode) {
            //Check objects for same activity-status
            if(this.getContainsActiveView() != layoutNode.getContainsActiveView()){
                return false;
            }
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

    @Override
    public List<UICoords> getCoordsList(UICoords uiCoords) {
        int heightChild = uiCoords.height / children.size(); //rounds down
        int yChild = uiCoords.startY;
        ArrayList<UICoords> resultList = new ArrayList<>();
        for (Layout child : children) {
            resultList.addAll(child.getCoordsList(new UICoords(uiCoords.startX, yChild, heightChild, uiCoords.height)));
            yChild = yChild + heightChild;
        }
        return resultList;
    }
}