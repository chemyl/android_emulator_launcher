#### Task description:
* It is necessary to develop a utility program that is capable of creating an Android emulator with the specified parameters.
* The program must be implemented with graphical interface.
* The utility must be able to create and install N emulators.
* 
#### Example of how the program works: 
* Create 2 emulators with Android API version 30, x86_64 bit depth.
* Have an image of a Pixel 3a phone.
* After creating and raising emulators, it returns a list of device addresses in the form: “127.0.0.1:5554, 127.0.0.1:5556”.

#### Requirements:
* If the required emulator image is missing, the program independently **downloads** it from the Internet
* The device image must have **Google Play support**
* The program is capable of raising N emulators, N is specified as a **parameter**
* The program should allow you to create emulators with version 22 < API < 33
* Emulators screen should have **screen -off** mode
* Output of the program: N running emulators, list of installed devices
* Development language **java 8**.

#### Used Packages:
_SDKMANAGER_
sdkmanager --list
sdkmanager --install system-images;android-30;google_apis_playstore;x86_64

_AVDMANGER_
avdmanager list devices	==> All possible devices with cyrrent Android SDK
avdmanager list target 	==> Available Android platforms (downloaded and ready to use)
avdmanager list avd		==> Active virtual devices
avdmanager create avd --name MyPixel6Emulator --package "system-images;android-30;google_apis_playstore;x86_64" --device "Nexus_6.1" --tag "google_apis_playstore" --abi "x86_64" --force
avdmanager delete avd --name Nexus_6.1

_EMULATOR_
emulator -list-avds
emulator -avd Nexus_5X_API_23 -netdelay none -netspeed full
    "emulator": The executable command for launching the emulator.
    "-avd", emulatorName: Specifies the AVD name or ID to launch. Replace emulatorName with the actual name or ID of the AVD you want to launch.
    "-port", port: Specifies the port number to use for the emulator's communication. Replace port with the desired port number.
    "-no-audio": Disables audio support in the emulator.
    "-no-boot-anim": Disables the boot animation when starting the emulator.
    "-no-window": Runs the emulator without displaying its window. This is useful for running the emulator headlessly.
    "-accel", "off": Disables hardware acceleration for the emulator.
    
_ADB_
adb exec-out screencap -p > screenshot.png
adb -s emulator-5554 reboot


![launcher window](https://github.com/chemyl/android_emulator_launcher/raw/master/src/main/resources/screen1.png)
