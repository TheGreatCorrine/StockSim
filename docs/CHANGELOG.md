# Project Changelog

## Tags

- [1.0.0](#1.0.0)
- [0.2.1](#0.2.1)
- [0.2.0](#0.2.0)
- [0.1.0](#0.1.0)

## 1.0.0

### New Features

- Implemented the `execute_buy` and `execute_sell` use cases
- Implemented `FontManager` to set custom font for GUI components
- Implemented `DatabaseUserDataAcces` with MongoDB database
- Implemented database integration with sign up and log in
- Added writing to database for `execute_buy` use case
- Added writing to database for `execute_sell` use case

### Internal Changes

- Added a statement that can throw `NumberFormatException` in `execute_buy` and
  `execute_sell` controllers
- Added a new `addTransaction` method in `User.java`
- Implemented DTO objects for database data transfer
- Implemented asynchronous stock data update for performance enhancement
- Implemented cache for database user DTO
- Changed `MarketTrackerConfigLoader` to `ConfigLoader` to handle different
  types of property loading from config.
- Rebased ViewHistory's dashboard button to interact with
  `ViewHistoryController` instead of directly initiating switch panel event
- Renamed `StockDataAccessObject` to `FinnhubStockDataAccessObject` to specify
  stock dao's api
- Implemented numeric sorting for tables in `MarketSearchPanel`,
  `PortfolioPanel`, and `TransactionHistoryPanel`

### Bug Fixes

- Fixed issues handling the invalid inputs in the `execute_buy` and
  `execute_sell` use cases
- Fixed issues with converting between entities and MongoDB `Document`
- Fixed input fields not cleared after executing

## 0.2.1

### New Features

- Implemented `MarketObserver` to update user data when stock market changes
- Implemented logout use case
- Implemented registration use case

### Internal Changes

- Refactored `StockMarket` to `MarketTracker`
- Refined all ui components to have a consistent style
- Changed `StockDataAccessInterface` to have two methods to do different api
  calls
- Implemented new methods from `StockDataAccessInterface` in
  `InMemoryStockDataAccessObject` and `StockDataAccessObject`
- Separate API update config into `market-tracker-config` file and implement
  `ConfigLoader` to load static configuration
- Separated API update config into `market-tracker-config` file and implement
  `ConfigLoader` to load static configuration
- Updated stock data provider from Alpha Vantage to Finnhub
- Removed references to Alpha Vantage in documentation and codebase
- Added service manager for `RegistrationController`, `RegistrationInteractor`,
  and `RegistrationPresenter`

### Bug Fixes

- Fixed `MarketTracker` not update prices after first initialization
- Fixed `AssetPanel` and `PortfolioPanel` not updated after buy use case

## 0.2.0

### New Features

- Added `ViewHistory` use case to view transaction history
- Added GUI components for view transaction history page
- Implemented login use case
- Implemented periodic update of stock information from data access interface in
  `StockMarket` that self-adjust based on API rate limit
- Changed API to Finnhub API
- Implemented integration of `StockMarket` and `StockDataAccessObject`

### Internal Changes

- Implemented `ClientSessionManager` for client-side session management
- Implemented credential verification in `BuyStockController`
- Moved `ValidationException` into a separate class in utility
- Implemented `StockDataAccess` to retrieve current market price for set tickers
- Modified `StockMarket` to utilize new IStockDataAccess return type
- Updated maven configuration with api request dependencies
- Added config resource file for 30 preset ticker names
- Modified `InMemoryStockDataAccessObject` to utilize new IStockDataAccess
  return type
- Added new api calls in `StockDataAccess` to retrieve and store company name
  and industry
- Modified `Stock`, `StockMarket`, and `InMemoryStockDataAccessObject` to store
  and utilize new company and industry data
- Modified `InMemoryUserDataAccessObject` to utilize
  `ViewHistoryDataAccessInterface` as well
- Implemented filter filed in market search panel
- Changed password input field using `JPasswordField`
- Added `getAssets` method in user entity
- Reformated app builder to accept more configs
- Implemented singleton pattern for service manager
- Added test cases for `ViewHistoryInteractor` with mock buy and sell
  transactions.
- Refined ui components, extracted table component to its own class
- Refactored `ServiceManager` registration process into object constructor

### Bug Fixes

- Fixed `TransactionHistoryPanel` not updated after buy use case

## 0.1.0

### New Features

- Added `SessionManager` to validate client request by credential and manage
  active sessions
- Added GUI components for trading page

### Internal Changes

- Created documentation for GitHub workflow and pull request template
- Added maven workspace and configuration
- Added Use Cases 4 and 5
- Implemented entities for stock and stock market
- Implemented entities for user, portfolio, and transaction
- Added synchronization checks and null checks in `StockMarket`
- Modified `Portfolio` constructor to match the optional return value of
  `StockMarket.getStock`
- Modified `Portfolio` constructor to take `Map<String, UserStock>` as parameter
  and remove synchronization with `StockMarket` data
- Updated `UserStock`: Rename `PurchasePrice` to `cost`
- Implemented controller and presenter for execute buy use case
- Implemented framework for view component update
- Added GUI components for trading page
- Implemented buy use case interactor and set up unit tests
- Removed outdated classes and methods that are incompatible with current
  codebase version
- Updated maven configuration with unit test and api request dependencies
- Implemented the frontend framework

### Bug Fixes
