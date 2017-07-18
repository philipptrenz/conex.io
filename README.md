<img src="img/logo.png" alt="our logo" width="400">

The conex.io project provides an API to interact with home automation devices, which are connected to a home automation server, detached from the manufacturer specific communication syntax. 

Therefore devices with their different value definitions get abstracted from the home automation server and unified to make them available at a easy to use RESTlike API for developers of home automation applications.

This is a prototypical implemementation for the home automation server [FHEM](https://fhem.de/).

**For further information take a look at the Wiki!**

## Get started

The application uses `jsonlist2` and the longpoll mode `websocket` from the FHEM module `FHEMWEB` to connect to your FHEM installation.
Therefor define a new FHEMWEB device, set attributes `longpoll` to `websocket` and `csrfToken` to `none` (current workaround).

* Install Java 8, Maven and git
* Clone repo with `git clone https://github.com/philipptrenz/conex.io/` and switch to this folder
* Open `src/main/resources/application.properties` and configure `fhem.url`, `fhem.port` as well as the optional credentials to your FHEM installation
* Run with `mvn spring-boot:run` *OR* generate a runnable JAR with `mvn package` (can be found in the `target` folder)

The software fetches all devices via `jsonlist2` on boot and receives updates by `longpoll`. Also on global events (fhem.cfg saved, new devices installed, ...) all data gets updated with `jsonlist2` to stay in sync, no reboot is required.


## Helpful notes

This API was partly generated using the [swagger-codegen](https://github.com/swagger-api/swagger-codegen) project. 

The underlying library integrating swagger to SpringBoot is [springfox](https://github.com/springfox/springfox)  

You can view the api documentation in the swagger-ui: http://\<ip-of-your-device\>:8080/v0/

Change the default api port value in the application.properties.
