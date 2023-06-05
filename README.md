# JavaAutoBuilder

Team 238's _new and improved_ autonomous mode builder (name in progress)

## Goal
we already had a perfectly serviceable program to work with that had been getting small updates every now and again.
Unfortunately, it had some minor annoyances with it, and the students who worked on it were long gone. yes, we could have learned c# and added to the program, but I decided that wouldn't teach me what i wanted to know, so i completely rewrote the program from the ground up in Java and Kotlin using the in-development java-gtk language bindings, allowing theoretical cross-platform support for linux, windows, and mac. And it was fun.

## what does it do?
the program makes a json file in your deploy directory that holds all of your autonomous modes. each entry is a list of commands, their parameters, and what (if any) mode of parallel it uses. optionally, if you have wpilib installed, and you wrote your code in java, you can use **THE POWER OF REFLECTION** to type check the parameters of the commands and change any entry fields that have an enum associated with them in your robot code to a drop-down of all of its possible values. additionally, if there is something that cannot be known at compile time, such as all of the possible trajectories in your pathplanner directory, you can add a handy-dandy .kts file into the plugins directory of the app to make your own custom source for dropdowns!
