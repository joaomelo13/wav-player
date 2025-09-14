package musicPlayer;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class BuscadordeMusicas {

    public static List<String> buscarMusicas() {
        String diretorioUsuario = System.getProperty("user.home");
        Path caminhoInicial = Paths.get(diretorioUsuario);

        // Criamos a lista aqui fora, pois vamos preenchê-la durante a "caminhada"
        List<String> nomesDasMusicas = new ArrayList<>();

        System.out.println("🔍 Buscando músicas em: " + caminhoInicial);

        try {

            Files.walkFileTree(caminhoInicial, new SimpleFileVisitor<Path>() {

                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String nomeDoArquivo = file.getFileName().toString();
                    if (nomeDoArquivo.toLowerCase().endsWith(".mp3")) {
                        nomesDasMusicas.add(nomeDoArquivo); // Adiciona na nossa lista
                    }
                    return FileVisitResult.CONTINUE; // Diz para a busca continuar
                }

                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Se o erro for de acesso negado, apenas imprime um aviso e continua
                    if (exc instanceof AccessDeniedException) {
                        System.err.println("Acesso negado, ignorando: " + file);
                        return FileVisitResult.CONTINUE; 
                    }
                    throw exc;
                }
            });
        } catch (IOException e) {
            System.err.println("❗ Erro geral durante a busca: " + e.getMessage());
        }

        return nomesDasMusicas;
    }

    public static void main(String[] args) {
        List<String> musicas = buscarMusicas();
        if (musicas.isEmpty()) {
            System.out.println("\nNenhuma música foi encontrada.");
        } else {
            System.out.println("\n🎶 Músicas encontradas (" + musicas.size() + "):");
            musicas.forEach(System.out::println);
        }
    }
}
