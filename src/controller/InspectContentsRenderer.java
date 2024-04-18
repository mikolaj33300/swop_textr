package controller;

import ui.View;

import java.io.IOException;
import java.util.ArrayList;

/*It uses the views as an external thing of the controller. This is so that it won't have to know
anything about possible operations on the views. Another alternative is to just make the views into a public array,
but the code and constructor are simpler this way.*/
public class InspectContentsRenderer implements ViewRenderer{
    private ControllerFacade controllerFacadeToRender;


    @Override
    public void paintView() throws IOException {
        if(controllerFacadeToRender.getContentsChangedSinceLastRender()){
            controllerFacadeToRender.setContentsChangedSinceRender(false);
            for(Window w: controllerFacadeToRender.getWindows()){
                w.handler.setContentsChangedSinceLastRenderFalse();
            }
            controllerFacadeToRender.getTermiosTerminalAdapter().clearScreen();
            for(Window w:controllerFacadeToRender.getWindows()){
                w.view.render(controllerFacadeToRender.getWindows().get(controllerFacadeToRender.getActive()).view.hashCode());
            }
            controllerFacadeToRender.getWindows().get(controllerFacadeToRender.getActive()).view.renderCursor();
        }
    }

    public InspectContentsRenderer(ControllerFacade conf){
        this.controllerFacadeToRender = conf;
    }
}
