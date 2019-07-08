#Runs specific test. Pass in test name.
set -e
./gradlew linkDebugTestNativeCommon
./koin-core/build/bin/nativeCommon/debugTest/test.kexe --ktest_regex_filter=.*$1.*