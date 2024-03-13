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
JAR=/usr/share/junit-5/lib/junit-platform-commons.jar termios/_build/main/io.github.btj.termios.jar

SRC = ./src/files/FileBuffer.java ./src/files/FileAnalyser.java ./src/files/Statusbar.java ./src/files/FileHolder.java ./src/layouttree/Layout.java ./src/layouttree/HorizontalLayoutNode.java ./src/layouttree/LayoutNode.java ./src/layouttree/VerticalLayoutNode.java ./src/layouttree/LayoutLeaf.java ./src/core/Controller.java ./src/util/Debug.java

TEST = ./tests/files/FileBufferTest.java ./tests/files/FileHolderTest.java ./tests/layouttree/LayoutLeafTest.java ./tests/layouttree/LayoutNodeTest.java ./tests/layouttree/LayoutTest.java ./tests/core/SaveBufferTest.java ./tests/core/EditBufferTest.java ./tests/core/ControllerTest.java ./tests/core/RearrangeLayoutTest.java ./tests/core/InspectBufferTest.java ./tests/core/LaunchTextrTest.java ./tests/core/CloseBufferTest.java
OBJ = $(patsubst %.java,%.class,$(SRC))
BUILD_DIR = ./build
