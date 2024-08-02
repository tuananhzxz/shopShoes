package com.example.ShopShoes.service.Cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@Service
@AllArgsConstructor
public class ParamService {

    HttpServletRequest request;

    public String getString(String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public int getInt(String name, int defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public double getDouble(String name, double defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return Double.parseDouble(value);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public Date getDate(String name, String pattern) {
        String value = request.getParameter(name);
        if (value == null) {
            return null;
        }
        return null;
    }

    public File save(MultipartFile file, String path) {
        if (!file.isEmpty()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = file.getOriginalFilename();
            File newFile = new File(path + fileName);
            try {
                file.transferTo(newFile);
                return newFile;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
