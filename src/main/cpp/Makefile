include makefile.defs

all:	$(BINDIR) $(SOURCES) $(LIBRARY)

$(LIBRARY):	$(OBJECTS)
	$(LD) -o $@ $(LDFLAGS) $(OBJECTS) $(LIBS)

$(BINDIR):
	mkdir -p $(BINDIR)

$(BINDIR)/%.o:	%.c
	$(CC) $(CFLAGS) $< -o $@

$(BINDIR)/%.o:	%.cpp
	$(CXX) $(CXXFLAGS) $< -o $@

clean:
	rm -rf $(BINDIR)

install:
	mkdir -p $(INSTALL_PATH)
	cp -f $(LIBRARY) $(INSTALL_PATH)

