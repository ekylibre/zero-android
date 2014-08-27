
all: debug install

clean:
	ant clean
	find ./ -name '*~' | xargs rm

debug:
	ant debug

install:
	adb install -r bin/Rei-debug.apk
