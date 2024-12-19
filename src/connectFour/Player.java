/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231037 - Al-khiqmah Manzilatul Mukaromah
 * 2 - 5026231095 - Akhtar Zia Faizarrobbi
 * 3 - 5026231227 - Arjuna Veetaraq
 */

package connectFour;

public class Player {
    private String name;
    private int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }
    public boolean hasWon() {
        return score >= 3;
    }
}