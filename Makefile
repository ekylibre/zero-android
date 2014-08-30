
all: debug install

clean:
	ant clean
	find . -type f -name '*~' -delete
	# find ./ -name '*~' | xargs rm

debug:
	ant debug

release:
	ant release

install:
	adb install -r bin/Rei-debug.apk
