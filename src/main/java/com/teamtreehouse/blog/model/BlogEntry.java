package com.teamtreehouse.blog.model;

import com.github.slugify.Slugify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BlogEntry {
    private String slug;
    private String title;
    private String entry;
    private List<Comment> comments;
    private LocalDateTime date;
    private List<String> tags;

    public BlogEntry(String title, String entry) {
        this.title = title;
        this.entry = entry;
        comments = new ArrayList<>();
        Slugify slugify = new Slugify();
        slug = slugify.slugify(title);
        date = LocalDateTime.now();
        tags = new ArrayList<>();
    }

    public void editEntry(String title, String entry) {
        this.title = title;
        this.entry = entry;
        Slugify slugify = new Slugify();
        slug = slugify.slugify(title);
    }

    public void addTags(String tags) {
        String[] tagsSplit = tags.split("\\s+");
        Slugify slugify = new Slugify();
        for(String tag: tagsSplit) {
            this.tags.add(slugify.slugify(tag));
        }
    }

    public boolean addComment(Comment comment) {
        // Store these comments!
        return comments.add(comment);
    }

    public String getSlug() {
        return slug;
    }

    public List<Comment> getComments() {
       return new ArrayList<>(comments);
    };

    public String getDate() {
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm");
        String text = date.format(dayFormatter)+ " at " + date.format(timeFormatter);
        return text;
    }

    public List<String> getTags() {
        return new ArrayList<>(tags);
    }

    public String getTitle() {
        return title;
    }

    public String getEntry() {
        return entry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogEntry blogEntry = (BlogEntry) o;

        if (title != null ? !title.equals(blogEntry.title) : blogEntry.title != null) return false;
        return entry != null ? entry.equals(blogEntry.entry) : blogEntry.entry == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (entry != null ? entry.hashCode() : 0);
        return result;
    }
}
