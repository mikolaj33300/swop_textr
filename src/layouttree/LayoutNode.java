package layouttree;

import java.io.IOException;
import java.util.ArrayList;

public abstract class LayoutNode extends Layout {

    /**
     * The children of this LayoutNode in the layout-tree
     */
    protected ArrayList<Layout> children;

    /**
     * Clones its arguments to prevent representation exposure
     * LayoutNodes need at least 2 children in its children-array
     */
    public LayoutNode(ArrayList<Layout> newChildren) {
        if (newChildren.size() < 2)
            throw new RuntimeException("newChildren too short");

        this.children = new ArrayList<>();
        for (Layout l : newChildren)
            if (l.isAllowedToBeChildOf(this))
                this.insertDirectChild(l.clone());
    }

    public void renderContent() throws IOException {
        for(Layout l : children){
            l.renderContent();
        }
    }

    public void renderCursor() throws IOException {
        for(Layout l : children){
            l.renderCursor();
        }
    }

    //Get start X of child l
    public abstract int getStartX(Layout l, int terminalWidth, int terminalHeight);

    public abstract int getStartY(Layout l, int terminalWidth, int terminalHeight);

    public abstract int getWidth(Layout l, int terminalWidth, int terminalHeight);

    public abstract int getHeight(Layout l, int terminalWidth, int terminalHeight);

    /**
     * Returns the orientation of the LayoutNode
     */
    public abstract Orientation getOrientation();

    /**
     * Returns a deepcopy of the children of this LayoutNode
     */
    public ArrayList<Layout> getDirectChildren() {
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for (Layout l : children) {
            deepCopyList.add(l.clone());
        }
        return deepCopyList;
    }

    /**
     * Moves focus from the currently active Layout, to the neigbouring layout
     * Which neighbour is decided by the dir argument
     * If no neighbours left, the active Layout stays active
     */
    public void moveFocus(DIRECTION dir) throws RuntimeException {
        for (Layout l : children) {
            if (l.getContainsActiveView()) {
                l.moveFocus(dir);
                return;
            }
        }
    }

    @Override
    public void moveCursor(char c) {
        for (Layout l : children) {
            if(l.getContainsActiveView()){
                l.moveCursor(c);
                return;
            }
        }
    }

    @Override
    public void enterText(byte b) {
        for (Layout l : children) {
            if(l.getContainsActiveView()){
                l.enterText(b);
                return;
            }
        }
    }

    @Override
    public void enterInsertionPoint() {
        for (Layout l : children) {
            if(l.getContainsActiveView()){
                l.enterInsertionPoint();
                return;
            }
        }
    }

    @Override
    public void saveActiveBuffer() {
        for (Layout l : children) {
            if(l.getContainsActiveView()){
                l.saveActiveBuffer();
                return;
            }
        }
    }

    @Override
    public void deleteCharacter() {
        for (Layout l : children) {
            if(l.getContainsActiveView()){
                l.deleteCharacter();
                return;
            }
        }
    }

    @Override
    public void closeActive(){
        for (Layout l : children) {
            if(l.getContainsActiveView()){
                l.closeActive();
                return;
            }
        }
    }

    @Override
    public void forcedCloseActive(){
        for (Layout l : children) {
            if(l.getContainsActiveView()){
                l.forcedCloseActive();
                return;
            }
        }
    }

