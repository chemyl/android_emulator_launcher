#### Task description:
* It is necessary to develop a utility program that is capable of creating an Android emulator with the specified parameters.
* The program must be implemented with graphical interface.
* The utility must be able to create and install N emulators.
* 
* **Example of how the program works**: 
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

![launcher window](https://github.com/chemyl/android_emulator_launcher/raw/master/src/main/resources/screen1.png)
