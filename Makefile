include config.mk

all: options diag termios

options:
	@echo dotfiles: $(DOT)
	@echo outformat: $(OFORMAT)
	@echo $(DIAGS)

uml/%.$(OFORMAT): uml/%.dot
	dot -T$(OFORMAT) $< -o $@
diag: uml $(DIAGS)

clean:
	rum -v \
		$(DIAGS)
termios:
	$(MAKE) -C ./termios
