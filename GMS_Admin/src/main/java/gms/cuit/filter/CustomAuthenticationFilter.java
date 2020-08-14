package gms.cuit.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

//创建filter,将spring security中获得用户名密码的方式改为json获取
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException(
                    "Authentication method not supported" + request.getMethod()
            );
        }
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            String username = null;
            String password = null;
            try{
                Map<String,String> map = new ObjectMapper().readValue(request.getInputStream(),Map.class);
                username = map.get("username");
                password = map.get("password");

            }catch (Exception e){
                e.printStackTrace();
            }
            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
        else {
            return super.attemptAuthentication(request, response);
        }
    }
}