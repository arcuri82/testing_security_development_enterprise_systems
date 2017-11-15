The files in this folder (besides the pom.xml and Dockerfile) have been generated following
the React Tutorial instructions at:

https://reactjs.org/docs/installation.html

i.e., by using on the command line:

npm install -g create-react-app
create-react-app welcome

and then copied over the files here in this folder.


Only one file was modified, which is "package.json" file, by replacing

"test": "react-scripts test --env=jsdom",

with

"test": "react-scripts test --env=jsdom  --coverage",


why? Because the tests would run in interactive mode, and
React does not have any easy way to run those tests directly in a cross-platform way.
In other words, it does not have a flag to deactivate the interactive mode, and its
developers have no intention to add it:

https://github.com/facebookincubator/create-react-app/issues/1137

so, the workaround for this "feature" is to activate the "--coverage" flag...


You might wonder why I am writing this here, and not directly in the "package.json" file
where the change was needed... well, the answer is simple: JSON is a data transfer
format, not meant for configurations and manual editing, so it does NOT support comments...
Why does NPM rely on JSON for configuration files? Well, if you want to have a good laugh,
read the explanation here from the creator of NPM:

https://groups.google.com/forum/#!msg/nodejs/NmL7jdeuw0M/yTqI05DRQrIJ

Add the fact that NPM does not support deterministic builds, and dependencies are
not signed, you can get an understanding of why I do not have such a high opinion of NPM...
especially when compared with Maven/Gradle.


-------------------------------
Using Docker

Once the "build" folder has been created with "mvn compile", you can manually do:

docker build . -t welcome

docker run -p 80:80 welcome

and then point your browser to "http://localhost"

Note: the Docker image is using Httpd Apache Server.
For service static files (eg, HTML/CSS/JS) there is no need for NodeJS,
even if the JS files are first transpiled with NPM.






