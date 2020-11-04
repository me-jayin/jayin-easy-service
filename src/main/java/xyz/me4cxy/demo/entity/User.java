package xyz.me4cxy.demo.entity;

/**
 * @author Jayin
 * @create 2020/10/30
 */
public class User {
    private String name;
    private Card card;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}