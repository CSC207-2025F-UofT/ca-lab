# Stock Project

A comprehensive stock market application built with Java Swing, following Clean Architecture principles. This application provides real-time stock data, news, earnings history, and market status information.

## Features

### User Management
- **User Registration**: Create a new account with username and password
- **User Login**: Secure authentication to access the application
- **Password Management**: Change password functionality
- **User Logout**: Secure session termination

### Stock Information
- **Stock Search**: Search for stocks by symbol with detailed information
- **Filter Search**: Advanced filtering capabilities for stock discovery
- **Real-Time Trade Feed**: Live WebSocket connection to receive real-time trade data
  - Connect to any trading symbol (e.g., `BINANCE:BTCUSDT`, `AAPL`, `TSLA`)
  - View real-time price, volume, and timestamp updates
  - Rate limiting protection to prevent API throttling
  - Automatic error handling for invalid symbols or connection issues

### Market Data
- **Market News**: View latest market and company-specific news articles
- **Earnings History**: Access historical earnings data for companies
- **Market Status**: Real-time market open/closed status indicator

## Architecture

This project follows **Clean Architecture** principles with clear separation of concerns:

```
src/main/java/
├── app/                    # Application entry point and dependency injection
├── data_access/            # Infrastructure layer (API clients, file I/O)
├── entity/                 # Domain models
├── interface_adapter/      # Adapters (ViewModels, Controllers, Presenters)
├── use_case/              # Business logic (Interactors)
└── view/                  # Presentation layer (Swing UI)
```

### Key Components

- **Entity Layer**: Domain models (`User`, `Stock`, `StockQuote`, `Trade`, `NewsArticle`, `EarningsRecord`, `MarketStatus`)
- **Use Case Layer**: Business logic and application rules
  - User management: Login, Signup, Logout, Change Password
  - Stock operations: Stock Search, Filter Search
  - Market data: News, Earnings History, Market Status
  - Real-time data: Trade Feed
  - User features: Watchlist management
- **Interface Adapter Layer**: Controllers, Presenters, and ViewModels
- **Framework Layer**: Data access objects and UI components (Swing views)

## Getting Started

### Prerequisites
- Java 15 or higher
- Maven 3.6+
- Internet connection (for API access)

