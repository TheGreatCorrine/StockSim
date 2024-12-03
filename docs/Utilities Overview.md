# Notes for latest update

## How this app works

There're three manager utilities which handle global access stuff.

1. `SessionManager`
   1. This is a singleton instance which runs both on server side and client
      side. When a user logs in, the server side manager will create a unique
      token for this new session and keep track of it, as well as sends the
      token back to client side.
   2. The manager on the client side will store this token, and when requests
      are sent from client side, the input data will be wrapped with the unique
      token to retrieve the current user.
   3. You don't need to worry about the implementation for now, if you need add
      credential for your use case when doing controller stuff, follow what I
      did in execute buy controller, just add a dummy string.
2. `ServiceManager`
   1. Some classed like controller, interactor, and presenter need to be
      instantiated when the app runs, and ui components may need to call them to
      send data or get data back. Instead of creating these instances inside the
      component, we created them in the app builder at start.
   2. Every class that need to instantiated and accessed should register itself
      in this manager in `AppBuilder`, and components should call the manager
      for this service. You may want to check the `OrderEntryPanel` for code
      sample.
3. `ViewManager`
   1. There're more than one causes that can trigger a ui component refresh, we
      wrap these causes with its new data as payload, and type. All the stuff
      that may need to be handled by ui components are called view events.
   2. For example, after the interactor has finished its work, it sends the
      output data to presenter, the presenter is responsible for identifying the
      event type, and create a event and put the output data inside the event.
   3. Then this event is sent to ViewManager. Presenter does not need to worry
      about how to handle other things, just prepare this event to call
      ViewManager. The ViewManager will then broadcasting this event to all the
      registered components.
   4. We say register to mean that a panel or component is created and add to
      the manager. The manager use something called CardLayout in Java Swing to
      handle which component should be visible. So the SwitchPanel event is not
      handled by component, it's handle by the manager.
   5. You may want to define a new event. First you should choose one type or
      add one in the EventType enum, the set up the XXXEvent class. Also you
      need to go to the specific ui component and modify its code, so that it
      can support this type, as well as define how the ui component should
      change according to the new data.
   6. New panels need to register itself to ViewManager through `AppBuilder`
      file, and you also need to set it up in `Main` file to make sure that this
      build method is called when the app runs.
4. `StockMarket`
   1. This is a singleton instance which runs when the app runs, and updates the
      stock data automatically.
   2. It will update the stock data every 2 minutes, ensure that the stock data
      is always up-to-date.
   3. This will not exceed the limit of API calls to Finnhub.
