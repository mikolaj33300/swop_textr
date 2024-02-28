include config.mk

all: options uml $(DIAGS) $(termios)

options:
	@echo OFORMAT: $(OFORMAT)

uml/%.$(OFORMAT): uml/%.dot
	dot -T$(OFORMAT) $< -o $@
docs: uml $(DIAGS)

clean:
	rm -r \
		$(DIAGS) \
		termios/_build/
$(termios):
	$(MAKE) -C termios -B jar
