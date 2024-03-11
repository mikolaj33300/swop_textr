# all variables, and various go here, the makefile itself just contains the build scripts

#
# diags:
#
OFORMAT = pdf
DOT := $(wildcard ./uml/*.dot)
DIAGS = $(patsubst %.dot,%.$(OFORMAT),$(DOT))
termios = termios/_build/main/io.github.btj.termios.jar

#
# src:
# 
SRC = ./src/files/FileHolder.java ./src/files/FileBuffer.java ./src/core/Controller.java ./src/layouttree/Layout.java ./src/layouttree/LayoutLeaf.java ./src/layouttree/LayoutNode.java
OBJ = $(patsubst %.java,%.class,$(SRC))
