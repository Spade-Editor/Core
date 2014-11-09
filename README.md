Paint.JAVA Core
===============
Official core plugin containing basic editing tools for Paint.JAVA

## Developing

Before you can work on the Core plugin, you must first have Paint.JAVA set up for development.

#### Setting up Paint.JAVA for development
There are two options:
1. If you are also developing the base framework, or are developing the plugin for an
unreleased version, follow the instructions
[here](https://github.com/PaintDotJava/Paint.JAVA#developing) to get Paint.JAVA setup
in your IDE.

2. If you are just interested in developing this plugin, and for the current release of
Paint.JAVA*, download the latest `PaintDotJava-Minimal.jar` from the
[release page](https://github.com/PaintDotJava/Paint.JAVA/releases) and use that for
step 4 below.

\* It's important to note that if the master build of Core has a minimum paint version
requirement higher than the latest release, you need to follow option 1, or else your
contributions may not be able to be merged back into master due to being out of date.

The minimum Paint.JAVA version of Core can be found in the
[`plugin.info`](https://github.com/PaintDotJava/Core/blob/master/src/plugin.info) file
(see the field `min-paint-version`).

#### Setting up Core plugin for development

1. Clone this repository (`$ git clone https://github.com/PaintDotJava/Core`).
2. Import the source into the IDE of your choice.
3. Add the Paint.JAVA project OR `PaintDotJava-Minimal.jar` which you set up earlier,
to the build path of the new project.
4. Run `CorePlugin.java`. Make sure it works. If it doesn't and you can't work
out why, create an issue.
5. Make whatever changes you want.
6. Commit your changes and make a pull request.
