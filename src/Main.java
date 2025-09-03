//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class Main {


    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static SistemaEventos sistema = new SistemaEventos();


    public static void main(String[] args) {
        exibirBannerDeAbertura();

        while (true) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Menu de Eventos");
            System.out.println("2. Menu de Usuários");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    menuEventos();
                    break;
                case "2":
                    menuUsuarios();
                    break;
                case "3":
                    sistema.salvarAlteracoes();
                    System.out.println("Alterações salvas. Saindo do sistema...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida. Por favor, tente novamente.");
                    break;
            }
        }
    }


    private static void exibirBannerDeAbertura() {
        System.out.println("=======================================================");
        System.out.println("||                                                   ||");
        System.out.println("||      A G E N D A   D E   E V E N T O S            ||");
        System.out.println("||                                                   ||");
        System.out.println("=======================================================");
        System.out.println("        Sistema de Gerenciamento de Eventos");
        System.out.println("        Desenvolvido por: Juliano"); // Coloque seu nome aqui!
        System.out.println();
    }

    // --- MENU DE EVENTOS ---
    private static void menuEventos() {
        while (true) {
            System.out.println("\n--- MENU DE EVENTOS ---");
            System.out.println("1. Criar Novo Evento");
            System.out.println("2. Visualizar Eventos");
            System.out.println("3. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();
            switch (opcao) {
                case "1":
                    criarEvento();
                    break;
                case "2":
                    menuVisualizarEventos();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }


    private static void menuUsuarios() {
        while (true) {
            System.out.println("\n--- MENU DE USUÁRIOS ---");
            System.out.println("1. Cadastrar Novo Usuário");
            System.out.println("2. Listar Usuários Cadastrados");
            System.out.println("3. Inscrever-se em um Evento");
            System.out.println("4. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();
            switch (opcao) {
                case "1":
                    cadastrarUsuario();
                    break;
                case "2":
                    listarUsuarios();
                    break;
                case "3":
                    inscreverUsuarioEmEvento();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }



    private static void cadastrarUsuario() {
        System.out.println("\n--- Cadastro de Novo Usuário ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        if (sistema.cadastrarUsuario(nome, email, cidade)) {
            System.out.println("Usuário cadastrado com sucesso!");
        }
    }

    private static void listarUsuarios() {
        System.out.println("\n--- Usuários Cadastrados ---");
        List<Usuario> usuarios = sistema.getTodosOsUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            for (Usuario usuario : usuarios) {
                System.out.println("- " + usuario.getNome() + " (" + usuario.getEmail() + ")");
            }
        }
    }

    private static void inscreverUsuarioEmEvento() {
        System.out.println("\n--- Inscrição em Evento ---");
        System.out.print("Digite o seu e-mail para se identificar: ");
        String email = scanner.nextLine();

        Usuario usuario = sistema.encontrarUsuarioPorEmail(email);
        if (usuario == null) {
            System.out.println("Usuário não encontrado. Por favor, cadastre-se primeiro.");
            return;
        }

        List<Evento> proximosEventos = sistema.getProximosEventos();
        if (proximosEventos.isEmpty()) {
            System.out.println("Não há eventos futuros disponíveis para inscrição.");
            return;
        }

        System.out.println("\n--- Próximos Eventos Disponíveis ---");
        for (int i = 0; i < proximosEventos.size(); i++) {
            System.out.println((i + 1) + ". " + proximosEventos.get(i).getNome());
        }

        System.out.print("Digite o número do evento no qual deseja se inscrever: ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            if (escolha > 0 && escolha <= proximosEventos.size()) {
                Evento eventoEscolhido = proximosEventos.get(escolha - 1);
                if (sistema.inscreverUsuarioEmEvento(usuario, eventoEscolhido)) {
                    System.out.println("Inscrição de " + usuario.getNome() + " no evento '" + eventoEscolhido.getNome() + "' realizada com sucesso!");
                } else {
                    System.out.println("Você já está inscrito neste evento.");
                }
            } else {
                System.out.println("Número de evento inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
        }
    }

    private static void criarEvento() {
        System.out.println("\n--- Criar Novo Evento ---");
        System.out.print("Nome do evento: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();
        LocalDateTime horario = null;
        while (horario == null) {
            System.out.print("Data e Hora (formato dd/MM/yyyy HH:mm): ");
            String horarioStr = scanner.nextLine();
            try {
                horario = LocalDateTime.parse(horarioStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data/hora inválido. Use o formato dd/MM/yyyy HH:mm. Tente novamente.");
            }
        }
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        sistema.criarNovoEvento(nome, endereco, categoria, horario, descricao);
    }

    private static void menuVisualizarEventos() {
        while (true) {
            System.out.println("\n--- VISUALIZAR EVENTOS ---");
            System.out.println("1. Listar Próximos Eventos (Ordenado)");
            System.out.println("2. Listar Eventos Passados");
            System.out.println("3. Listar Eventos Ocorrendo Agora");
            System.out.println("4. Ver Participantes de um Evento");
            System.out.println("5. Voltar");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();
            switch (opcao) {
                case "1":
                    imprimirListaDeEventos(sistema.getProximosEventos(), "Próximos Eventos");
                    break;
                case "2":
                    imprimirListaDeEventos(sistema.getEventosPassados(), "Eventos Passados");
                    break;
                case "3":
                    imprimirListaDeEventos(sistema.getEventosOcorrendoAgora(), "Eventos Ocorrendo Agora");
                    break;
                case "4":
                    verParticipantesDeEvento();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void verParticipantesDeEvento() {
        System.out.println("\n--- Ver Participantes ---");
        List<Evento> todosEventos = sistema.getTodosOsEventos();
        if (todosEventos.isEmpty()) {
            System.out.println("Não há eventos cadastrados.");
            return;
        }

        System.out.println("\n--- Escolha um evento para ver os participantes ---");
        for (int i = 0; i < todosEventos.size(); i++) {
            System.out.println((i + 1) + ". " + todosEventos.get(i).getNome());
        }

        System.out.print("Digite o número do evento: ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            if (escolha > 0 && escolha <= todosEventos.size()) {
                Evento eventoEscolhido = todosEventos.get(escolha - 1);
                List<Usuario> participantes = eventoEscolhido.getParticipantes();
                System.out.println("\n--- Participantes do evento: " + eventoEscolhido.getNome() + " ---");
                if (participantes.isEmpty()) {
                    System.out.println("Ninguém se inscreveu neste evento ainda.");
                } else {
                    for (Usuario p : participantes) {
                        System.out.println("- " + p.getNome() + " (" + p.getEmail() + ")");
                    }
                }
            } else {
                System.out.println("Número de evento inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
        }
    }

    private static void imprimirListaDeEventos(List<Evento> eventos, String titulo) {
        System.out.println("\n--- " + titulo + " ---");
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento encontrado para esta categoria.");
        } else {
            for (int i = 0; i < eventos.size(); i++) {
                Evento evento = eventos.get(i);
                System.out.println("\n--- Evento " + (i + 1) + " ---");
                System.out.println("Nome: " + evento.getNome());
                System.out.println("Horário: " + evento.getHorario().format(formatter));
                System.out.println("Descrição: " + evento.getDescricao());
            }
        }
    }
}