# JavaAutoBuilder

Team 238's _new and improved_ autonomous mode builder (name in progress)

## Goal
we already had a perfectly serviceable program to work with that had been getting small updates every now and again.
Unfortunately, it had some minor annoyances with it, and the students who worked on it were long gone. yes, we could have learned c# and added to the program, but I decided that wouldn't teach me what i wanted to know, so i completely rewrote the program from the ground up in Java and Kotlin using the in-development java-gtk language bindings, allowing theoretical cross-platform support for linux, windows, and mac. And it was fun.

## what does it do?
the program makes a json file in your deploy directory that holds all of your autonomous modes. each entry is a list of commands, their parameters, and what (if any) mode of parallel it uses. optionally, if you have wpilib installed, and you wrote your code in java, you can use **THE POWER OF REFLECTION** to type check the parameters of the commands and change any entry fields that have an enum associated with them in your robot code to a drop-down of all of its possible values. additionally, if there is something that cannot be known at compile time, such as all of the possible trajectories in your pathplanner directory, you can add a handy-dandy .kts file into the plugins directory of the app to make your own custom source for dropdowns!

only java-based robot projects are supported at the moment. if you are savvy with c++ and kotlin, feel free to develop the c++ library as a part of the [AutoBuilderLib](https://github.com/me6262/AutoBuilderLib) project that goes with this.

## Setting up your Robot Project
the robot side of things is handled by the [AutoBuilderLib](https://github.com/me6262/AutoBuilderLib) vendordep conveniently created for this very purpose, so head over there to learn more


## Building the program on your own
in case you would like to contribute to the project, or you want to test the main branch, this is the part of the guide for you.

### Windows

The program requires gtk-4 and libadwaita-1 as build dependencies

the easiest method to get them without building gtk yourself is using msys2.


1. install msys2 from their website, or from chocolatey
2. open MinGW64.exe and run this:

```bash
pacman -S mingw-w64-x86_64-gtk4 mingw-w64--x86_64-adwaita-icon-theme mingw-w64-x86_64-libadwaita
```

3. then add the `bin`, `lib` and `include` directories of the msys2 install directory to your windows path
4. this project should install all required gradle dependencies, although you may need to install jdk17 using your preferred method
5. optionally, install wpilib, vscode is not required, only the libraries.
## Build on Linux distros
#### NOTE: until flatpak support is added, only distros providing Libadwaita 44 and above are usable

### OpenSUSE Tumbleweed
1. download the dependencies
```bash
sudo zypper install libgtk-4-1 libadwaita-1-0 java-17-openjdk
```
2. optionally, install wpilib, vscode is not required, only the libraries.
### Ubuntu 23.04
```bash
sudo apt install libgtk-4-1 libadwaita-1-0 openjdk-17-jdk
```
2. optionally, install wpilib, vscode is not required, only the libraries.

### Fedora 38
coming soon
## Set up development on MacOS
coming soon
