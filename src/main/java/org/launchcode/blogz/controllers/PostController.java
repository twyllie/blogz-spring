package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		
		if (title.equals("") || body.equals("")){
			model.addAttribute("error", "There is something wrong with your post or title.");
			model.addAttribute("title", title);
			model.addAttribute("body", body);
			return "/blog/newpost";
		}
		
		User user = getUserFromSession(request.getSession());
		Post post = new Post(title, body, user);
		postDao.save(post);
		
		return "redirect:"+ user + post.getUid();
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		model.addAttribute("post", postDao.findByUid(uid));
		
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		int userId = userDao.findByUsername(username).getUid();
		List<Post> posts = postDao.findByAuthor(userId);
		model.addAttribute("posts", posts);
		
		return "blog";
	}
	
}
