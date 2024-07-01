package hust.server.app.filter;

import hust.server.app.exception.ApiException;
import hust.server.domain.authen.entities.CustomUserDetails;
import hust.server.domain.authen.service.UserService;
import hust.server.infrastructure.enums.MessageCode;
import hust.server.infrastructure.utilies.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

@Component
public class JwtTokenFilter extends BasicAuthenticationFilter {
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    public JwtTokenFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        // Lấy ra JWT token, parse token lấy ra userId, username và các thông tin cần
        // thiết mà trước đó lúc gen token đã put data vào token
        // Nếu parse được username, user id => tạo ra
        // UsernamePasswordAuthenticationToken và đưa vào context

        String username = null;
        String role = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            if (jwtUtil.isTokenExpired(jwt)) throw new ApiException(MessageCode.TOKEN_ERROR);
            username = jwtUtil.extractUsername(jwt);
        }
        if ((username != null || (jwt != null && jwtUtil.getClaimByRole(jwt).equals("GUEST"))) && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails customUserDetails = new CustomUserDetails(
                    jwtUtil.getClaimByKey(jwt),
                    jwtUtil.extractUsername(jwt),
                    jwtUtil.getClaimByRole(jwt));
            if (jwtUtil.validateToken(jwt, customUserDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");
        filterChain.doFilter(request, response);
    }
}
