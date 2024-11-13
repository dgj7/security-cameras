# Security Camera Stream
Development of home security camera stream.

There are two components:
* transcoder: converts RTSP to MP4
  * java: an implementation in java; initially there will be several rtsp stream readers
  * rust: a future implementation, ideally a little more serious
* ui: display (multiple?) feeds
  * initially, just an html webpage with a simple video tag
  * eventually, something more serious (nodejs app?)

## Additional Resources
* [soap](/soap/) - _learning about the SOAP service exposed by ONVIF devices_

----
