#JavaSCAD

#####3D model generator library

After we had been using [OpenSCAD](http://www.openscad.org/) for months and learnt its limitations we decided we should write something which helps creating complex models. Our obvious choice was Java, because both of us are expert Java developers and the cross-platform support for Java is ideal to generate code for the equally cross-platform OpenSCAD.

This led to the creation of JavaSCAD, a simple framework in Java. The basic concept is to represent the OpenSCAD primitive objects with a Java objects and build more complex ones from them. There is still ongoing development around JavaSCAD - for one thing it should be improved and addded more functionality and the other part is to include the OpenSCAD's 2D capabilities (the 3D features are already implemented).

The official website of the library can be found [here](http://www.printingin3d.eu/javascad), where you can find tutorials and example codes too.

##Building the project

The project is a standard Maven project now, which means you can build it with the standard ```mvn clean install``` command.
