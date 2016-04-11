#8Wear

This is an android experiment(app/game) which simulates Magic 8 ball on Android Wear.
There are 20 standard [responses](https://en.wikipedia.org/wiki/Magic_8-Ball#Possible_answers) which are triggered by a shake on Android watch.

##How it Works
1. MainActivity implements SensorEventListener which triggers responses.
2. Responses are randomly picked from a string array and colored accordingly.
3. 8 ball icon is animated using vector drawable where as text using TextSwitcher.

##Credits
- Google
- [Background](http://www.oxygenna.com/news/new-free-set-of-material-design-backgrounds)