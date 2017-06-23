# Site Availability Monitor


## Running the development profile example
In development profile the software tests 5 pre-configured sites, every 15 seconds.  
To see it in action run the following command
```
java -Dspring.profiles.active=development -jar sam-0.0.1-SNAPSHOT.jar
```
Open http://localhost:8080/ in your browser to see the monitoring results. Browser will reload monitoring status automatically.
Slf4j binding to a logging framework is not provided, so logs will flow on the console.


## Running with your own configuration
In default mode, the software scans the sites every 30 seconds. The connection timeout is configured for 15 seconds.
Create a file application-production.yml listing the websites (yaml format) to monitor with the mandatory content to check in each one
```
webpages:
  -
    url: http://start.spring.io/
    mandatoryContent: "Secure your application"
  -
    url: https://facebook.github.io/react/
    mandatoryContent: "painless"
```
Then launch the following command from the same folder where the file was created.
```
java -Dspring.profiles.active=production -jar sam-0.0.1-SNAPSHOT.jar
```
Open http://localhost:8080/ in your browser to see the monitoring results. Browser will reload monitoring status automatically.
Slf4j binding to a logging framework is not provided, so logs will flow on the console.


## Advanced Configuration

### Configuring custom monitoring period and timeout
To change the frequency of polling and/or the timeout configured for a site, 
you may override the default settings by adding the following section to the file application-production.yml 
```
monitoringSettings:
  pollingFrequencyCronExpression: 0,30 * * * * *
  timeOutForPollRequestSeconds: 15
```
* pollingFrequencyCronExpression - is a cron expression including seconds. See org.springframework.scheduling.support.CronSequenceGenerator
* timeOutForPollRequestSeconds - Max time the monitoring application will wait for the service to respond.
Please ensure the timeOutForPollRequestSeconds is configured to be less than the duration between successive monitoring cycles.

### More threads for async
The default configuration in AsyncConfiguration should suffice for most use cases.  
However, if there are fare too many sites to monitor you may want to increase the number of threads available for parallel monitoring.  
To do so, add the following to your application-production.yml config file.
```
async:
  corePoolSize: 200
  maxPoolSize: 500
  queueCapacity: 1000
```
