import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaEventos {


    private List<Evento> eventos;
    private List<Usuario> usuarios;

    public SistemaEventos() {
        this.eventos = GerenciadorDeArquivos.carregarEventos();
        this.usuarios = new ArrayList<>();
    }


    public void criarNovoEvento(String nome, String endereco, String categoria, LocalDateTime horario, String descricao) {
        Evento novoEvento = new Evento(nome, endereco, categoria, horario, descricao);
        this.eventos.add(novoEvento);
        System.out.println("Evento '" + nome + "' criado com sucesso!");
    }

    public void salvarAlteracoes() {
        GerenciadorDeArquivos.salvarEventos(this.eventos);
    }

    public List<Evento> getTodosOsEventos() {
        return this.eventos;
    }

    public List<Evento> getProximosEventos() {
        LocalDateTime agora = LocalDateTime.now();
        return this.eventos.stream()
                .filter(evento -> evento.getHorario().isAfter(agora))
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());
    }

    public List<Evento> getEventosPassados() {
        LocalDateTime agora = LocalDateTime.now();
        return this.eventos.stream()
                .filter(evento -> evento.getHorario().isBefore(agora))
                .sorted(Comparator.comparing(Evento::getHorario).reversed())
                .collect(Collectors.toList());
    }

    public List<Evento> getEventosOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime duasHorasAtras = agora.minusHours(2);
        return this.eventos.stream()
                .filter(evento -> evento.getHorario().isAfter(duasHorasAtras) && evento.getHorario().isBefore(agora))
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());
    }


    /**
     * Cadastra um novo usuário no sistema, se o e-mail não estiver em uso.
     */
    public boolean cadastrarUsuario(String nome, String email, String cidade) {
        // Verifica se já existe um usuário com este e-mail
        if (encontrarUsuarioPorEmail(email) != null) {
            System.out.println("Erro: Já existe um usuário cadastrado com este e-mail.");
            return false;
        }
        Usuario novoUsuario = new Usuario(nome, email, cidade);
        this.usuarios.add(novoUsuario);
        return true;
    }

    /**
     * Retorna a lista de todos os usuários cadastrados.
     */
    public List<Usuario> getTodosOsUsuarios() {
        return this.usuarios;
    }

    /**
     * Procura por um usuário na lista com base no e-mail.
     * @return O objeto Usuario se encontrado, ou null se não encontrar.
     */
    public Usuario encontrarUsuarioPorEmail(String email) {
        for (Usuario usuario : this.usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Inscreve um usuário em um evento.
     * @return true se a inscrição for bem-sucedida, false se o usuário já estiver inscrito.
     */
    public boolean inscreverUsuarioEmEvento(Usuario usuario, Evento evento) {

        if (evento.getParticipantes().contains(usuario)) {
            return false;
        }
        evento.adicionarParticipante(usuario);
        return true;
    }
}