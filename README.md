# Security Camera Stream
Development of home security camera stream.

There are two components:
1) transcoder: converts RTSP to MP4
    a) java: an implementation in java; initially there will be several rtsp stream readers
    b) rust: a future implementation, ideally a little more serious
2) ui: display (multiple?) feeds
    a) initially, just an html webpage with a simple video tag
    b) eventually, something more serious (nodejs app?)

----
