# all variables, and various go here, the makefile itself just contains the build scripts

#
# diags:
#
OFORMAT = pdf
DOT := $(wildcard ./uml/*.dot)
DIAGS=$(patsubst %.dot,%.$(OFORMAT),$(DOT))
