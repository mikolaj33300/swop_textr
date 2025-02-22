include config.mk

all: options diagrams $(DIAGS) $(termios) build textr.jar

options:
	@echo OFORMAT: $(OFORMAT)

diagrams/%.$(OFORMAT): diagrams/%.dot
	dot -T$(OFORMAT) $< -o $@
diagrams/%.$(OFORMAT): diagrams/%.sd
	java -jar ~/Documents/tools/java/sdedit-4.2.1.jar -o $@ -t$(OFORMAT) $<

docs: diagrams $(DIAGS)

build: $(OBJ)

jar: $(JAR)
	mkdir -p $(BUILD_DIR)
	$(foreach jar, $(JAR), jar xvf $(jar);)
	cp -vr ./io ./build/io
	cp -vr ./org ./build/org

build/%.class: src/%.java
	
textr.jar: $(SRC)
	mkdir -p $(BUILD_DIR)
	@javac -Xlint:unchecked -Xdiags:verbose -cp ./build:./:./termios/_build/main/io.github.btj.termios.jar -d ./build $^
	jar cvfm textr.jar ./Manifest -C $(BUILD_DIR) .

test:
	@javac -Xlint:unchecked -Xdiags:verbose -Xmaxerrs 5 -Xmaxwarns 5 -cp /usr/share/junit-5/lib/junit-jupiter-api.jar:/usr/share/apiguardian-api/lib/apiguardian-api.jar:$(BUILD_DIR):termios/_build/main/io.github.btj.termios.jar -d $(BUILD_DIR) $(SRC) $(TEST)
	junit-platform-console execute --disable-banner --fail-if-no-tests --class-path ./build/ --scan-classpath ./build/

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

run:
	java -cp ./build:./:./termios/_build/main/io.github.btj.termios.jar -jar ./textr.jar ./long.txt ./config.mk || true
	stty 500:5:bf:8a3b:3:1c:7f:15:4:0:1:0:11:13:1a:0:12:f:17:16:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0
debug:
	java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n -jar textr.jar test || true
	stty 500:5:bf:8a3b:3:1c:7f:15:4:0:1:0:11:13:1a:0:12:f:17:16:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0
