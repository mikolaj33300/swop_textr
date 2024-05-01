# all variables, and various go here, the makefile itself just contains the build scripts

#
# diags:
#
OFORMAT = svg
DOT := $(wildcard ./diagrams/*.dot) 
DOT += $(wildcard ./diagrams/**/*.dot)
DIAGS = $(patsubst %.dot,%.$(OFORMAT),$(DOT))
termios = termios/_build/main/io.github.btj.termios.jar

#
# src:
# 
JAR= termios/_build/main/io.github.btj.termios.jar /usr/share/junit-5/lib/junit-jupiter-api.jar /usr/share/apiguardian-api/lib/apiguardian-api.jar
#$(wildcard /usr/share/junit-5/lib/*.jar)

rwildcard=$(wildcard $1$2) $(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2))
SRC = $(call rwildcard,src/,*.java)

#TEST = $(call rwildcard,tests/,*.java)
TEST = tests/files/FileBufferTest.java ./tests/files/BufferCursorContextTest.java ./tests/files/InsertionPointTest.java ./tests/files/FileAnalyserUtilTest.java ./tests/files/FileHolderTest.java ./tests/controller/RearrangeLayoutTest.java ./tests/controller/CloseBufferTest.java ./tests/layouttree/HorizontalLayoutNodeTest.java  ./tests/ui/ViewTest.java

#TODO
#./tests/controller/SaveBufferTest.java
#./tests/controller/EditBufferTest.java
#./tests/controller/ControllerTest.java
#./tests/controller/InspectBufferTest.java
#./tests/controller/LaunchTextrTest.java
#./tests/layouttree/VerticalLayoutNodeTest.java
#./tests/layouttree/LayoutNodeTest.java
#./tests/layouttree/LayoutTest.java
#./tests/ui/FileBufferViewTest.java
#./tests/layouttree/LayoutLeafTest.java

OBJ := $(patsubst %.java,%.class,$(SRC))
OBJ := $(patsubst src/%,build/%,$(OBJ))
BUILD_DIR = ./build

#
# package
#
EXTS := java pdf png
TOPACK := $(foreach EXT, $(EXTS), $(shell find . -type d \( -path ./termios -o -path ./build -o -path ./group07 \) -prune -o -name '*.$(EXT)' -print))
TOPACK += ./textr.jar
