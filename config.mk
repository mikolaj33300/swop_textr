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

SRC = $(wildcard ./src/**/*.java)
TEST = $(wildcard ./tests/**/*.java)

OBJ = $(patsubst %.java,%.class,$(SRC))
BUILD_DIR = ./build

#
# package
#
EXTS := java pdf png
TOPACK := $(foreach EXT, $(EXTS), $(shell find . -type d \( -path ./termios -o -path ./build -o -path ./group07 \) -prune -o -name '*.$(EXT)' -print))
TOPACK += ./textr.jar