### Installation & Running

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd CSC207_Stock_Project
   ```

2. **Build the project**:
   ```bash
   mvn clean compile
   ```

3. **Run the application**:
   ```bash
   mvn exec:java -Dexec.mainClass="app.Main"
   ```
   Or run `Main.java` directly from your IDE

4. **Run tests**:
   ```bash
   mvn test
   ```

### First Time Setup

1. **Create an Account**: 
   - Launch the application
   - Click "Sign Up" to create a new user account
   - Enter a username and password
   
2. **Login**: 
   - Use your credentials to log in
   
3. **Explore Features**: 
   - Navigate using the top toolbar buttons
   - Try searching for stocks, viewing news, or connecting to real-time trades

## Usage Guide

### User Management

#### Sign Up
1. Launch the application
2. Enter a username and password
3. Click "Sign Up" to create your account
4. You'll be automatically redirected to the login page

#### Login
1. Enter your username and password
2. Click "Log In" to access the application
3. You'll be taken to the main dashboard (Logged In View)

#### Change Password
1. After logging in, enter your new password in the password field
2. Click "Change Password" to update your credentials
3. You'll receive a confirmation message upon success

#### Logout
1. Click "Log Out" to securely end your session
2. You'll be redirected to the login page

### Main Dashboard Features

Once logged in, you'll see the main dashboard with the following features:

#### Stock Search & Watchlist
1. Enter a stock symbol (e.g., `AAPL`, `TSLA`, `GOOGL`) in the search field on the main dashboard
2. Click "Search" to retrieve detailed stock information
3. View stock details in the "Stock Information" panel including:
   - Company name and sector
   - Current price and market data
   - Price changes and volume
4. **Add to Watchlist**: Click the "Add to Watchlist" button at the bottom of the main dashboard to save the currently displayed stock information to your watchlist
5. **View Watchlist**: Click the "Account" button in the top toolbar to view all your saved watchlist items

#### Real-Time Trade Feed
1. Click the **"Realtime Trade"** button in the top toolbar
2. Enter a trading symbol in the input field (default: `BINANCE:BTCUSDT`)
   - **Crypto pairs work best**: `BINANCE:BTCUSDT`, `BINANCE:ETHUSDT`
   - **Stock symbols**: `AAPL`, `TSLA` (only active during market hours)
3. Click **"Connect"** to start receiving real-time trade data
4. View live updates for:
   - Symbol
   - Last Price
   - Volume
   - Timestamp
5. Click **"Disconnect"** to stop the feed before connecting to a new symbol

**Important Notes:**
- You must disconnect before connecting to a new symbol
- Rate limiting is enforced (5-second cooldown between connections)
- Invalid symbols or connection errors will display appropriate error messages
- The application automatically handles API rate limits (429 errors)
- Crypto pairs (e.g., `BINANCE:BTCUSDT`) work 24/7, while stock symbols only work during market hours

#### Filter Search
1. Click the **"Filter Search"** button in the top toolbar
2. Use the search and filter options to find stocks by:
   - Symbol pattern
   - Market criteria
   - Other filtering options
3. View filtered results in the table
4. Click "Back" to return to the main dashboard

#### News
1. Click the **"News"** button in the top toolbar
2. View latest market news and company-specific articles
3. Filter news by company symbol if needed
4. Click "Back" to return to the main dashboard

#### Earnings History
1. Click the **"History"** button in the top toolbar
2. Enter a stock symbol to view historical earnings data
3. Browse through past earnings records and dates
4. Click "Back" to return to the main dashboard

#### Account & Watchlist
1. Click the **"Account"** button in the top toolbar
2. View all your saved watchlist items that you've added from the main dashboard
3. Your watchlist displays all the stock information you've saved using the "Add to Watchlist" button
4. Click "Back" to return to the main dashboard

#### Market Status
- The market status indicator is displayed in the top toolbar
- Shows current market status (Open/Closed) in real-time
- Updates automatically when market status changes
- Color-coded: Green for open, Gray for closed, Red for errors

## Technical Details

### Dependencies
- **OkHttp 4.12.0**: For HTTP and WebSocket connections
- **JSON 20240303**: For JSON parsing
- **JUnit 5.8.1**: For testing (test scope)

### API Integration
- **Finnhub API**: Used for stock data, news, earnings, and real-time trade feeds
- WebSocket connections for real-time data streaming
- REST API for historical and static data

### Data Storage
- User data stored in `users.csv` (file-based storage)
- Alternative database storage option available (`DBUserDataAccessObject`)

## Project Structure

```
CSC207_Stock_Project/
├── src/
│   ├── main/java/
│   │   ├── app/              # Application setup
│   │   ├── data_access/      # API clients and data sources
│   │   ├── entity/           # Domain models
│   │   ├── interface_adapter/# Adapters
│   │   ├── use_case/         # Business logic
│   │   └── view/             # UI components
│   └── test/java/           # Unit tests
├── pom.xml                   # Maven configuration
└── README.md                 # This file
```

## Error Handling

The application includes comprehensive error handling across all features:
- **User Authentication**: Clear error messages for invalid credentials or existing usernames
- **Password Management**: Validation for password changes and mismatches
- **Stock Search**: Error messages for invalid symbols or search failures
- **Rate Limiting**: Automatic cooldown periods to prevent API throttling (trade feed)
- **Connection Errors**: User-friendly error messages for WebSocket connection issues
- **Invalid Symbols**: Clear feedback when symbols are not found or unavailable
- **Network Issues**: Graceful handling of network failures and API unavailability
- **Data Loading**: Error handling for failed data fetches (news, earnings, market status)

## Use Cases Implemented

The application implements the following use cases, each following Clean Architecture:

### User Management
- **Signup**: Create new user accounts with validation
- **Login**: Authenticate users and manage sessions
- **Logout**: Terminate user sessions securely
- **Change Password**: Update user passwords with validation

### Stock Operations
- **Stock Search**: Search for stocks by symbol and display detailed information
- **Filter Search**: Advanced filtering and search capabilities for stock discovery

### Market Data
- **News**: Fetch and display market and company-specific news articles
- **Earnings History**: Retrieve and display historical earnings data for companies
- **Market Status**: Real-time market open/closed status with automatic updates

### Real-Time Features
- **Trade Feed**: WebSocket-based real-time trade data streaming
  - Supports both crypto pairs and stock symbols
  - Automatic reconnection and error handling
  - Rate limiting protection

### User Features
- **Watchlist**: Save and manage favorite stocks in user accounts
- **Account Management**: View and manage user account information

## Design Patterns & Principles

### SOLID Principles
- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed**: System open for extension (new implementations) but closed for modification
- **Liskov Substitution**: All interface implementations are interchangeable
- **Interface Segregation**: Focused interfaces (e.g., separate interfaces for Signup, Login, ChangePassword)
- **Dependency Inversion**: High-level components depend on abstractions (interfaces), not concrete implementations

### Design Patterns
- **Observer Pattern**: PropertyChangeListener for view updates
- **Adapter Pattern**: TradeListener adapts async callbacks to synchronous presenter calls
- **Builder Pattern**: AppBuilder for dependency injection and application setup
- **Strategy Pattern**: Swappable data access implementations (FileUserDataAccessObject, DBUserDataAccessObject)

## Testing

The project includes unit tests for use cases:
- Login, Signup, Logout tests
- Stock Search, Filter Search tests
- News, Earnings History, Market Status tests
- Additional tests can be run with `mvn test`

## Contributing

This is a course project following Clean Architecture principles. When contributing:
- Maintain separation of concerns across layers
- Follow the existing architecture patterns
- Add appropriate error handling
- Include unit tests for new features
- Respect interface boundaries and dependency inversion

## License

[Add your license information here if applicable]

