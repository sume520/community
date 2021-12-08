package com.sun.community.controller;

import com.sun.community.entity.User;
import com.sun.community.service.UserService;
import com.sun.community.util.CommunityUtil;
import com.sun.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片！");
            return "/site/setting";
        }

        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf('.'));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确！");
            return "/site/setting";
        }
        if(suffix!=".png"||suffix!=".jpg"||suffix!=".gif"||suffix!="jpeg"){
            model.addAttribute("error","不支持该文件格式，请选择格式为jpg、png、jpeg或gif的图像");
            return "/site/setting";
        }

        //生成随机文件名
        filename = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + filename);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败：" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常!", e);
        }

        //更新当前用户的头像路径（web访问路径）
        //http://localhost:9090/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @GetMapping("/header/{filename}")
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response) {
        //服务器存放路径
        filename = uploadPath + "/" + filename;
        //文件后缀
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        response.setContentType("image/" + suffix);
        try (FileInputStream fis = new FileInputStream(filename);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostMapping("/password")
    public String updatePassword(Model model, String oldPassword,String newPassword,String rePassword) {
        User user = hostHolder.getUser();
        oldPassword=CommunityUtil.md5(oldPassword+user.getSalt());
        if(!oldPassword.equals(user.getPassword())){
            model.addAttribute("oldPasswordMsg","原密码错误！");
            return "/site/setting";
        }

        if(newPassword==null){
            model.addAttribute("newPasswordMsg","新密码不能为空！");
            return "/site/setting";
        }
        if(newPassword.length()<8){
            model.addAttribute("newPasswordMsg","新密码不能小于8位！");
            return "/site/setting";
        }
        if(!newPassword.equals(rePassword)){
            model.addAttribute("rePasswordMsg","两次输入的密码不一致！");
            return "/site/setting";
        }

        userService.updatePassword(user.getId(), newPassword);
        model.addAttribute("msg", "密码修改成功，请重新登录！");
        model.addAttribute("target", "/logout");
        return "/site/operate-result";
    }
}
