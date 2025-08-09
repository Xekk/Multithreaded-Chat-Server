# 🧵 Multithreaded Java Chat Server

A simple **multithreaded chat application** written in Java using **TCP/IP sockets**.  
Supports multiple concurrent clients, unique usernames, and custom commands.

---

## ✨ Features

- 📡 **Multithreaded server** — handles multiple clients at the same time using Java threads
- 🧑‍💻 **Unique usernames** — clients must choose a name not already in use
- 🔁 **Retry on invalid usernames** — prompts again until the name is accepted
- 📢 **Broadcast chat** — messages are sent to all connected clients
- 🗒️ **Chat commands**:
  - `/who` — lists all currently connected users
- 🛠 **Console-based** — no GUI, easy to run in any terminal

---

## 🛠 Technologies Used

- **Java SE**
- **TCP sockets**
- **Multithreading**
- **BufferedReader / PrintWriter** for I/O

---

## 🚀 Getting Started

### 1️⃣ Clone the repository
```bash
git clone https://github.com/yourusername/chat-server.git
cd chat-server
```
### 2️⃣ Start the server
Run ChatServer.java (default port is 12345).
### 3️⃣ Start clients
Run ChatClient.java. Each instance will correspond to a client.

## 💬 Usage
Upon starting, the client will prompt for a unique username

Type messages to send to everyone

Commands:
```
/who     # Show a list of online users
/msg (username) # Send private message to a user
exit     # Disconnect from the server
```