    public void insertDirectChild(Layout toInsert){
        if(toInsert.isAllowedToBeChildOf(this)){
            Layout cloneOfInsert = toInsert.clone();
            cloneOfInsert.setParent(this);
            this.children.add(cloneOfInsert);
            if (cloneOfInsert.getContainsActiveView()) {
                this.containsActiveView = true;
            }
        }
    }

    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir) throws RuntimeException {
        for (Layout l : children) {
            if (l.getContainsActiveView()) {
                return l.rotateRelationshipNeighbor(rot_dir);
            }
        }
        throw new RuntimeException("Contains no active leaf!");
    }

    /**
     *
     */
    protected LayoutLeaf getRightNeighbor(Layout subtree) {
        int index = children.indexOf(subtree);
        if (index < children.size() - 1) {
            return children.get(index + 1).getLeftLeaf(); // Called when we can make child of current node the active one.
        } else {
            if (parent != null) {
                return parent.getRightNeighbor(this);
            } else {
                return null;
            }
            //called when we need to backtrack one level up
        }
    }

    /**
     * Deletes right neighbour.
     */
    protected void deleteRightNeighbor(Layout subtree) {
        int index = children.indexOf(subtree);
        if (index < children.size() - 1)
            children.get(index + 1).deleteLeftLeaf(); // Called when we can make child of current node the active one.
        else
            parent.deleteRightNeighbor(this); //called when we need to backtrack one level up
    }

    /**
     * Checks if parameter layout is of type {@link LayoutNode}, and if the children match
     * the layout of this node. All leaves must have a FileBuffer containing the same path.
     * ! Assumes the children are always put in the same sequence upon creation of layout
     */

    protected void deleteLeftLeaf() {
        children.get(0).deleteLeftLeaf();
    }

    protected void delete(Layout l) {
        children.remove(l);
        if (this.children.size() == 1) {
            if (this.parent != null) {
                this.parent.children.set(this.parent.children.indexOf(this), this.children.get(0));
                this.children.get(0).setParent(this.parent);
            }
        }
    }

    protected void makeLeftmostLeafActive() {
        this.containsActiveView = true;
        children.get(0).makeLeftmostLeafActive();
    }

    protected void makeRightmostLeafActive() {
        this.containsActiveView = true;
        children.get(children.size() - 1).makeRightmostLeafActive();
    }

    protected LayoutLeaf getLeftLeaf() {
        return children.get(0).getLeftLeaf();
    }

    //makes the leftmost leaf that is to the right of the given subtree active
    //Requires passed layout to be a child of this.
    protected void makeRightNeighbourActive(Layout layout) {
        int index = children.indexOf(layout);
        if (index < children.size() - 1) {
            children.get(index + 1).makeLeftmostLeafActive(); // Called when we can make child of current node the active one.
        } else {
            if (parent != null) {
                containsActiveView = false;
                parent.makeRightNeighbourActive(this); //called when we need to backtrack one level up
            } else {
                makeRightmostLeafActive();
            }
        }
    }

    //see makeRightNeighbourActive
    protected void makeLeftNeighbourActive(Layout layout) {
        int index = children.indexOf(layout);
        if (index > 0) {
            children.get(index - 1).makeRightmostLeafActive(); // Called when we can make child of current node the active one.
        } else {
            if (parent != null) {
                containsActiveView = false;
                parent.makeLeftNeighbourActive(this); //called when we need to backtrack one level up
            } else {
                makeLeftmostLeafActive();
            }
        }
    }

    //replaces the given child with a new layoutnode with the child and its sibling rotated
    //Returns the new root layout of the rotated node
    protected Layout rotateWithRightSibling(ROT_DIRECTION rotdir, Layout child) {
        LayoutLeaf newSibling = this.getRightNeighbor(child);
        LayoutNode newChild = null;

        if (newSibling == null) {
            System.out.print((char) 7);
        } else if (newSibling.parent != this) {
            this.deleteRightNeighbor(child);
            if (rotdir == ROT_DIRECTION.COUNTERCLOCKWISE) {
                children.add(children.indexOf(child), newSibling);
                newSibling.setParent(this);
            } else {
                children.add(children.indexOf(child) + 1, newSibling);
                newSibling.setParent(this);
            }
            this.fixChangedTreeFromNewNode();
        } else {
            newChild = getNewMergedRotatedChild(rotdir, child, newSibling);
            this.replaceAndSetParent(child, newChild);
            this.deleteRightNeighbor(newChild);
            newChild.fixChangedTreeFromNewNode();
        }

        if (newChild == null) {
            return child.getRootLayoutUncloned();
        } else {
            return newChild.getRootLayoutUncloned();
        }
    }

    //Returns a layoutnode containing a child and a new specified sibling
    protected abstract LayoutNode getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child, LayoutLeaf newSibling);

    private void replaceAndSetParent(Layout toReplace, Layout newLayout) {
        int index = children.indexOf(toReplace);
        children.set(index, newLayout);
        newLayout.setParent(this);
    }

    //Bottom up fixing of tree structure with unary and binary constraints.
    //Given that the children Layouts are in a consistent state with this
    //(= could exist as root on its own and are oriented differently)
    //make the current node and its parent (may be null) consistent with each other.
    //Rules: A parent's child LayoutNodes aren't empty, don't contain just 1 element
    // and are oriented differently.
    private void fixChangedTreeFromNewNode() {
        if (parent != null) {
            //absorb this and siblings if orientations of parent
            if (this.getOrientation() == parent.getOrientation()) {
                for (Layout l : this.children) {
                    l.setParent(parent);
                }
                parent.children.addAll(parent.children.indexOf(this), this.children);
                parent.children.remove(this);
            }
            if (parent.children.size() == 1) {
                if (parent.parent != null) {
                    //As of 13/03 there is no coverage on this part. This is because due to the way delete is called, it takes care of this already. However, if the assignment and order of operations were to change, this would no longer be the case.
                    this.parent.parent.children.set(this.parent.parent.children.indexOf(parent), this);
                    this.parent = parent.parent;
                    this.fixChangedTreeFromNewNode();
                } else {
                    this.parent = null;
                }
            }
        }
    }

    @Override
    public abstract LayoutNode clone();

    /**
     * Enumerator that represents whether the Layouts inside this Layout
     * will be stacked (VERTICAL) or side-by-side (HORIZONTAL)
     */
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}
