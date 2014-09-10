##Gateway Android Client

This is the android client for [Gateway][1], an online craigslist-like marketplace for people looking to exchange services and help eachother out. It was built by me and two other students during out CS 4261 Mobile Apps & Services class. 

The app is far from feature complete, as it can only log in and view listings from the [Gateway API][6], but it demonstrates how to architect android apps using Dagger, separate common providers into logical sub-modules, and effortlessly interact with REST APIs using Retrofit. 

I'd love to add [RxJava][7] support, proper unit tests, and even some UI animations in the future. Maybe one day!


###Technologies Used
* [Dagger][2]
* [Retrofit][3]
* [Butterknife][4]
* [Otto][5]


[1]: www.github.com/erawhctim/gateway_web
[2]: www.github.com/square/dagger
[3]: www.github.com/square/retrofit
[4]: www.github.com/jakewharton/butterknife
[5]: www.github.com/square/otto
[6]: https://github.com/erawhctim/gateway_web/blob/master/gateway_api.py
[7]: https://github.com/ReactiveX/rxjava
