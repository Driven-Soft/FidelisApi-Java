package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Perfil;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailComVinculo(email)
                .orElseThrow(() -> new UsernameNotFoundException("E-mail ou senha inválidos."));

        validarVinculo(usuario);

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .disabled(!usuario.isAtivo())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name())))
                .build();
    }

    /**
     * Reforça em código a mesma regra que já existe como CHECK no banco:
     * um Usuario CLINICA precisa estar vinculado a uma Clinica, e um
     * Usuario TUTOR precisa estar vinculado a um Tutor - nunca os dois,
     * nunca nenhum.
     */
    private void validarVinculo(Usuario usuario) {
        boolean vinculoValido = switch (usuario.getPerfil()) {
            case CLINICA -> usuario.getClinica() != null && usuario.getTutor() == null;
            case TUTOR -> usuario.getTutor() != null && usuario.getClinica() == null;
        };

        if (!vinculoValido) {
            throw new UsernameNotFoundException("E-mail ou senha inválidos.");
        }
    }
}