package com.teamtreehouse.blog.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {
    private String name;
    private String comment;
    private LocalDateTime date;

    public Comment(String name, String comment) {
        this.name = name;
        this.comment = comment;
        date = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm");
        String text = date.format(dayFormatter)+ " at " + date.format(timeFormatter);
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment1 = (Comment) o;

        if (name != null ? !name.equals(comment1.name) : comment1.name != null) return false;
        return comment != null ? comment.equals(comment1.comment) : comment1.comment == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
