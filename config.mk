# all variables, and various go here, the makefile itself just contains the build scripts

#
# diags:
#
OFORMAT = pdf
DOT := $(wildcard ./diagrams/*.dot) 
DOT += $(wildcard ./diagrams/**/*.dot)
DIAGS = $(patsubst %.dot,%.$(OFORMAT),$(DOT))
termios = termios/_build/main/io.github.btj.termios.jar

#
# src:
# 
JAR= termios/_build/main/io.github.btj.termios.jar /usr/share/junit-5/lib/junit-jupiter-api.jar /usr/share/apiguardian-api/lib/apiguardian-api.jar
#$(wildcard /usr/share/junit-5/lib/*.jar)

SRC = src/controller/TextR.java src/controller/UseCaseController.java src/controller/controllerFacade.java src/controller/InspectContentsController.java src/controller/FileErrorPopupController.java src/controller/FailedSavePopupController.java src/controller/DirtyClosePromptController.java src/util/Debug.java src/files/FileHolder.java src/files/FileAnalyserUtil.java src/files/FileBuffer.java src/observer/FileBufferListener.java src/ui/InsertionPoint.java src/ui/View.java src/ui/FileBufferView.java src/ui/UserPopupBox.java src/ui/Render.java src/inputhandler/InputHandler.java src/inputhandler/FileBufferInput.java src/layouttree/HorizontalLayoutNode.java src/layouttree/DIRECTION.java src/layouttree/LayoutLeaf.java src/layouttree/ROT_DIRECTION.java src/layouttree/Layout.java src/layouttree/VerticalLayoutNode.java src/layouttree/LayoutNode.java
TEST = $(wildcard ./tests/**/*.java)

OBJ := $(patsubst %.java,%.class,$(SRC))
OBJ := $(patsubst src/%,build/%,$(OBJ))
BUILD_DIR = ./build

#
# package
#
EXTS := java pdf png
TOPACK := $(foreach EXT, $(EXTS), $(shell find . -type d \( -path ./termios -o -path ./build -o -path ./group07 \) -prune -o -name '*.$(EXT)' -print))
TOPACK += ./textr.jar
