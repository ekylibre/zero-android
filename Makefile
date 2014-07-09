
all: debug install

debug:
	ant debug

install:
	adb install -r bin/Rei-debug.apk
