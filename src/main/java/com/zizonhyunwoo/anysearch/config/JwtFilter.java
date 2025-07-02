package com.zizonhyunwoo.anysearch.config;

import com.zizonhyunwoo.anysearch.domain.UserInfo;
import com.zizonhyunwoo.anysearch.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class JwtFilter extends OncePerRequestFilter {

    protected final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return; // 필터 통과 (토큰 없음)
            }
            token = token.substring(7);
            checkToken(token);
            addContext(token);

            filterChain.doFilter(request, response);
            System.out.println("token check finished");
        }catch (Exception e) {
            PrintWriter printWriter = response.getWriter();
            printWriter.print("token error");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void addContext(String token) {
        UserInfo userInfo = new UserInfo(
                jwtUtil.parseJwt(token,"user_name"),
                "",
                jwtUtil.parseJwt(token,"user_email"),
                jwtUtil.parseJwt(token,"user_nickname"),
                jwtUtil.parseJwt(token,"user_role")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userInfo,
                null,
                List.of(
                        (GrantedAuthority) userInfo::getRole
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void checkToken(String token) throws ServletException {
        if (jwtUtil.isExpired(token) || !jwtUtil.parseJwt(token,"type").equals("access")) {
            System.out.println("token is expired");
            throw new ServletException("access token error");
        }
    }
}
