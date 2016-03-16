# LogarithmicGraphForAndroid
Android - Plotting a Frequency vs dB graph in Logarithmic scale (intermediate version)

Hello folks! Well this is just a light weight simple graph which I created during one of my projects related to sound systems and it's calibration. That time, I didn't find any suitable library. I have done little further which includes one secondary editable or movable plot which responds to move gesture. I haven't get time to include that because it was time taking to remove project dependencies and upload a full independent code. The dimensions used may be not suitable for every device because scaling dimensions to display I planned to do later. Hopefully I'll upload more matured version soon. 

To get the smooth curve or graph, you can use any available QudraticBezierCurve algorithms OR you change from canvas.drawLine() to canvas.drawPath with path.cubeTo or path.quadTo. Everything is done using Android native components, so hopefully, there wouldn't by any performance issue.

For any queries mail me on binujayaram@hotmail.com

<form action="https://www.paypal.com/cgi-bin/webscr" method="post">

  <!-- Identify your business so that you can collect the payments. -->
  <input type="hidden" name="business" value="herschelgomez@xyzzyu.com">

  <!-- Specify a Buy Now button. -->
  <input type="hidden" name="cmd" value="_xclick">

  <!-- Specify details about the item that buyers will purchase. -->
  <input type="hidden" name="item_name" value="Hot Sauce-12 oz. Bottle">
  <input type="hidden" name="amount" value="5.95">
  <input type="hidden" name="currency_code" value="USD">

  <!-- Provide a dropdown menu option field. -->
  <input type="hidden" name="on0" value="Type">Type of sauce <br />
  <select name="os0">
    <option value="Select a type">-- Select a type --</option>
    <option value="Red">Red sauce</option>
    <option value="Green">Green sauce</option>
  </select> <br />

  <!-- Display the payment button. -->
  <input type="image" name="submit" border="0"
    src="https://www.paypalobjects.com/en_US/i/btn/btn_buynow_LG.gif"
    alt="PayPal - The safer, easier way to pay online">

  <img alt="" border="0" width="1" height="1"
  src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" >

</form>


