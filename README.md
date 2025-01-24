### Android Emulator launcher

![java Version](https://img.shields.io/badge/java-21%20-green)
![java Version](https://img.shields.io/badge/java_swing%20-orange)
![lombok Version](https://img.shields.io/badge/Lombok-1.18.30%20-orange)
![Build Status](https://github.com/chemyl/android_emulator_launcher/actions/workflows/maven.yml/badge.svg)

* If the required emulator image is missing, the program automatically `downloads` it from the Internet
* The device image must have `Google Play support`
* The program is capable of running `N` emulators
* The program must allow creating emulators with API versions from `22` to `33`
* The emulator screen must have the `screen -off` mode
* Program output: N running emulators, list of installed devices

#### Used Packages:
`SDKMANAGER`
`AVDMANGER`
`EMULATOR`
`ADB`

### Use Case:
1. Update path to avdmanager, sdkmanager, emulator
2. Run Application

![launcher window](https://github.com/chemyl/android_emulator_launcher/raw/master/src/main/resources/screen1.png)
