package com.teamtreehouse.blog;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.teamtreehouse.blog.dao.BlogDao;
import com.teamtreehouse.blog.model.BlogEntry;
import com.teamtreehouse.blog.model.Comment;
import com.teamtreehouse.blog.model.SimpleBlogDao;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;


/**
 * Created by Vinson on 9/15/2016.
 */


public class Main {
    public static void main(String[] args) {
        BlogDao dao = new SimpleBlogDao();
        staticFileLocation("/public"); // Static files
        final boolean[] generatedBlogPosts = {false};

        before((req, res) -> {
            if (req.cookie("password") != null) {
                req.attribute("password", req.cookie("password"));
            }
        });

        before("/", (req, res) -> {
            if (!generatedBlogPosts[0]) {
                for (int i=1; i<4; i++) {
                    BlogEntry blogEntry = new BlogEntry(
                            "Blog Entry " + Integer.toString(i),
                            "Entry for blog entry "+ Integer.toString(i) +".");
                    for (int j=0; j<i; j++) {
                        blogEntry.addTags(Integer.toString(j));
                    }
                    dao.addEntry(blogEntry);
                }
                generatedBlogPosts[0] = true;
            }
        });

        before("/protected/*" , (req, res) -> {
            boolean authenticated = false;
            if (req.attribute("password")!=null){
                if (req.attribute("password").equals("admin")) {
                    authenticated = true;
                }
            }
            if (!authenticated) {
                res.redirect("/password");
                halt();
            }
        });

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("blogEntries", dao.findAllEntries());

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/", (req, res) -> {
            String title = req.queryParams("title");
            String entry = req.queryParams("entry");

            BlogEntry blogEntry = new BlogEntry(title, entry);
            blogEntry.addTags(req.queryParams("tags"));
            dao.addEntry(blogEntry);
            res.redirect("/");
            return null;
        });

        get("/tag/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("blogEntries", dao.findTags(req.params("slug")));

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/pass", (req, res) -> {
            String password = req.queryParams("password");
            res.cookie("password", password);
            res.redirect("/");
            return null;
        });

        get("/protected/new", (req, res) -> new ModelAndView(null, "new.hbs"), new HandlebarsTemplateEngine());

        post("/protected/edit/:slug", (req, res) -> {
            dao.findEntryBySlug(req.params("slug")).editEntry(
                    req.queryParams("title"),
                    req.queryParams("entry"));
            res.redirect("/");
            return null;
        });

        get("/protected/delete/:slug", (req, res) -> {
            dao.removeEntry(dao.findEntryBySlug(req.params("slug")));
            res.redirect("/");
            return null;
        });

        get("/protected/edit/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("blogEntry", dao.findEntryBySlug(req.params("slug")));
            return new ModelAndView(model, "edit.hbs");
        }, new HandlebarsTemplateEngine());

        get("/password", (req, res) -> new ModelAndView(null, "password.hbs"), new HandlebarsTemplateEngine());

        get("/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("blogEntry", dao.findEntryBySlug(req.params("slug")));
            return new ModelAndView(model, "detail1.hbs");
        }, new HandlebarsTemplateEngine());

        post("/:slug/comment", (req, res) -> {
            Comment comment = new Comment(req.queryParams("name"), req.queryParams("comment"));
            BlogEntry blogEntry = dao.findEntryBySlug(req.params("slug"));
            blogEntry.addComment(comment);
            res.redirect("/" + req.params("slug"));
            return null;
        });

    }


}
