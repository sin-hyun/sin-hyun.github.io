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
    public String update(@ModelAttribute Post post) {
        postService.save(post);
        return "redirect:/";
    }

    @GetMapping("/new")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("categories", categoryService.findAll());
        return "new";
    }

    @PostMapping("/post")
    public String create(@ModelAttribute Post post, @RequestParam String category) {
        // 새로운 카테고리가 입력되었는지 확인
        if (!categoryService.findAll().stream().anyMatch(c -> c.getName().equals(category))) {
            Category newCategory = new Category();
            newCategory.setName(category);
            categoryService.save(newCategory);
        }
        post.setCategory(category); // 필드 이름 변경
        postService.save(post);
        return "redirect:/";
    }

    @GetMapping("/post/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        postService.deleteById(id);
        return "redirect:/";
    }
}
