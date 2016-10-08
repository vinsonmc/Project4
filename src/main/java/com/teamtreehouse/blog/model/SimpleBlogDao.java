package com.teamtreehouse.blog.model;

import com.teamtreehouse.blog.dao.BlogDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vinson on 9/29/2016.
 */
public class SimpleBlogDao implements BlogDao {
    private List<BlogEntry> blogEntries;

    public SimpleBlogDao() {
        blogEntries = new ArrayList<>();
    }

    @Override
    public boolean addEntry(BlogEntry blogEntry) {
        return blogEntries.add(blogEntry);
    }

    @Override
    public boolean removeEntry(BlogEntry blogEntry) {
        return blogEntries.remove(blogEntry);
    }

    @Override
    public List<BlogEntry> findAllEntries() {
        return new ArrayList<>(blogEntries);
    }

    @Override
    public BlogEntry findEntryBySlug(String slug) {
        return blogEntries.stream()
                .filter(blogEntry -> blogEntry.getSlug().equals(slug))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<BlogEntry> findTags(String slug) {
        return new ArrayList<>(blogEntries.stream()
                .filter(blogEntry -> blogEntry.getTags().contains(slug))
                .collect(Collectors.toList()));
    }

}
