inherit gettext

SUMMARY = "Downloads the Snapdragon Wear 2100/3100 /usr/libexec/hal-droid and /usr/include/android folders and installs them for libhybris"
LICENSE = "CLOSED"

SRC_URI = "https://dl.dropboxusercontent.com/s/yl8tezw6e2d661o/hybris-o-msm8909.tar.gz;name=hybris \
    https://dl.dropboxusercontent.com/s/vjqlcekapca3c60/system-ray-pie-preview.tar.gz;name=system \
    file://60-i2c.rules \
"
SRC_URI[hybris.md5sum] = "7891147518b1c1a3071af6173c9fd38f"
SRC_URI[hybris.sha256sum] = "61c59dbcb894e693c0fd092d690efdb5fa63fd5784be63e0e3749f25af800ce8"
SRC_URI[system.md5sum] = "ef660b4b747cdace3033d65efb80864f"
SRC_URI[system.sha256sum] = "64e00a450c19eb7af0518f3676f87a4e81d3f3e106f09f2879d43542b32691aa"
PV = "oreo"

PACKAGE_ARCH = "${MACHINE_ARCH}"
INHIBIT_PACKAGE_STRIP = "1"
COMPATIBLE_MACHINE = "ray"
INSANE_SKIP:${PN} = "already-stripped"
S = "${WORKDIR}"
B = "${S}"

PROVIDES += "virtual/android-system-image"
PROVIDES += "virtual/android-headers"

do_install() {
    # Allow pulseaudio to control I2C devices, for speaker.
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/60-i2c.rules ${D}${sysconfdir}/udev/rules.d/

    install -d ${D}/usr/
    cp -r usr/* ${D}/usr/

    install -d ${D}${includedir}/android
    cp -r include/* ${D}${includedir}/android/

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${D}${includedir}/android/android-headers.pc ${D}${libdir}/pkgconfig
    rm ${D}${includedir}/android/android-headers.pc

    install -d ${D}/system/
    cp -r system/* ${D}/system/

    cd ${D}
    ln -s system/vendor vendor
}

# FIXME: QA Issue: Architecture did not match (40 to 164) on /work/dory-oe-linux-gnueabi/android/lollipop-r0/packages-split/android-system/system/vendor/firmware/adsp.b00 [arch]
do_package_qa() {
}

PACKAGES =+ "android-system android-headers"
FILES:android-system = "/usr ${sysconfdir}/udev /system /vendor"
FILES:android-headers = "${libdir}/pkgconfig ${includedir}/android"
EXCLUDE_FROM_SHLIBS = "1"
