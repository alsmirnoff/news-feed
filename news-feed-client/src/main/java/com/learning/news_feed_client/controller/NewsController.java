package com.learning.news_feed_client.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learning.news_feed_client.dto.NewsDTO;
import com.learning.news_feed_client.dto.NewsRequest;
import com.learning.news_feed_client.service.NewsFeedService;

@Controller
@RequestMapping("/feed")
public class NewsController {
    
    private final NewsFeedService newsFeedService;

    public NewsController(NewsFeedService newsFeedService) {
        this.newsFeedService = newsFeedService;
    }

// REST
    // @GetMapping
    // public String getAllNews(Model model) {
    //     model.addAttribute("news", newsFeedService.getAllNewsRest().collectList().block());
    //     return "news/feed";
    // }

    // rabbitMQ
    @GetMapping
    public String getAllNews(Model model) {
        List<NewsDTO> news = newsFeedService.getAllNews();
        model.addAttribute("news", news);
        return "news/feed";
    }

// REST
    // @GetMapping("{id}")
    // public String getNewsDetails(@PathVariable Long id, Model model) {
    //     NewsResponse news = newsFeedService.getNewsById(id).block();
    //     model.addAttribute("news", news);
    //     return "news/details";
    // }

// rabbitMQ
    @GetMapping("{id}")
    public String getNewsDetails(@PathVariable int id, Model model) {
        NewsDTO news = newsFeedService.getNewsById(id);
        model.addAttribute("news", news);
        return "news/details";
    }

// REST
    // @GetMapping("/create")
    // public String showCreateForm(Model model) {
    //     model.addAttribute("newsRequest", new NewsRequest(null, null, LocalDate.now()));
    //     return "news/create";
    // }

// rabbitMQ
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        NewsDTO emptyNews = new NewsDTO();
        emptyNews.setDate(LocalDate.now());
        model.addAttribute("newsRequest", emptyNews);
        return "news/create";
    }

// REST
    // @PostMapping("/create")
    // public String createNews(@ModelAttribute NewsRequest request) {
    //     newsFeedService.createNews(request).block();
    //     return "redirect:/feed";
    // }

// rabbitMQ
    @PostMapping("/create")
    public String createNews(@ModelAttribute NewsDTO request) {
        newsFeedService.createNews(request);
        return "redirect:/feed";
    }


// REST
    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsFeedService.deleteNews(id).block();
        return "redirect:/feed";
    }
    
}
