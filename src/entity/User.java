package entity;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String status;  // true: online, false: offline
    private int score;
    private int win;

    // Constructor không tham số
    public User() {}

    // Constructor với tham số
    public User(int id, String username, String password, String status, int score, int win) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.score = score;
        this.win = win;
    }

    public User(String username, String status) {
        this.username = username;
        this.status = status;
    }


    // Getter và Setter cho id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter cho username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter và Setter cho password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    // Getter và Setter cho score
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Getter và Setter cho win (số trận thắng)
    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

}
