package controller;

import java.io.IOException;

public class ViewManager {
    private ViewRenderer activeViewRenderer;
    private ControllerFacade controllerFacade;

    public void setActiveViewRenderer(ViewRenderer activeViewRenderer) {
        this.activeViewRenderer = activeViewRenderer;
    }

    public ViewManager(ControllerFacade controllerFacade){
        this.activeViewRenderer = new InspectContentsRenderer(controllerFacade);
        this.controllerFacade = controllerFacade;
    }

    public void paintScreen() throws IOException {
        this.activeViewRenderer.paintView();
    }

    public void handleFileSaveError() {
        this.activeViewRenderer = new FileErrorPopupRenderer(controllerFacade);
    }

    public void handleClosePrompt(){
        this.activeViewRenderer = new DirtyClosePromptRenderer(controllerFacade);
    }

    public void handleFileErrorPopup(){
        this.activeViewRenderer = new FileErrorPopupRenderer(controllerFacade);
    }

    public void displayNormalContents(){
        this.activeViewRenderer = new InspectContentsRenderer(controllerFacade);
    }
}
