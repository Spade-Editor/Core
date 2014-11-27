Spade Core
==========
Official core plugin containing basic editing tools for Spade

## Developing

Before you can work on the Core plugin, you must first have Spade set up for development.

#### Setting up Spade for development
There are two options:

1. If you are also developing the base framework, or are developing the plugin for an
unreleased version, follow the instructions
[here](https://github.com/Spade-Editor/Spade#developing) to get Spade setup
in your IDE.

2. If you are just interested in developing this plugin, and for the current release of
Spade\*, download the latest `Spade-Minimal.jar` from the
[release page](https://github.com/Spade-Editor/Spade/releases) and use that for
step 4 below.

\* It's important to note that if the master build of Core has a minimum spade version
requirement higher than the latest release, you need to follow option 1, or else your
contributions may not be able to be merged back into master due to being out of date.

The minimum Spade version of Core can be found in the
[`plugin.info`](https://github.com/Spade-Editor/Core/blob/master/src/plugin.info) file
(see the field `min-spade-version`).

#### Setting up Core plugin for development

1. Clone this repository (`$ git clone https://github.com/Spade/Core`).
2. Import the source into the IDE of your choice.
3. Add the Spade.JAVA project OR `Spade-Minimal.jar` which you set up earlier,
to the build path of the new project.
4. Run `CorePlugin.java`. Make sure it works. If it doesn't and you can't work
out why, create an issue.
5. Make whatever changes you want.
6. Commit your changes and make a pull request.
