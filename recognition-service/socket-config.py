import socket
import json

HOST = '0.0.0.0'  # Listen on all interfaces
PORT = 9000  # Port your backend will send to

# Create socket
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    print(f"Listening on {HOST}:{PORT}...")

    while True:
        conn, addr = s.accept()
        with conn:
            print(f"Connected by {addr}")
            data = conn.recv(4096)  # Receive data
            if not data:
                continue

            # Decode JSON sent by backend
            try:
                message = json.loads(data.decode())
                record_id = message.get("record_id")
                object_name = message.get("object_name")
                print(f"Received: {record_id}, {object_name}")

                # Here you can call your face recognition / add reference code
                # Example: add_reference(record_id, object_name)

                # Send response back
                response = {"status": "ok"}
                conn.sendall(json.dumps(response).encode())

            except Exception as e:
                conn.sendall(json.dumps({"status": "error", "msg": str(e)}).encode())
