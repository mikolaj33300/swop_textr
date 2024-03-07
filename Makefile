include config.mk

all: options uml $(DIAGS) $(termios) build

options:
	@echo OFORMAT: $(OFORMAT)

uml/%.$(OFORMAT): uml/%.dot
	dot -T$(OFORMAT) $< -o $@
docs: uml $(DIAGS)

build: textr.jar

textr.jar:
	jar -xf ./termios/_build/main/io.github.btj.termios.jar
	javac $(SRC)
	jar cvf textr.jar $(OBJ) ./libio_github_btj_termios.so

clean:
	rm -r \
		$(DIAGS) \
		termios/_build/ \
		$(OBJ) \
		io \
		libio_github_btj_termios

$(termios):
	$(MAKE) -C termios -B jar
