import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeArquivos {

    private static final String NOME_ARQUIVO = "events.data";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

       public static void salvarEventos(List<Evento> eventos) {
        try (FileWriter fileWriter = new FileWriter(NOME_ARQUIVO);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            for (Evento evento : eventos) {
                String linha = evento.getNome() + ";" +
                        evento.getEndereco() + ";" +
                        evento.getCategoria() + ";" +
                        evento.getHorario().format(formatter) + ";" +
                        evento.getDescricao();
                printWriter.println(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar eventos no arquivo: " + e.getMessage());
        }
    }


    public static List<Evento> carregarEventos() {
        List<Evento> eventos = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);


        if (!arquivo.exists()) {
            return eventos;
        }

        try (FileReader fileReader = new FileReader(arquivo);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String linha;
                        while ((linha = bufferedReader.readLine()) != null) {

                String[] partes = linha.split(";");


                String nome = partes[0];
                String endereco = partes[1];
                String categoria = partes[2];
                LocalDateTime horario = LocalDateTime.parse(partes[3], formatter); // Converte o texto de volta para data/hora
                String descricao = partes[4];

                Evento evento = new Evento(nome, endereco, categoria, horario, descricao);
                eventos.add(evento);
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar eventos do arquivo: " + e.getMessage());
        }

        return eventos;
    }
}