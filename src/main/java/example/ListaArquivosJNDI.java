package example;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.io.File;
import java.util.Hashtable;

/**
 * Aplicação de exemplo que mostra
 * como listar arquivos locais usando a API  Java Naming and Directory Interface (JDNI).
 * @author Manoel Campos
 */
public class ListaArquivosJNDI {
    /**
     * Caminho do diretório local para listar arquivos (poderia ser um diretório remoto se, por exemplo,
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
            final var context = new InitialDirContext(env);
            final NamingEnumeration<Binding> list = context.listBindings("");

            // Percorre a lista de arquivos obtidos
            while (list.hasMore()) {
                Binding binding = list.next();
                if(binding.getObject() instanceof File f){
                    System.out.printf("%s: %.2f KB%n", f.getName(), f.length()/1024.0);
                }
                else System.out.println(binding.getName());
            }

            /*Fecha o contexto, liberando recursos.
            * Não é possível usar o try-with-resources para fechar o contexto
            * automaticamente pois as interfaces Context e DirContext não implementam AutoCloseable.*/
            context.close();
        } catch (NamingException e) {
            System.err.println("Erro no serviço de nomes: " + e.getMessage());
        }
    }

}
