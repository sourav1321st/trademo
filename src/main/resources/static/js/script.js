const userId = 1; // For now fixed user (later we can make login system)

// ---- Load Balance ----
async function loadBalance() {
    try {
        const response = await fetch(`/api/users/${userId}/balance`);
        const data = await response.json();
        document.getElementById("balance").innerText = "₹" + data.balance.toFixed(2);
    } catch (err) {
        document.getElementById("balance").innerText = "Error loading balance";
    }
}

// ---- Get Stock Price ----
async function getPrice() {
    const symbol = document.getElementById("stockSymbol").value;
    if (!symbol) {
        alert("Please enter a stock symbol");
        return;
    }
    try {
        const response = await fetch(`/api/trades/price/${symbol}`);
        const data = await response.json();
        if (data.status === "success") {
            document.getElementById("priceResult").innerText = 
                "Price of " + data.symbol + ": ₹" + data.price;
        } else {
            document.getElementById("priceResult").innerText = "Error: " + data.message;
        }
    } catch (error) {
        document.getElementById("priceResult").innerText = "Error: " + error.message;
    }
}

// ---- Buy Stock ----
async function buyStock() {
    const symbol = document.getElementById("tradeSymbol").value;
    const qty = document.getElementById("tradeQuantity").value;
    if (!symbol || qty <= 0) {
        alert("Enter valid symbol and quantity");
        return;
    }
    try {
        const response = await fetch(`/api/trades/buy?userId=${userId}&stockSymbol=${symbol}&quantity=${qty}`);
        const result = await response.text();
        document.getElementById("tradeResult").innerText = result;
        loadBalance();
    } catch (err) {
        document.getElementById("tradeResult").innerText = "Error: " + err.message;
    }
}

// ---- Sell Stock ----
async function sellStock() {
    const symbol = document.getElementById("tradeSymbol").value;
    const qty = document.getElementById("tradeQuantity").value;
    if (!symbol || qty <= 0) {
        alert("Enter valid symbol and quantity");
        return;
    }
    try {
        const response = await fetch(`/api/trades/sell?userId=${userId}&stockSymbol=${symbol}&quantity=${qty}`);
        const result = await response.text();
        document.getElementById("tradeResult").innerText = result;
        loadBalance();
    } catch (err) {
        document.getElementById("tradeResult").innerText = "Error: " + err.message;
    }
}

// Auto-load balance on page load
loadBalance();
