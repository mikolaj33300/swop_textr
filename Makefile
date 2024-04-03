include config.mk

all: options diagrams $(DIAGS) $(termios) build

options:
	@echo OFORMAT: $(OFORMAT)

diagrams/%.$(OFORMAT): diagrams/%.dot
	dot -T$(OFORMAT) $< -o $@
docs: diagrams $(DIAGS)

build: textr.jar

jar: $(JAR)
	mkdir -p $(BUILD_DIR)
	$(foreach jar, $(JAR), jar xvf $(jar);)
	cp -vr ./io ./build/io
	cp -vr ./org ./build/org

textr.jar:
	mkdir -p $(BUILD_DIR)
	@javac -cp termios/_build/main/io.github.btj.termios.jar -d $(BUILD_DIR) $(SRC)
	jar cvfm textr.jar ./Manifest -C $(BUILD_DIR) . ./libio_github_btj_termios.so
test:
	@javac -Xlint:unchecked -Xmaxwarns 200 -cp /usr/share/junit-5/lib/junit-jupiter-api.jar:$(BUILD_DIR) -d $(BUILD_DIR) $(SRC) $(TEST)
	junit-platform-console --class-path ./build/ --scan-classpath ./build/

clean:
	rm -r \
		$(DIAGS) \
		termios/_build/ \
		org/ \
		$(OBJ) \
		io/ \
		libio_github_btj_termios.so \
		build/ \
		*.jar

$(termios):
	$(MAKE) -C termios -B jar

dist: all group07.zip
group07.zip:
	 mkdir /tmp/group07
	 $(foreach DFILE, $(TOPACK), echo $(DFILE) | cpio -p -d -v  /tmp/group07/;)
	 cd /tmp; zip -r -b /tmp/ ./group07.zip ./group07/
	 mv /tmp/group07.zip ./

