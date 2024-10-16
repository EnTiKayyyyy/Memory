package services;

import java.io.*;
import java.net.*;
import java.util.List;

import entity.User;

public class Client {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client() {
        try {
            // Kết nối đến server, giả sử server đang chạy ở localhost và port 12345
            clientSocket = new Socket("localhost", 12345);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<User> fetchUserData() {
        List<User> users = null;
        try {
            out.writeObject("GET_USER_DATA");  // Gửi yêu cầu
            out.flush();

            // Nhận danh sách User từ server
            users = (List<User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();  // Đóng kết nối
        }
        return users;
    }

    public void close() {
        try {
            if (out != null) {
                out.writeObject("bye");
                out.flush();
            }
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            return (String) in.readObject();  // Nhận phản hồi từ server
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startConnection("127.0.0.1", 12345);
        client.stopConnection();
    }
}

