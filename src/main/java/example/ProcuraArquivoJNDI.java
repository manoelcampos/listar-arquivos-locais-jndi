package example;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Aplicação de exemplo que mostra
 * como procurar e ler um arquivo local usando a API
 * Java Naming and Directory Interface (JDNI).
 * @author Manoel Campos
 */
public class ProcuraArquivoJNDI {
    /**
     * Caminho do diretório local para listar procurar arquivos (poderia ser um diretório remoto se, por exemplo,
     * um provedor de Network File System (NFS) fosse utilizado.
     * . indica o diretório atual
     */
    private static final String directoryPath = ".";

    /**
     * Nome da classe que cria o {@link InitialDirContext},
     * fornecida pelo pacote com.sun.messaging.mq.fscontext no arquivo pom.xml
    */
    private static final String initialContextFactoryClassName = "com.sun.jndi.fscontext.RefFSContextFactory";

    public static void main(String[] args) {
        // Configurações do ambiente JNDI
        final var env = new Hashtable<String, String>();

        env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactoryClassName);
        env.put(Context.PROVIDER_URL, "file:" + directoryPath);

        try {
            DirContext context = new InitialDirContext(env);
            final var readmeFile = (File)context.lookup("README.md");
            try(final var reader = new BufferedReader(new FileReader(readmeFile))){
                String linha;
                while((linha = reader.readLine()) != null)
                    System.out.println(linha);
            }

            /*Fecha o contexto, liberando recursos.
            * Não é possível usar o try-with-resources para fechar o contexto
            * automaticamente pois as interfaces Context e DirContext não implementam AutoCloseable.*/
            context.close();
        } catch (NamingException e) {
            System.err.println("Erro no serviço de nomes: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao tentar acessar arquivo" + e.getMessage());
        }
    }

}
