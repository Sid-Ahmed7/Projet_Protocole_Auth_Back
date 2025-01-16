package com.example.demo.config.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.config.XSSRequestWrapper;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class XSSFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        XSSRequestWrapper xssRequestWrapper = new XSSRequestWrapper(request);

        filterChain.doFilter(xssRequestWrapper, response);
    }
}

