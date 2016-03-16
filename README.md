# LogarithmicGraphForAndroid
Android - Plotting a Frequency vs dB graph in Logarithmic scale (intermediate version)

Hello folks! Well this is just a light weight simple graph which I created during one of my projects related to sound systems and it's calibration. That time, I didn't find any suitable library. I have done little further which includes one secondary editable or movable plot which responds to move gesture. I haven't get time to include that because it was time taking to remove project dependencies and upload a full independent code. The dimensions used may be not suitable for every device because scaling dimensions to display I planned to do later. Hopefully I'll upload more matured version soon. 

To get the smooth curve or graph, you can use any available QudraticBezierCurve algorithms OR you change from canvas.drawLine() to canvas.drawPath with path.cubeTo or path.quadTo. Everything is done using Android native components, so hopefully, there wouldn't by any performance issue.

For any queries mail me on binujayaram@hotmail.com



