include config.mk

all: options uml $(DIAGS) $(termios) build

options:
	@echo OFORMAT: $(OFORMAT)

uml/%.$(OFORMAT): uml/%.dot
	dot -T$(OFORMAT) $< -o $@
docs: uml $(DIAGS)

build: textr.java
textr.java:
	javac $(SRC)
	jar cvf $(OBJ)

clean:
	rm -r \
		$(DIAGS) \
		termios/_build/
$(termios):
	$(MAKE) -C termios -B jar
