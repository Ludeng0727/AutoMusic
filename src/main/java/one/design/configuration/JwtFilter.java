package one.design.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.design.error.SimpleError;
import one.design.service.UserService;
import one.design.utils.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Filter;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    private void tokenCheck(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(token == null){//
            log.info("토큰 없음");
            filterChain.doFilter(request,response);
            return;
        }

        if(JwtUtil.isExpired(token, secretKey)){
            log.info("토큰 만료");
            filterChain.doFilter(request,response);
            return;
        }

        String userId = JwtUtil.getUserId(token,secretKey);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("USER")));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        log.info(method + requestURI);

        if(method.equals("POST")&&requestURI.equals("/song")){
            tokenCheck(request,response,filterChain);
        }
        if(method.equals("GET")&&requestURI.equals("/song/my")){
            tokenCheck(request,response,filterChain);
        }
        if (method.equals("DELETE")&&requestURI.startsWith("/song")){
            tokenCheck(request,response,filterChain);
        }


        filterChain.doFilter(request, response);
    }


}
