<img src="img/logo.png" alt="our logo" width="400">

**Authors:** Philipp Trenz, Benjamin Brausse, Timo Schwan, Christin Voscort, Marcel Wemmer

The conex.io project provides an API to interact with home automation devices, which are connected to a home automation server, detached from the manufacturer specific communication syntax. 

Therefore devices with their different value definitions get abstracted from the home automation server and unified to make them available at a easy to use RESTlike API for developers of home automation applications.

This is a prototypical implemementation for the home automation server [FHEM](https://fhem.de/).

**For further information take a look at the Wiki!**

## Related Publications

[1] O.Droegehorn, P.Trenz, B.Brausse, T.Schwan, C.Voscort and M.Wemmer. **Capability Based Communication for Green Buildings and Homes: A REST-like API within the conex.io project**. Proceedings of the 51st Hawain International Conference on System Sciences, ISBN 978-0-9981331-1-9, p.5767-5776, URI: http://hdl.handle.net/10125/50612, 2018. [PDF](http://scholarspace.manoa.hawaii.edu/bitstream/10125/50612/1/paper0725.pdf).

## Get started

The application uses `jsonlist2` and the longpoll mode `websocket` from the FHEM module `FHEMWEB` to connect to your FHEM installation.
Therefor define a new FHEMWEB device, set attributes `longpoll` to `websocket` and `csrfToken` to `none` (current workaround).

* Install Java 8, Maven and git - On a Raspberry Pi open the terminal and enter `sudo apt-get install oracle-java8-jdk maven git`
* Clone repo with `git clone https://github.com/philipptrenz/conex.io/` and switch to this folder
* Open `src/main/resources/application.properties` and configure `fhem.url`, `fhem.port` as well as the optional credentials to fit the FHEMWEB device in your FHEM installation
* Run with `mvn spring-boot:run` *OR* generate a runnable JAR with `mvn package` (can be found in the `target` folder)

If you are using slower hardware, e.g. a Raspberry Pi, please be patient. Fetching all dependencies, compiling and getting the software to run will take a while ...

The software fetches all devices via `jsonlist2` on boot and receives updates by `longpoll`. Also on global events (fhem.cfg saved, new devices installed, ...) all data gets updated with `jsonlist2` to stay in sync, no reboot is required.


## Helpful notes

This API was partly generated using the [swagger-codegen](https://github.com/swagger-api/swagger-codegen) project. 

The underlying library integrating swagger to SpringBoot is [springfox](https://github.com/springfox/springfox)  

You can view the api documentation in the swagger-ui: http://\<ip-of-your-device\>:8080/v0/

Change the default api port value in the application.properties.
