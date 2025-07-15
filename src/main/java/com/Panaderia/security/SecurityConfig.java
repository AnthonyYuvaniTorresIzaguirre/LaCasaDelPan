package com.Panaderia.security;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Repositorio.ClientesRepositorio;
import com.Panaderia.Servicios.ClientesUserDetailsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final ClientesUserDetailsService userDetailsService;
    

    public SecurityConfig(ClientesUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    private CustomLoginSuccessHandler successHandler;
    
     @Autowired
    private ClientesRepositorio ClienteRepositorio;

    
       
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                
                .authorizeHttpRequests(auth -> auth
                // Páginas públicas
                .requestMatchers("/", "/inicio", "/listapanes", "/listatortas", "/listabocaditos","/listakekes","/listasalados","/preguntasfrecuentes", "/css/**", "/js/**", "/img/**", "/registro", "/login").permitAll()
                // Rutas que requieren usuario autenticado
                .requestMatchers("/carritocompras","/pedido","/RegistroPedido", "/compras/**").hasRole("USER")
                
                            
                         
                        
                .requestMatchers(HttpMethod.POST, "/clientes/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/clientes/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/clientes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasRole("ADMIN")
                        
                        
                .requestMatchers("/reclamos", "/Nuevo").authenticated()

                // Rutas exclusivas para admin
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .usernameParameter("correo")
                .passwordParameter("contraseña")
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public CommandLineRunner encryptPasswords() {
    return args -> {
        List<Clientes> clientes = ClienteRepositorio.findAll();

        for (Clientes cliente : clientes) {
            String rawPassword = cliente.getContraseña(); 
            if (!rawPassword.startsWith("$2a$")) { 
                String encrypted = passwordEncoder().encode(rawPassword);
                //cliente.setPass(rawPassword);(encrypted);
                cliente.setContraseña(encrypted);
                ClienteRepositorio.save(cliente);
            }
        }
    };
    }

}
