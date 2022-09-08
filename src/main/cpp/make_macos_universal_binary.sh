#!/bin/sh

X64_MAKEFILE="makefiles/makefile.defs.macos.x64"
ARM64_MAKEFILE="makefiles/makefile.defs.macos.arm64"
MAKEFILE_DEFS="makefile.defs"

X64_TARGET="x86_64-apple-macos10.12"
ARM64_TARGET="arm64-apple-macos11"
LIBNAME="desktopicons"

TARGET_FOLDER="../../../target/cpp"
X64_LIB="${TARGET_FOLDER}/${X64_TARGET}/objects/lib${LIBNAME}-${X64_TARGET}.jnilib"
ARM64_LIB="${TARGET_FOLDER}/${ARM64_TARGET}/objects/lib${LIBNAME}-${ARM64_TARGET}.jnilib"
UNIVERSAL_TARGET_FOLDER="${TARGET_FOLDER}/universal"
UNIVERSAL_TARGET="${UNIVERSAL_TARGET_FOLDER}/lib${LIBNAME}.jnilib"
INSTALL_FOLDER="../../../src/main/resources/META-INF/lib/macos"

# compile
echo "Compile x86_64 target"
ln -sf "${X64_MAKEFILE}" "${MAKEFILE_DEFS}"
make clean
make

echo "Compile arm64 target"
ln -sf "${ARM64_MAKEFILE}" "${MAKEFILE_DEFS}"
make clean
make

# make universal binary
echo "Make universal binary"

mkdir -p "${UNIVERSAL_TARGET_FOLDER}"
lipo -create -output "${UNIVERSAL_TARGET}" "${X64_LIB}" "${ARM64_LIB}"

# install (optional)
if [ "-install" = "$1" ]; then
  echo "Install universal binary to resources folder"
  cp "${UNIVERSAL_TARGET}" "${INSTALL_FOLDER}"
fi
