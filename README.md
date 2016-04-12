# 8Wear
This is an android (app/game) which simulates Magic 8 ball on Android Wear.
There are 20 standard [responses](https://en.wikipedia.org/wiki/Magic_8-Ball#Possible_answers) which are triggered by a shake/tap on Android watch.

## Installation
- Import the project in Android Studio which creates two modules - wear and mobile.
- [Setup](http://developer.android.com/training/wearables/apps/creating.html) a watch or emulator.
- Select wear configuration on left side of run button and then run the app.

## How it Works
- MainActivity implements SensorEventListener which triggers responses.(Response can be triggered via tap also)
- Responses are randomly picked from a string array and colored accordingly.
- 8 ball icon is animated using vector drawable where as text using TextSwitcher.

## Credits
- Google
- [Background](http://www.oxygenna.com/news/new-free-set-of-material-design-backgrounds)
- [8 Ball icon svg](https://commons.wikimedia.org/wiki/File:8_ball_icon.svg)