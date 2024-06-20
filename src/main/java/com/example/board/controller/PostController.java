package com.example.board.controller;

import com.example.board.entity.Post;
import com.example.board.entity.Category;
import com.example.board.service.PostService;
import com.example.board.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "index";
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);
        if (post != null) {
            model.addAttribute("post", post);
            model.addAttribute("categories", categoryService.findAll());
            return "view";
        } else {
            return "redirect:/"; // post가 없는 경우 메인 페이지로 리디렉션
        }
    }

    @PostMapping("/post/update")
    public String update(@ModelAttribute Post post, @RequestParam Long categoryId) {
        Category category = categoryService.findById(categoryId);
        if (category != null) {
            post.setCategory(category);
            postService.save(post);
        }
        return "redirect:/";
    }

    @GetMapping("/new")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("categories", categoryService.findAll());
        return "new";
    }

    @PostMapping("/post")
    public String create(@ModelAttribute Post post, @RequestParam(required = false) Long categoryId, @RequestParam(required = false) String categoryIdNew, @RequestParam(required = false) String newCategoryName, @RequestParam(required = false) String newCategoryColor) {
        Category category = null;
        if (categoryId != null) {
            category = categoryService.findById(categoryId);
        } else if (categoryIdNew != null && !categoryIdNew.isEmpty()) {
            Category newCategory = new Category();
            newCategory.setName(newCategoryName);
            newCategory.setColor(newCategoryColor);
            categoryService.save(newCategory);
            category = newCategory;
        }
        post.setCategory(category);
        postService.save(post);
        return "redirect:/";
    }

    @GetMapping("/post/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        postService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/category/{id}")
    public String viewCategory(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.findById(id);
        if (category != null) {
            model.addAttribute("posts", postService.findByCategoryId(id));
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("currentCategory", category);
        } else {
            model.addAttribute("posts", postService.findAll());
            model.addAttribute("categories", categoryService.findAll());
        }
        return "index";
    }
}
