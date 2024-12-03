# Finnhub API Documentation

## Introduction

We utilize the Finnhub API for retrieving stock prices, company profiles, and
other market data. Detailed documentation is available
[here](https://finnhub.io/docs/api).

### Response Data Type

The Finnhub API returns responses in JSON format.

## API Usage Examples

### Quote

**Parameters:**

- `symbol`: The stock ticker.
- `token`: The API token.

**Sample request:**

```txt
https://finnhub.io/api/v1/quote?symbol=AAPL&token=YOUR_API_TOKEN
```

**Sample response (JSON output):**

```json
{
  "c": 229.87,
  "d": 1.35,
  "dp": 0.5908,
  "h": 230.7199,
  "l": 228.06,
  "o": 228.06,
  "pc": 228.52,
  "t": 1732309200
}
```

### Company Profile 2

**Parameters:**

- `symbol`: The stock ticker.
- `token`: The API token.

**Sample request:**

```txt
https://finnhub.io/api/v1/stock/profile2?symbol=AAPL&token=YOUR_API_TOKEN
```

**Sample response (JSON output):**

```json
{
  "country": "US",
  "currency": "USD",
  "estimateCurrency": "USD",
  "exchange": "NASDAQ NMS - GLOBAL MARKET",
  "finnhubIndustry": "Technology",
  "ipo": "1980-12-12",
  "logo": "https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/AAPL.png",
  "marketCapitalization": 3474674.353644472,
  "name": "Apple Inc",
  "phone": "14089961010",
  "shareOutstanding": 15115.82,
  "ticker": "AAPL",
  "weburl": "https://www.apple.com/"
}
```
