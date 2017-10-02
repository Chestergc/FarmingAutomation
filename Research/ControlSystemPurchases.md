# Control Systems Research

This will be a in depth review of the computational systems that can be used for this project, including the prices and selling points of said systems.
__All Prices in BRL__

* __Raspberry Pi (All Models)__

The Raspberry Pi system was originally created as a computational resource for low income scenarios, the board cost was always an attractive factor, providing enough resources for everyday jobs and even some small prototyping runs, as such it would be the ideal candidate for a prototype, although for a production run it would stop being cost effective in a short amount of time.

The main use case for the board, as of now, is for small prototypes in simple scenarios where AI or a graphical control system would be applicable, given the system is a fully fledged (although low powered) computer.

Being so the programmer is able to choose between any familiar programming languages, any language that runs on a linux system can be used.

The price of the board as of now, is shown at the table below.

|Store|System Price|Posting|Total|
|:-------------|:-------------|:------|:------|
|Baú da Eletrônica|237,92|34,97|272,89|
|Mercado Livre|149,00|0,00|149,00|
|4Hobby|214,99|16,10|231,09|


* __Arduino Uno (All Manufacturers)__

The Arduino board is a simple microcontroller prototyping tool, compared to the Raspberry Pi it's underpowered, The board is useful for certain use cases, wherein the end project the system will be manufactured using a custom made microcontroller board. The system uses a Atmel microcontroller for most of it's computation, although it has a second computational unit handling USB/Serial conversion in the input and output of the system.

It's a well known board for prototyping and general home automation projects, for it's ease of use and simple IDE. The system is programmed in a revision of the C programming language, which makes it easily approachable without much complication by any person into robotics.

The uno system has a few downsides, mostly tailored for small prototypes and simple automation cases, it has few I/O ports and virtually no protection to power surges or overloads, making the system quite flimsy, which is overshadowed by the price of the board. Most of the time the failure states of the system are easier to deal with by replacing the whole board, due to it's low cost.

There are no simple solutions to graphical interfaces on the system.

There is not enough memory for AI in the system.

For this project this system is inviable, unless used in conjunction with other systems on this list, as a buffer and actuator for outputs.

The price of the board as of now, is shown at the table below.

|Store|System Price|Posting|Total|
|:-------------|:-------------|:------|:------|
|Baú da Eletrônica|127,42|35,89|163,31|
|Mercado Livre|40,00|41,00|81,00|
|4Hobby|25,90|16,10|42,00|


* __Parallella__

The Parallella Board has a main focus on parallel computing, as the name implies, the main board has 18 processors and can be easily chained for increased computational power, creating a prototyping tool for supercomputers instead of simple controllers and actuators.

Although the system is powerful and infinitely scalable the cost of the board makes it viable only on very specific scenarios and disadvantages to the board make it inviable for any projects that don't need machine learning or other high intensity computational resources.

The price of the board as of now, is shown at the table below.

|Store|System Price|Posting|Total|
|:-------------|:-------------|:------|:------|
|Baú da Eletrônica|NotFound|NotFound|NotFound|
|Mercado Livre|905,00|858,16|1763,16|
|4Hobby|NotFound|NotFound|NotFound|

* __Beagle Board__

The Beagle boards are the most competitive direct competitor to the raspberry pi computers, and as such they offer some functionality that is comparable.

The main difference encountered by the research was that the Beagle Board has a onboard flash storage, instead of an SD card, which makes it a bit complicated to prototype for, as you need to have it unplugged for the system to be able to alter the program, whereas on the Raspberry you can just plug in a second SD card.

The price of the board as of now, is shown at the table below.

|Store|System Price|Posting|Total|
|:-------------|:-------------|:------|:------|
|Baú da Eletrônica|331,42|34,97|366,39|
|Mercado Livre|NotFound|NotFound|NotFound|
|4Hobby|NotFound|NotFound|NotFound|

* __Intel NUC__

The NUC stands out in this list by not being a board, it is a fully fledged computer, running on intel's 64 bit platform, it doesn't have the usual components of a board, such as pinouts, but it overpowers any of the boards on this list. For big projects this would be viable, for our project it is overkill.

The price of the computer as of now, is shown at the table below.

|Store|System Price|Posting|Total|
|:-------------|:-------------|:------|:------|
|Baú da Eletrônica|NotFound|NotFound|NotFound|
|Mercado Livre|1679,00|0,00|1679,00|
|4Hobby|NotFound|NotFound|NotFound|

### Results:

Based on the research, the prices and the availability of the components the group has opted to use the Raspberry Pi board, given the board is easy to find, has a name on the market, and a thriving community of users that have made tutorials for pretty much every aspect of the board.

The project will be built on top of the Raspberry Pi architecture, running the native operating system, Raspbian.
