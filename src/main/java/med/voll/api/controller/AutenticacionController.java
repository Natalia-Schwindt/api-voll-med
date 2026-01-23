package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DatosAutenticacion;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.DatosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication; // Importante agregar esta
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity iniciarSesion(@RequestBody @Valid DatosAutenticacion datos) {
        // Mantenemos datos.login() y datos.contrasena() como en tu record original
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(datos.login(), datos.contrasena());

        // El manager autentica las credenciales
        var autenticacion = manager.authenticate(authenticationToken);

        // Generamos el token usando el principal (el usuario) ya autenticado
        var tokenJWT = tokenService.generarToken((Usuario) autenticacion.getPrincipal());

        // Retornamos el DTO con el nombre que tú ya tenías: DatosTokenJWT
        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }

}